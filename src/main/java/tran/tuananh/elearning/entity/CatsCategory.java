package tran.tuananh.elearning.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "cats_category")
@Data
public class CatsCategory extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "parent_id")
    private Integer parentId = 0;
}
