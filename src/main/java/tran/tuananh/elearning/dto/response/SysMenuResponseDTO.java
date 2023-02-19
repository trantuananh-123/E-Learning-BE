package tran.tuananh.elearning.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tran.tuananh.elearning.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysMenuResponseDTO extends BaseEntity {

    private Integer id;

    private Integer parentId;

    private String code;

    private String name;

    private String path;

    private List<SysMenuResponseDTO> children = new ArrayList<>();
}
