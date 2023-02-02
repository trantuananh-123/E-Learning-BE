package tran.tuananh.elearning.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponseDTO {

    private String access_token;

    private Integer expires_in;

    private Integer refresh_expires_in;

    private String refresh_token;

    private String token_type;

    private String scope;

}
