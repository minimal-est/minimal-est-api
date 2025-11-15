package kr.minimalest.api.application.article;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.ArticleId;
import kr.minimalest.api.domain.writing.exception.ArticleNotFoundException;
import kr.minimalest.api.domain.writing.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Business
@RequiredArgsConstructor
public class DeleteArticle {

    private final ArticleRepository articleRepository;

    @Transactional
    public void exec(DeleteArticleArgument argument) {
        ArticleId articleId = ArticleId.of(argument.articleId());

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException("해당 ID의 글이 존재하지 않습니다."));

        article.delete();
        articleRepository.save(article);
    }
}
