package tran.tuananh.elearning.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tran.tuananh.elearning.entity.BaseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatsNationRequestDTO extends BaseEntity {

    private Integer id;

    private String name;

    private String code;

    private Integer page;

    private Integer size;
}
