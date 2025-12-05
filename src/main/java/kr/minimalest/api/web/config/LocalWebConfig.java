package kr.minimalest.api.web.config;

import kr.minimalest.api.web.interceptor.DelayInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Profile({"default"})
@Configuration
@RequiredArgsConstructor
public class LocalWebConfig implements WebMvcConfigurer {

    private final DelayInterceptor delayInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(delayInterceptor)
                .addPathPatterns("/api/**");
    }
}
