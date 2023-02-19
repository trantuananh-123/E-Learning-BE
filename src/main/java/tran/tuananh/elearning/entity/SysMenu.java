package tran.tuananh.elearning.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "sys_menu")
@Data
public class SysMenu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "parent_id")
    private Integer parentId = 0;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "path")
    private String path;
}
