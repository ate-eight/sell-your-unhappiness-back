package sellyourunhappiness.api.config.response.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import sellyourunhappiness.api.config.response.aspect.dto.ApiResponse;

import static sellyourunhappiness.api.config.response.aspect.dto.ApiResponse.create;
import static sellyourunhappiness.api.config.response.aspect.dto.ApiResponseCommon.*;

@Slf4j
@Aspect
@Component
public class ApiResponseAspect {

    @Around("@within(sellyourunhappiness.api.config.response.annotation.ApiResponseAnnotation)")
    public ApiResponse doResponse(ProceedingJoinPoint joinPoint) {
        log.info("[ApiResponse] {}", joinPoint.getSignature());

        try {
            Object proceed = joinPoint.proceed();

            if (proceed instanceof ApiResponse) {
                ApiResponse response = (ApiResponse) proceed;
                response.commonUpdate(success());
                return response;
            }

            throw new IllegalStateException("반환 값이 올바르지 않습니다.");
        } catch (RuntimeException e) {
            return create(notFoundError(e.getMessage()));
        } catch (Throwable e) {
            log.error("Error occurred while processing request: {}", e.getMessage());
            return create(internalServerError());
        }
    }
}


