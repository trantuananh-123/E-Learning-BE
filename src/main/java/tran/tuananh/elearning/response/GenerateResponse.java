package tran.tuananh.elearning.response;

public class GenerateResponse {

    public static <T> ListResponseData<T> generateListResponseData(String message, Long totalItem, T data, int status) {
        ListResponseData<T> listResponseData = new ListResponseData<>();
        listResponseData.setStatus(status);
        listResponseData.setMessage(message);
        listResponseData.setTotalItem(totalItem);
        listResponseData.setData(data);
        return listResponseData;
    }

    public static <T> DetailResponseData<T> generateDetailResponseData(String message, T data, int status) {
        DetailResponseData<T> detailResponseData = new DetailResponseData<>();
        return detailResponseData;
    }
}
