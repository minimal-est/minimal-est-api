package kr.minimalest.api.infrastructure.sitemap;

import java.time.LocalDateTime;

public record SitemapEntry(
        String url,
        LocalDateTime lastModified,
        String changeFreq,
        String priority
) {
}
