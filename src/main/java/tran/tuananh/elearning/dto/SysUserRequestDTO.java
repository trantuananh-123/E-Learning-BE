package tran.tuananh.elearning.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tran.tuananh.elearning.entity.BaseEntity;
import tran.tuananh.elearning.entity.CatsNation;
import tran.tuananh.elearning.entity.CatsProvince;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserRequestDTO extends BaseEntity {

    private String id;
    private String firstName;
    private String lastName;
    private String password;
    private String username;
    private String phone;
    private String email;
    private Date birthday;
    private String address;
    private Integer role;
    private CatsNation catsNation;
    private CatsProvince catsProvince;

}
