package kr.minimalest.api.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 요청에 딜레이를 줍니다.
 */
@Component
public class DelayInterceptor implements HandlerInterceptor {

    private final long DELAY_SECONDS = 1;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Thread.sleep(DELAY_SECONDS * 1000);
        return true;
    }
}
