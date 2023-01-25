package tran.tuananh.elearning.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "cats_province")
@Data
public class CatsProvince extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @ManyToOne
    @JoinColumn(name = "nation_id")
    private CatsNation catsNation;
}
