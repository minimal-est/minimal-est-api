package kr.minimalest.api.web.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kr.minimalest.api.web.controller.dto.SignUpRequest;
import org.springframework.util.Assert;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, SignUpRequest> {

    @Override
    public boolean isValid(SignUpRequest request, ConstraintValidatorContext context) {
        Assert.notNull(request, "SignUpRequest는 null일 수 없습니다.");

        boolean isConfirmed = request.password().equals(request.confirmPassword());
        if (!isConfirmed) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("'비밀번호'와 '비밀번호 확인'이 일치하지 않습니다.")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
        }

        return isConfirmed;
    }
}
