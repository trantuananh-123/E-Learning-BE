package tran.tuananh.elearning.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tran.tuananh.elearning.entity.BaseEntity;
import tran.tuananh.elearning.entity.CatsNation;
import tran.tuananh.elearning.entity.CatsProvince;
import tran.tuananh.elearning.util.FnCommon;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserRequestDTO extends BaseEntity {

    private String id;

    private String firstName;

    private String lastName;

    private String password;

    private String currentPassword;

    private String confirmPassword;

    private String username;

    private String phone;

    private String email;

    private Date birthday;

    private String address;

    private Integer role;

    private CatsNation catsNation;

    private CatsProvince catsProvince;

    private String accessToken;

    private String refreshToken;

    @JsonIgnore
    private String client_id;

    @JsonIgnore
    private String client_secret;

    @JsonIgnore
    private String grant_type;

    public SysUserRequestDTO(String username, String password) {
        this.password = password;
        this.client_id = FnCommon.getPropertyValue("keycloak.resource");
        this.client_secret  = FnCommon.getPropertyValue("keycloak.credentials.secret");
        this.grant_type = "password";
        this.username = username;
    }

    @Override
    public String toString() {
        return "grant_type=" + grant_type + "&" +
                "client_id=" + client_id + "&" +
                "client_secret=" + client_secret + "&" +
                "username=" + username + "&" +
                "password=" + password;
    }

}
