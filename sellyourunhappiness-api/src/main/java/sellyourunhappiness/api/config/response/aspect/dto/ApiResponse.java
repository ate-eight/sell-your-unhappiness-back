package sellyourunhappiness.api.config.response.aspect.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApiResponse {
    private ApiResponseCommon common;
    private Object data;

    public ApiResponse(ApiResponseCommon common, Object data) {
        this.common = common;
        this.data = data;
    }

    public static ApiResponse create(ApiResponseCommon common, Object data) {
        return new ApiResponse(common, data);
    }

    public static ApiResponse create(ApiResponseCommon common) {
        return new ApiResponse(common, null);
    }

    public static ApiResponse create(Object data) {
        return new ApiResponse(null, data);
    }

    public void commonUpdate(ApiResponseCommon common) {
        this.common = common;
    }
}


