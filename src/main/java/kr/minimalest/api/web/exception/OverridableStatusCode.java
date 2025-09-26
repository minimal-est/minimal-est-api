package kr.minimalest.api.web.exception;

import lombok.Getter;

@Getter
public class OverridableStatusCode {

    /**
     * statusCode 속성은 실제 응답 코드가 아닌, '의도'하기 위한 응답 코드입니다.
     * 예를 들어 예외 응답 코드가 500이여도, 실제 응답 코드는 404일 수도 있습니다.
     * 즉, 해당 statusCode는 해당 예외가 의도하는 상태를 나타내는 속성입니다.
     */
    private int statusCode = 500;
    private boolean overrideStatusCode = false;

    protected OverridableStatusCode() {

    }

    protected OverridableStatusCode(int statusCode) {
        this.statusCode = statusCode;
        this.overrideStatusCode = true;
    }
}
