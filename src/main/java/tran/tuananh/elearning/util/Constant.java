package tran.tuananh.elearning.util;


import com.squareup.okhttp.MediaType;

public class Constant {

    public static final MediaType FORM_URLENCODED_BODY = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    public static final MediaType JSON_BODY = MediaType.parse("application/json; charset=utf-8");


    public static final String EMPTY_INPUT = "Dữ liệu đầu vào không được bỏ trống!";
    public static final String NOT_MATCH_CONFIRM_PWD = "Mật khẩu mới và Xác nhận mật khẩu mới không khớp nhau!";
    public static final String INCORRECT_CURRENT_PWD = "Mật khẩu hiện tại không chính xác!";
    public static final String SAME_CURRENT_PWD = "Mật khẩu mới trùng với mật khẩu hiện tại!";
    public static final String UNSAFE_PWD = "Mật khẩu không an toàn!";
    public static final String[] COMMON_PWD = new String[]{""};
}
