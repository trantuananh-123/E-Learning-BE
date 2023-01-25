//package tran.tuananh.elearning.config;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.client.ClientHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.ResponseErrorHandler;
//
//import javax.security.sasl.AuthenticationException;
//import java.io.IOException;
//
//@Component
//public class ErrorHandler implements ResponseErrorHandler {
//
//    @Override
//    public boolean hasError(ClientHttpResponse response) throws IOException {
//        return response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError();
//    }
//
//    @Override
//    public void handleError(ClientHttpResponse response) throws IOException {
//        if (response.getStatusCode().is4xxClientError()) {
//            if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
//                throw new AuthenticationException("Unauthorized access");
//            }
//        }
//    }
//}
