package kr.minimalest.api.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.minimalest.api.application.article.FindRecommendArticles;
import kr.minimalest.api.application.article.FindRecommendArticlesArgument;
import kr.minimalest.api.application.article.FindRecommendArticlesResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
@Tag(name = "Article", description = "아티클 추천 관련 API")
public class ArticleController {

    private final FindRecommendArticles findRecommendArticles;

    @GetMapping("/recommend")
    @Operation(summary = "추천 아티클 조회", description = "추천 알고리즘에 따른 아티클 목록을 조회합니다. (페이지, 제한 갯수 지정 가능)")
    public ResponseEntity<?> findArticles(
            // 추천 알고리즘에 따라 변경될 수 있음
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer limit
    ) {
        if (page == null) page = 0;
        if (limit == null) limit = 5;
        FindRecommendArticlesArgument argument = FindRecommendArticlesArgument.limitOf(page, limit);
        FindRecommendArticlesResult result = findRecommendArticles.exec(argument);
        return ResponseEntity.ok(result);
    }
}
