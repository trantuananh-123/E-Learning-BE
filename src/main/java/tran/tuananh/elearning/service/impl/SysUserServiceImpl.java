package tran.tuananh.elearning.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.xmp.impl.Base64;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import tran.tuananh.elearning.dto.SysUserRequestDTO;
import tran.tuananh.elearning.dto.TokenResponseDTO;
import tran.tuananh.elearning.entity.SysUser;
import tran.tuananh.elearning.repository.SysUserRepository;
import tran.tuananh.elearning.response.GenerateResponse;
import tran.tuananh.elearning.service.SysUserService;
import tran.tuananh.elearning.util.CommonFunction;
import tran.tuananh.elearning.util.Mapper;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class SysUserServiceImpl implements SysUserService {

    @Value("${keycloak.auth-server-url}")
    public String serverURL;

    @Value("${keycloak.realm}")
    public String realm;

    @Value("${keycloak.resource}")
    public String clientID;

    @Value("${keycloak.credentials.secret}")
    public String clientSecret;

    @Value("${my-keycloak.token-url}")
    public String tokenUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Mapper mapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SysUserRepository sysUserRepository;

    @Override
    public GenerateResponse<TokenResponseDTO> login(SysUserRequestDTO dto) throws ParseException {
        JSONParser parser = new JSONParser();
        TokenResponseDTO tokenResponseDTO = new TokenResponseDTO();

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.put("grant_type", Collections.singletonList(OAuth2Constants.PASSWORD));
        data.put("client_id", Collections.singletonList(clientID));
        data.put("client_secret", Collections.singletonList(clientSecret));
        data.put("username", Collections.singletonList(dto.getUsername()));
        data.put("password", Collections.singletonList(dto.getPassword()));

//        String tokenUrl = CommonFunction.getPropertyValue("my-keycloak.token-url");
        String url = serverURL.concat("/realms/").concat(realm).concat(tokenUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(data, headers);

        try {
            ResponseEntity<String> token = restTemplate.postForEntity(url, request, String.class);

            tokenResponseDTO = objectMapper.readValue(token.getBody(), TokenResponseDTO.class);
        } catch (Exception e) {
            String message = "{".concat(e.getMessage().split("[{}]")[1]).concat("}");
            JSONObject json = (JSONObject) parser.parse(message);
            String errorMessage = (String) json.get("error_description");
            log.error(e.getMessage());
            return new GenerateResponse<>(HttpStatus.BAD_REQUEST.value(), errorMessage, null);
        }

        return new GenerateResponse<>(HttpStatus.OK.value(), "Login successfully", tokenResponseDTO);
    }

    @Override
    public GenerateResponse<SysUser> getUserDetail() {
        return null;
    }

    @Override
    public GenerateResponse<SysUser> signUp(SysUserRequestDTO dto) {
        SysUser sysUser = mapper.fromTo(dto, SysUser.class);

        List<String> clientRoles = new ArrayList<>();
        List<String> realmRoles = new ArrayList<>();

        if (dto.getRole() == 0) {
            clientRoles = Collections.singletonList("student");
            realmRoles = Collections.singletonList("e-student");
        } else if (dto.getRole() == 1) {
            clientRoles = Collections.singletonList("teacher");
            realmRoles = Collections.singletonList("e-teacher");
        }

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(Base64.encode(dto.getPassword()));
        credentialRepresentation.setTemporary(false);

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(dto.getUsername());
        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));
        userRepresentation.setFirstName(dto.getFirstName());
        userRepresentation.setLastName(dto.getLastName());
        userRepresentation.setEmail(dto.getEmail());
        userRepresentation.setRealmRoles(realmRoles);
        userRepresentation.setEnabled(true);

        Response response = CommonFunction.getUserResource().create(userRepresentation);

        if (response.getStatus() == HttpStatus.CREATED.value()) {
//            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
            String userId = CreatedResponseUtil.getCreatedId(response);
            sysUser.setId(userId);
            sysUser = sysUserRepository.save(sysUser);
        } else if (response.getStatus() == HttpStatus.CONFLICT.value()) {
            return new GenerateResponse<>(HttpStatus.CONFLICT.value(), "User exists with same username", null);
        }
        return new GenerateResponse<>(HttpStatus.CREATED.value(), "SignUp successfully", sysUser);
    }

    @Override
    public GenerateResponse<SysUser> updateProfile(SysUserRequestDTO dto) {
        SysUser sysUser = mapper.fromTo(dto, SysUser.class);
        List<UserRepresentation> userRepresentationList = CommonFunction.getRealmResource().users().search(dto.getUsername());
        if (userRepresentationList.size() != 0) {
            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
            credentialRepresentation.setValue(Base64.encode(dto.getPassword()));
            credentialRepresentation.setTemporary(false);

            UserRepresentation userRepresentation = userRepresentationList.get(0);
            userRepresentation.setUsername(dto.getUsername());
            userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));
            userRepresentation.setFirstName(dto.getFirstName());
            userRepresentation.setLastName(dto.getLastName());
            userRepresentation.setEmail(dto.getEmail());

            CommonFunction.getUserResource().get(userRepresentation.getId()).update(userRepresentation);

            sysUser = sysUserRepository.save(sysUser);
        }
        return new GenerateResponse<>(HttpStatus.OK.value(), "Update profile successfully", sysUser);
    }
}
