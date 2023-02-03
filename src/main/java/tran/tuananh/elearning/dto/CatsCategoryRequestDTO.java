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
public class CatsCategoryRequestDTO extends BaseEntity {

    private Integer id;

    private String name;

    private String imageUrl;

    private Integer parentId;

    private Boolean isGetAll;

    private Integer page;

    private Integer size;
}
