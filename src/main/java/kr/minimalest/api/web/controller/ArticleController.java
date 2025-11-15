package kr.minimalest.api.web.controller;

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
public class ArticleController {

    private final FindRecommendArticles findRecommendArticles;

    @GetMapping("/recommend")
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
