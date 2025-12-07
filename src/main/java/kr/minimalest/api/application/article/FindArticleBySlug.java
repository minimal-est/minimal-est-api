package kr.minimalest.api.application.article;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.domain.discovery.tag.repository.ArticleTagRepository;
import kr.minimalest.api.domain.publishing.Author;
import kr.minimalest.api.domain.publishing.PenName;
import kr.minimalest.api.domain.publishing.service.BlogService;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.Slug;
import kr.minimalest.api.domain.writing.exception.ArticleNotFoundException;
import kr.minimalest.api.domain.writing.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Business
@RequiredArgsConstructor
public class FindArticleBySlug {

    private final ArticleRepository articleRepository;
    private final BlogService blogService;
    private final ArticleTagRepository articleTagRepository;

    @Transactional(readOnly = true)
    public FindArticleBySlugResult exec(FindArticleBySlugArgument argument) {
        Slug slugVO = new Slug(argument.slug());
        PenName penName = new PenName(argument.penName());

        Article article = articleRepository.findBySlug(slugVO)
                .orElseThrow(() -> new ArticleNotFoundException("해당 slug의 글이 존재하지 않습니다."));

        Author author = blogService.getAuthor(article.getBlogId())
                .orElseThrow(() -> new RuntimeException("작가를 찾을 수 없습니다."));

        // penName 검증: 해당 글의 작가가 맞는지 확인
        if (!author.getPenName().equals(penName)) {
            throw new ArticleNotFoundException("해당 작가의 글이 아닙니다.");
        }

        List<String> tagNames = articleTagRepository.findTagNamesByArticleId(article.getId());

        return new FindArticleBySlugResult(
                ArticleDetail.from(article, author, tagNames)
        );
    }
}
