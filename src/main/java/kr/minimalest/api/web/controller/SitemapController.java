package kr.minimalest.api.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.minimalest.api.infrastructure.sitemap.SitemapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Sitemap", description = "사이트맵 관련 API")
public class SitemapController {

    private final SitemapService sitemapService;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @GetMapping(value = "/articles/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @Operation(summary = "아티클 Sitemap 조회", description = "발행된 모든 아티클의 sitemap.xml을 생성합니다.")
    public ResponseEntity<String> getArticleSitemap() {
        log.info("Sitemap 요청");
        String sitemapXml = sitemapService.generateArticleSitemap(baseUrl);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_XML)
                .body(sitemapXml);
    }
}
