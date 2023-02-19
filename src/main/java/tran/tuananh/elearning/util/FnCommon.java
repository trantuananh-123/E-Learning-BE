package tran.tuananh.elearning.util;

import com.google.gson.Gson;
import com.squareup.okhttp.*;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.AuthorizationResource;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import tran.tuananh.elearning.dto.request.SysUserRequestDTO;
import tran.tuananh.elearning.dto.response.TokenResponseDTO;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class FnCommon {

    public static String serverURL;
    public static String realm;
    public static String clientID;
    public static String clientSecret;
    public static String username;
    public static String password;

    @Value("${keycloak.auth-server-url}")
    public void setServerURL(String serverURL) {
        this.serverURL = serverURL;
    }

    @Value("${keycloak.realm}")
    public void setRealm(String realm) {
        this.realm = realm;
    }

    @Value("${keycloak.resource}")
    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    @Value("${keycloak.credentials.secret}")
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    @Value("${user.keycloak.admin.username}")
    public void setUsername(String username) {
        this.username = username;
    }

    @Value("${user.keycloak.admin.password}")
    public void setpassword(String password) {
        this.password = password;
    }

    @Autowired
    private static Environment env;

    public static Keycloak getKeycloak() {
        return KeycloakBuilder.builder()
                .realm(realm)
                .serverUrl(serverURL)
                .clientId(clientID)
                .clientSecret(clientSecret)
                .grantType(OAuth2Constants.PASSWORD)
                .username(username)
                .password(password)
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(Integer.MAX_VALUE).build())
                .build();
    }

    public static RealmResource getRealmResource() {
        return getKeycloak().realm(realm);
    }

    public static ClientResource getClientResource() {
        return getRealmResource().clients().get(clientID);
    }

    public static UsersResource getUserResource() {
        return getRealmResource().users();
    }

    public static AuthorizationResource getAuthorizationResource() {
        return getClientResource().authorization();
    }

    public static String getUserId() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            KeycloakPrincipal keycloakPrincipal = (KeycloakPrincipal) authentication.getPrincipal();
            return keycloakPrincipal.getKeycloakSecurityContext().getToken().getSubject();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static String getUserToken() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            KeycloakPrincipal keycloakPrincipal = (KeycloakPrincipal) authentication.getPrincipal();
            return keycloakPrincipal.getKeycloakSecurityContext().getTokenString();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static String getPropertyValue(String propertyName) {
        return env.getProperty(propertyName);
    }

    public static boolean isNullOrEmpty(String s) {
        if (s == null || s.isEmpty()) {
            return true;
        }
        s = s.trim();
        if ("".equals(s)) {
            return true;
        }
        return false;
    }

    public static String getUserToken(String username, String password) {
        OkHttpClient client = new OkHttpClient();
        SysUserRequestDTO dto = new SysUserRequestDTO(username, password);
        RequestBody body = RequestBody.create(Constant.FORM_URLENCODED_BODY, dto.toString());
        try {
            client.setConnectTimeout(30, TimeUnit.SECONDS);
            client.setReadTimeout(30, TimeUnit.SECONDS);
            client.setWriteTimeout(30, TimeUnit.SECONDS);
            HttpUrl.Builder httpBuilder = HttpUrl.parse(FnCommon.getPropertyValue("keycloak.auth-server-url") + FnCommon.getPropertyValue("my-keycloak.token-url")).newBuilder().username(dto.getUsername()).password(dto.getPassword());
            Request request = new Request.Builder()
                    .header("Accept", "*/*")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .url(httpBuilder.build())
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            TokenResponseDTO responseDTO;
            if (response != null) {
                responseDTO = new Gson().fromJson(response.body().string(), TokenResponseDTO.class);
                return responseDTO.getAccess_token();
            }
        } catch (Exception e) {
            log.error("Has error", e);
        }
        return "";
    }
}
