package sellyourunhappiness.api.config.response.aspect.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponseCommon {
    private int code;
    private String message;
    private boolean success;

    public ApiResponseCommon(HttpStatus code, String message, boolean success) {
        this.code = code.value();
        this.message = message;
        this.success = success;
    }

    public static ApiResponseCommon success() {
        return new ApiResponseCommon(HttpStatus.OK, "", true);
    }

    public static ApiResponseCommon notFoundError(String message) {
        return new ApiResponseCommon(HttpStatus.NOT_FOUND, message, false);
    }

    public static ApiResponseCommon internalServerError() {
        return new ApiResponseCommon(HttpStatus.INTERNAL_SERVER_ERROR, "관리자에게 문의해 주세요.", false);
    }
}
