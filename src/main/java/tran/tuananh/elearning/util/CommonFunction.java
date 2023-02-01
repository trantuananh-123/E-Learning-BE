package tran.tuananh.elearning.util;

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

@Component
@Slf4j
public class CommonFunction {

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
            String userId = keycloakPrincipal.getKeycloakSecurityContext().getToken().getSubject();
            return userId;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static String getPropertyValue(String propertyName) {
        return env.getProperty(propertyName);
    }
}
