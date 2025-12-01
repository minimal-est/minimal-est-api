package kr.minimalest.api.application.article;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.domain.publishing.Author;
import kr.minimalest.api.domain.publishing.PenName;
import kr.minimalest.api.domain.publishing.exception.AuthorNotFoundException;
import kr.minimalest.api.domain.publishing.service.BlogService;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.ArticleId;
import kr.minimalest.api.domain.writing.exception.ArticleAccessDeniedException;
import kr.minimalest.api.domain.writing.exception.ArticleNotFoundException;
import kr.minimalest.api.domain.writing.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Business
@RequiredArgsConstructor
public class FindSingleArticle {

    private final ArticleRepository articleRepository;
    private final BlogService blogService;

    @Transactional(readOnly = true)
    public FindSingleArticleResult exec(FindSingleArticleArgument argument) {
        ArticleId articleId = ArticleId.of(argument.articleId());
        PenName penName = PenName.of(argument.penName());

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException("해당 ID의 글이 존재하지 않습니다."));

        Author author = blogService.getAuthorByPenName(penName)
                .orElseThrow(() -> new AuthorNotFoundException("해당 작가는 존재하지 않습니다."));

        // penName, article 작가 검증
        if (!article.isOwnedBy(article.getBlogId())) {
            throw new ArticleAccessDeniedException("해당 블로그의 글이 아닙니다");
        }

        return new FindSingleArticleResult(
                ArticleDetail.from(article, author)
        );
    }
}
