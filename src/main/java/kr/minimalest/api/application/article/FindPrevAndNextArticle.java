package kr.minimalest.api.application.article;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.domain.publishing.Author;
import kr.minimalest.api.domain.publishing.exception.BlogNotFoundException;
import kr.minimalest.api.domain.publishing.repository.BlogRepository;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.ArticleId;
import kr.minimalest.api.domain.writing.exception.ArticleNotFoundException;
import kr.minimalest.api.domain.writing.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Business
@RequiredArgsConstructor
public class FindPrevAndNextArticle {

    private final ArticleRepository articleRepository;
    private final BlogRepository blogRepository;

    /**
     * 이전, 다음 글을 조회합니다.
     * 만약 이전 글이나 다음 글이 존재하지 않는다면, null을 반환합니다.
     */
    @Transactional(readOnly = true)
    public FindPrevAndNextArticleResult exec(FindPrevAndNextArticleArgument argument) {
        ArticleId pivotArticleId = ArticleId.of(argument.articleId());

        Article pivotArticle = articleRepository.findById(pivotArticleId)
                .orElseThrow(() -> new ArticleNotFoundException("해당 아티클이 존재하지 않습니다: " + pivotArticleId.id()));

        Author author = blogRepository.findAuthorById(pivotArticle.getBlogId())
                .orElseThrow(() -> new BlogNotFoundException("해당 아티클의 블로그 정보를 찾을 수 없습니다."));

        Article prevArticle = articleRepository.findPrevPublishedArticle(pivotArticleId).orElse(null);
        Article nextArticle = articleRepository.findNextPublishedArticle(pivotArticleId).orElse(null);

        return FindPrevAndNextArticleResult.of(prevArticle, nextArticle, author);
    }
}
