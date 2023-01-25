package tran.tuananh.elearning.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateResponse<T> {

    private Integer status;
    private String message;
    private T data;
}
