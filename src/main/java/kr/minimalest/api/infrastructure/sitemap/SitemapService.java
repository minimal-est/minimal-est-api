package kr.minimalest.api.infrastructure.sitemap;

import kr.minimalest.api.domain.publishing.service.BlogService;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SitemapService {

    private final ArticleRepository articleRepository;
    private final BlogService blogService;

    @Transactional(readOnly = true)
    public String generateArticleSitemap(String baseUrl) {
        // 발행된 모든 글 조회
        List<Article> publishedArticles = articleRepository.findAllPublished();

        // BlogId -> PenName 매핑
        var blogIds = publishedArticles.stream()
                .map(Article::getBlogId)
                .distinct()
                .collect(Collectors.toList());

        var penNameMap = blogService.getMappingPenNames(blogIds);

        // Sitemap Entry 생성
        List<SitemapEntry> entries = publishedArticles.stream()
                .filter(article -> article.getSlug() != null && !article.getSlug().value().equals("-"))
                .map(article -> {
                    String penName = penNameMap.get(article.getBlogId()).value();
                    // 프론트엔드 실제 경로: /{penName}/{slug}
                    String url = String.format("%s/articles/%s/%s",
                            baseUrl, penName, article.getSlug().value());

                    return new SitemapEntry(
                            url,
                            article.getUpdatedAt(),
                            "weekly",
                            "0.8"
                    );
                })
                .collect(Collectors.toList());

        return generateXml(entries);
    }

    private String generateXml(List<SitemapEntry> entries) {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        for (SitemapEntry entry : entries) {
            xml.append("  <url>\n");
            xml.append("    <loc>").append(escapeXml(entry.url())).append("</loc>\n");
            xml.append("    <lastmod>").append(entry.lastModified().format(formatter)).append("</lastmod>\n");
            xml.append("  </url>\n");
        }

        xml.append("</urlset>");
        return xml.toString();
    }

    private String escapeXml(String text) {
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
