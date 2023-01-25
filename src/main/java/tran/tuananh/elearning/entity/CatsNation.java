package tran.tuananh.elearning.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cats_nation")
@Data
public class CatsNation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @OneToMany(mappedBy = "catsNation", cascade = CascadeType.ALL)
    private List<CatsProvince> catsProvinces;
}
