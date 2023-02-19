package tran.tuananh.elearning.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import tran.tuananh.elearning.dto.request.SysUserRequestDTO;
import tran.tuananh.elearning.dto.response.TokenResponseDTO;
import tran.tuananh.elearning.entity.SysUser;
import tran.tuananh.elearning.repository.SysUserRepository;
import tran.tuananh.elearning.response.DetailResponseData;
import tran.tuananh.elearning.response.GenerateResponse;
import tran.tuananh.elearning.service.SysUserService;
import tran.tuananh.elearning.util.Constant;
import tran.tuananh.elearning.util.FnCommon;
import tran.tuananh.elearning.util.Mapper;

import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.util.*;

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

    @Value("${my-keycloak.revoke-token-url}")
    public String revokeTokenUrl;

    @Value("${my-keycloak.update-password-url}")
    public String updatePasswordUrl;

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
    public DetailResponseData<TokenResponseDTO> login(SysUserRequestDTO dto) throws ParseException {
        SysUser sysUser = sysUserRepository.findByUsernameEqualsOrEmailEquals(dto.getUsername(), dto.getEmail());

        if (sysUser == null) {
            return GenerateResponse.generateDetailResponseData("Incorrect username/email or password", null, HttpStatus.UNAUTHORIZED.value());
        }

        JSONParser parser = new JSONParser();
        TokenResponseDTO tokenResponseDTO = new TokenResponseDTO();

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.put("grant_type", Collections.singletonList(OAuth2Constants.PASSWORD));
        data.put("client_id", Collections.singletonList(clientID));
        data.put("client_secret", Collections.singletonList(clientSecret));
        data.put("username", Collections.singletonList(sysUser.getUsername()));
        data.put("password", Collections.singletonList(dto.getPassword()));

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
            if (errorMessage.equals("Invalid user credentials")) {
                return GenerateResponse.generateDetailResponseData(errorMessage, null, HttpStatus.UNAUTHORIZED.value());
            }
            return GenerateResponse.generateDetailResponseData(errorMessage, null, HttpStatus.BAD_REQUEST.value());
        }

        return GenerateResponse.generateDetailResponseData("Login successfully", tokenResponseDTO, HttpStatus.OK.value());
    }

    @Override
    public DetailResponseData<?> logout(SysUserRequestDTO dto) {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.put("client_id", Collections.singletonList(clientID));
        data.put("client_secret", Collections.singletonList(clientSecret));
        data.put("token", Collections.singletonList(dto.getAccessToken()));

        String url = serverURL.concat("/realms/").concat(realm).concat(revokeTokenUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(data, headers);

        ResponseEntity<String> response = null;
//        Revoke access token
        try {
            response = restTemplate.postForEntity(url, request, String.class);
        } catch (Exception e) {
            return GenerateResponse.generateDetailResponseData(e.getMessage(), null, response.getStatusCodeValue());
        }

        data.put("token", Collections.singletonList(dto.getRefreshToken()));
        request = new HttpEntity<>(data, headers);

//        Revoke refresh token
        try {
            response = restTemplate.postForEntity(url, request, String.class);
        } catch (Exception e) {
            return GenerateResponse.generateDetailResponseData(e.getMessage(), null, response.getStatusCodeValue());
        }

        return GenerateResponse.generateDetailResponseData("Logout successfully", null, HttpStatus.OK.value());
    }

    @Transactional
    @Override
    public DetailResponseData<SysUser> signUp(SysUserRequestDTO dto) {
        SysUser sysUser = sysUserRepository.findByUsernameEqualsOrEmailEquals(dto.getUsername(), dto.getEmail());

        if (sysUser != null) {
            if (sysUser.equals(dto.getUsername())) {
                return GenerateResponse.generateDetailResponseData("User exists with same username", null, HttpStatus.CONFLICT.value());
            } else if (sysUser.equals(dto.getEmail())) {
                return GenerateResponse.generateDetailResponseData("User exists with same email", null, HttpStatus.CONFLICT.value());
            }
        } else {
            sysUser = mapper.fromTo(dto, SysUser.class);
        }

        List<String> clientRoles = new ArrayList<>();
        List<String> realmRoles = new ArrayList<>();

//        if (dto.getRole() == 0) {
//            clientRoles = Collections.singletonList("student");
//            realmRoles = Collections.singletonList("e-student");
//        } else if (dto.getRole() == 1) {
//            clientRoles = Collections.singletonList("teacher");
//            realmRoles = Collections.singletonList("e-teacher");
//        }

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(dto.getPassword());
        credentialRepresentation.setTemporary(false);

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(dto.getUsername());
        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));
        userRepresentation.setFirstName(dto.getFirstName());
        userRepresentation.setLastName(dto.getLastName());
        userRepresentation.setEmail(dto.getEmail());
//        userRepresentation.setRealmRoles(realmRoles);
        userRepresentation.setEnabled(true);

        Response response = FnCommon.getUserResource().create(userRepresentation);

        if (response.getStatus() == HttpStatus.CREATED.value()) {
//            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
            String userId = CreatedResponseUtil.getCreatedId(response);
            sysUser.setId(userId);
            sysUser = sysUserRepository.save(sysUser);

            RoleRepresentation roleRepresentation = new RoleRepresentation();
            if (dto.getRole() == 0) {
                roleRepresentation = FnCommon.getRealmResource().roles().get("e-student").toRepresentation();
            } else if (dto.getRole() == 1) {
                roleRepresentation = FnCommon.getRealmResource().roles().get("e-teacher").toRepresentation();
            }
            FnCommon.getRealmResource().users().get(userId).roles().realmLevel().add(Collections.singletonList(roleRepresentation));
        } else if (response.getStatus() == HttpStatus.CONFLICT.value()) {
            return GenerateResponse.generateDetailResponseData("User exists with same username", null, HttpStatus.CONFLICT.value());
        } else if (response.getStatus() == HttpStatus.BAD_REQUEST.value()) {
            return GenerateResponse.generateDetailResponseData("Bad request", null, HttpStatus.BAD_REQUEST.value());
        } else {
            return GenerateResponse.generateDetailResponseData("Error", null, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return GenerateResponse.generateDetailResponseData("SignUp successfully", sysUser, HttpStatus.CREATED.value());
    }

    @Override
    public DetailResponseData<SysUser> delete(SysUserRequestDTO dto) {
        SysUser sysUser = mapper.fromTo(dto, SysUser.class);

        String userId = FnCommon.getUserId();
        if (userId != null) {
            FnCommon.getUserResource().get(userId).remove();
            sysUser.setId(userId);
            sysUser = sysUserRepository.save(sysUser);
        } else {
            return GenerateResponse.generateDetailResponseData("User does not exist", null, HttpStatus.BAD_REQUEST.value());
        }
        return GenerateResponse.generateDetailResponseData("Delete user successfully", sysUser, HttpStatus.OK.value());
    }

    @Override
    public DetailResponseData<SysUser> lockAndUnlock(SysUserRequestDTO dto) {
        String message = dto.getIsActive() ? "Unlock" : "Lock";
        SysUser sysUser = mapper.fromTo(dto, SysUser.class);
        List<UserRepresentation> userRepresentationList = FnCommon.getRealmResource().users().search(dto.getUsername());
        if (userRepresentationList.size() != 0) {
            UserRepresentation userRepresentation = userRepresentationList.get(0);
            userRepresentation.setEnabled(dto.getIsActive());
            FnCommon.getUserResource().get(userRepresentation.getId()).update(userRepresentation);
            sysUser.setId(userRepresentation.getId());
            sysUser = sysUserRepository.save(sysUser);
        } else {
            return GenerateResponse.generateDetailResponseData(message + " user failed", null, HttpStatus.BAD_REQUEST.value());
        }
        return GenerateResponse.generateDetailResponseData(message + " user successfully", sysUser, HttpStatus.OK.value());
    }

    @Override
    public DetailResponseData<SysUser> getUserDetail() {
        try {
            Optional<SysUser> optionalSysUser = sysUserRepository.findById(FnCommon.getUserId());
            if(optionalSysUser.isPresent()) {
                SysUser sysUser = optionalSysUser.get();
                return GenerateResponse.generateDetailResponseData("Get user successfully", sysUser, HttpStatus.OK.value());
            } else {
                return GenerateResponse.generateDetailResponseData("User does not exist", null, HttpStatus.BAD_REQUEST.value());
            }
        } catch (Exception e) {
            return GenerateResponse.generateDetailResponseData(e.getMessage() + "Error", null, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public DetailResponseData<SysUser> updateProfile(SysUserRequestDTO dto) {
        SysUser sysUser = mapper.fromTo(dto, SysUser.class);
        List<UserRepresentation> userRepresentationList = FnCommon.getRealmResource().users().search(dto.getUsername());
        if (userRepresentationList.size() != 0) {
//            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
//            credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
//            credentialRepresentation.setValue(dto.getPassword());
//            credentialRepresentation.setTemporary(false);

            UserRepresentation userRepresentation = userRepresentationList.get(0);
            userRepresentation.setUsername(dto.getUsername());
//            userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));
            userRepresentation.setFirstName(dto.getFirstName());
            userRepresentation.setLastName(dto.getLastName());
            userRepresentation.setEmail(dto.getEmail());

            FnCommon.getUserResource().get(userRepresentation.getId()).update(userRepresentation);

            sysUser.setId(userRepresentation.getId());
            sysUser = sysUserRepository.save(sysUser);
        } else {
            return GenerateResponse.generateDetailResponseData("User does not exist", null, HttpStatus.BAD_REQUEST.value());
        }
        return GenerateResponse.generateDetailResponseData("Delete user successfully", sysUser, HttpStatus.OK.value());
    }

    @Override
    public DetailResponseData<SysUser> updatePassword(SysUserRequestDTO dto) {
        SysUser sysUser = new SysUser();
        String userId = FnCommon.getUserId();
        if (userId != null) {

            String currentPassword = dto.getCurrentPassword();
            String newPassword = dto.getPassword();
            String confirmPassword = dto.getConfirmPassword();

            String validation = validatePassword(dto.getUsername(), currentPassword, newPassword, confirmPassword);
            if (!FnCommon.isNullOrEmpty(validation)) {
                return GenerateResponse.generateDetailResponseData(validation, null, HttpStatus.BAD_REQUEST.value());
            }

            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
            credentialRepresentation.setValue(newPassword);
            credentialRepresentation.setTemporary(false);

            UserResource userResource = FnCommon.getUserResource().get(userId);
            userResource.resetPassword(credentialRepresentation);

            return GenerateResponse.generateDetailResponseData("Update password successfully", sysUser, HttpStatus.OK.value());
        } else {
            return GenerateResponse.generateDetailResponseData("User does not exist", null, HttpStatus.BAD_REQUEST.value());
        }
    }

    @Override
    public DetailResponseData<SysUser> updateEmail(SysUserRequestDTO dto) {
        return null;
    }

    @Override
    public DetailResponseData<TokenResponseDTO> refreshToken(SysUserRequestDTO dto) throws ParseException {
        JSONParser parser = new JSONParser();
        TokenResponseDTO tokenResponseDTO = new TokenResponseDTO();

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.put("grant_type", Collections.singletonList(OAuth2Constants.REFRESH_TOKEN));
        data.put("client_id", Collections.singletonList(clientID));
        data.put("client_secret", Collections.singletonList(clientSecret));
        data.put("refresh_token", Collections.singletonList(dto.getRefreshToken()));

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
            return GenerateResponse.generateDetailResponseData(errorMessage, null, HttpStatus.BAD_REQUEST.value());
        }

        return GenerateResponse.generateDetailResponseData("Refresh token successfully", tokenResponseDTO, HttpStatus.OK.value());
    }

    private String validatePassword(String username, String currentPassword, String password, String confirmPassword) {
        if (FnCommon.isNullOrEmpty(currentPassword) || FnCommon.isNullOrEmpty(password) || FnCommon.isNullOrEmpty(confirmPassword)) {
            return Constant.EMPTY_INPUT;
        }
        String token = FnCommon.getUserToken(username, currentPassword);
        if (token == null) {
            return Constant.INCORRECT_CURRENT_PWD;
        }
        if (!password.equals(confirmPassword)) {
            return Constant.NOT_MATCH_CONFIRM_PWD;
        }
        if (currentPassword.equals(password)) {
            return Constant.SAME_CURRENT_PWD;
        }
        if (Arrays.stream(Constant.COMMON_PWD).anyMatch(password::contains)) {
            return Constant.UNSAFE_PWD;
        }
        return "";
    }
}
