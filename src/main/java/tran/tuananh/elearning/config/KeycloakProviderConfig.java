//package tran.tuananh.elearning.config;
//
//import org.keycloak.OAuth2Constants;
//import org.keycloak.admin.client.Keycloak;
//import org.keycloak.admin.client.KeycloakBuilder;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//@Component
//public class KeycloakProviderConfig {
//
//    @Value("${keycloak.auth-server-url}")
//    public String serverURL;
//
//    @Value("${keycloak.realm}")
//    public String realm;
//
//    @Value("${keycloak.resource}")
//    public String clientID;
//
//    @Value("${keycloak.credentials.secret}")
//    public String clientSecret;
//
//    @Value("${user.keycloak.admin.username}")
//    private String username;
//
//    @Value("${user.keycloak.admin.password}")
//    private String password;
//
//    public Keycloak getInstance() {
//        return KeycloakBuilder.builder()
//                .realm(realm)
//                .serverUrl(serverURL)
//                .clientId(clientID)
//                .clientSecret(clientSecret)
//                .grantType(OAuth2Constants.PASSWORD)
//                .username(username)
//                .password(password)
//                .build();
//    }
//}
