package kr.minimalest.api.web.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.Assert;

public record CreateBlogRequest(
        @NotBlank
        @Length(min = 2, max = 30)
        String penName
) {
    public CreateBlogRequest {
        Assert.hasText(penName, "penName은 값을 가져야합니다.");
    }

    public static CreateBlogRequest of(String penName) {
        return new CreateBlogRequest(penName);
    }
}
