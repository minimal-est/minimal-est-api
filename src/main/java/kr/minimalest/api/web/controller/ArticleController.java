package kr.minimalest.api.web.controller;

import kr.minimalest.api.application.article.*;
import kr.minimalest.api.domain.writing.ArticleId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ArticleController {

    private final FindSingleArticle findSingleArticle;
    private final FindRecommendArticles findRecommendArticles;

    @GetMapping("/{articleId}")
    public ResponseEntity<?> findSingleArticle(
            @PathVariable UUID articleId
    ) {
        // 아티클 정보 불러오기
        FindSingleArticleArgument arg = FindSingleArticleArgument.of(ArticleId.of(articleId));
        FindSingleArticleResult result = findSingleArticle.exec(arg);

        // 아티클 상호작용

        // 응답 데이터
        Map<String, Object> res = Map.of(
                "articleId", result.articleId(),
                "title", result.title(),
                "content", result.content(),
                "completedAt", result.completedAt()
        );

        return ResponseEntity.ok(res);
    }

    @GetMapping("/recommend")
    public ResponseEntity<?> findArticles(
            // 추천 알고리즘에 따라 변경될 수 있음
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer limit
    ) throws Exception {
        if (page == null) page = 0;
        if (limit == null) limit = 5;
        FindRecommendArticlesArgument argument = FindRecommendArticlesArgument.limitOf(page, limit);
        FindRecommendArticlesResult result = findRecommendArticles.exec(argument);
        return ResponseEntity.ok(result);
    }
}
