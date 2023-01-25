package tran.tuananh.elearning.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.OAuth2Constants;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import tran.tuananh.elearning.dto.SysUserRequestDTO;
import tran.tuananh.elearning.dto.TokenResponseDTO;
import tran.tuananh.elearning.entity.SysUser;
import tran.tuananh.elearning.response.GenerateResponse;
import tran.tuananh.elearning.service.SysUserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Value("${keycloak.auth-server-url}")
    public String serverURL;

    @Value("${keycloak.realm}")
    public String realm;

    @Value("${keycloak.resource}")
    public String clientID;

    @Value("${keycloak.credentials.secret}")
    public String clientSecret;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public GenerateResponse<SysUser> login(SysUserRequestDTO dto, HttpServletRequest httpServletRequest) throws ParseException {
        JSONParser parser = new JSONParser();
        TokenResponseDTO tokenResponseDTO = new TokenResponseDTO();

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.put("grant_type", Collections.singletonList(OAuth2Constants.PASSWORD));
        data.put("client_id", Collections.singletonList(clientID));
        data.put("client_secret", Collections.singletonList(clientSecret));
        data.put("username", Collections.singletonList(dto.getUsername()));
        data.put("password", Collections.singletonList(dto.getPassword()));

        String url = serverURL.concat("/realms/").concat(realm).concat("/protocol/openid-connect/token");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(data, headers);

        try {
            ResponseEntity<String> token = restTemplate.postForEntity(url, request, String.class);

            tokenResponseDTO = mapper.readValue(token.getBody(), TokenResponseDTO.class);
        } catch (Exception e) {
            String message = "{".concat(e.getMessage().split("[{}]")[1]).concat("}");
            JSONObject json = (JSONObject) parser.parse(message);
            String errorMessage = (String) json.get("error_description");
            return new GenerateResponse(HttpStatus.BAD_REQUEST.value(), errorMessage, null);
        }

        return new GenerateResponse(HttpStatus.OK.value(), "Login successfully", tokenResponseDTO);
    }
}
