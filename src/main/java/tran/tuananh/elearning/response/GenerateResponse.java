package tran.tuananh.elearning.response;

public class GenerateResponse {

    public static <T> ListResponseData<T> generateListResponseData(String message, T data, Long totalItem, Integer status, Integer page, Integer size) {
        ListResponseData<T> listResponseData = new ListResponseData<>();
        listResponseData.setStatus(status);
        listResponseData.setMessage(message);
        listResponseData.setTotalItem(totalItem);
        listResponseData.setPage(page);
        listResponseData.setSize(size);
        listResponseData.setData(data);
        return listResponseData;
    }

    public static <T> DetailResponseData<T> generateDetailResponseData(String message, T data, Integer status) {
        DetailResponseData<T> detailResponseData = new DetailResponseData<>();
        detailResponseData.setMessage(message);
        detailResponseData.setData(data);
        detailResponseData.setStatus(status);
        return detailResponseData;
    }
}
