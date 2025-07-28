package kr.minimalest.api.application.common.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 비즈니스, 서비스 컴포넌트임을 밝힙니다.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Business {

    @AliasFor(annotation = Component.class)
    String value() default "";
}
