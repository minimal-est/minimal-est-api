package kr.minimalest.api.application.article;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.domain.writing.exception.ArticleNotFoundException;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Business
@RequiredArgsConstructor
public class FindSingleArticle {

    private final ArticleRepository articleRepository;

    @Transactional
    public FindSingleArticleResult exec(FindSingleArticleArgument argument) {
        Article article = articleRepository.findById(argument.articleId())
                .orElseThrow(() -> new ArticleNotFoundException("해당 ID의 글이 존재하지 않습니다."));

        return FindSingleArticleResult.of(
                article.getRawId(),
                article.getTitle().value(),
                article.getContent().value(),
                article.getStatus(),
                article.getCompletedAt()
        );
    }
}
