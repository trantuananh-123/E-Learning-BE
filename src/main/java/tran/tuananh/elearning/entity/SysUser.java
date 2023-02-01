package tran.tuananh.elearning.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

@Entity
@Table(name = "sys_user")
@Data
public class SysUser extends BaseEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username")
    private String username;

    @Column(name = "phone", length = 11)
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "birthday", length = 36)
    private Date birthday;

    @Column(name = "address", length = 1000)
    private String address;

    @Column(name = "role")
    @Min(value = 0)
    @Max(value = 2)
    private Integer role;

    @OneToOne
    @JoinColumn(name = "nation_id", referencedColumnName = "id")
    private CatsNation catsNation;

    @OneToOne
    @JoinColumn(name = "province_id", referencedColumnName = "id")
    private CatsProvince catsProvince;
}
