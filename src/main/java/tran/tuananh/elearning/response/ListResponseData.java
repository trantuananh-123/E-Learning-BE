package tran.tuananh.elearning.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListResponseData<T> {

    private Integer status;
    private String message;
    private Long totalItem;
    private T data;
}
