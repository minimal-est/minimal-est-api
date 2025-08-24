package kr.minimalest.api.web.controller;

import jakarta.validation.Valid;
import kr.minimalest.api.application.blog.CreateBlog;
import kr.minimalest.api.application.blog.CreateBlogArgument;
import kr.minimalest.api.application.blog.CreateBlogResult;
import kr.minimalest.api.infrastructure.security.JwtUserDetails;
import kr.minimalest.api.web.controller.dto.CreateBlogRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/blogs")
public class BlogController {

    private final CreateBlog createBlog;

    @PostMapping
    public ResponseEntity<?> createBlog(
            @RequestBody @Valid CreateBlogRequest createBlogRequest,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {
        CreateBlogArgument argument = CreateBlogArgument.of(jwtUserDetails.getUserId(), createBlogRequest.penName());
        CreateBlogResult result = createBlog.exec(argument);
        return ResponseEntity.ok(Map.of("blogId", result.blogId().id()));
    }
}
