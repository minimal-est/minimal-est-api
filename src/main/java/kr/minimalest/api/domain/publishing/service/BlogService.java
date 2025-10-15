package kr.minimalest.api.domain.publishing.service;

import kr.minimalest.api.domain.publishing.BlogId;
import kr.minimalest.api.domain.publishing.PenName;

import java.util.Collection;
import java.util.Map;

public interface BlogService {

    PenName getPenName(BlogId blogId);

    /**
     * BlogId 목록으로 PenName을 꺼낼 수 있습니다.
     */
    Map<BlogId, PenName> getMappingPenNames(Collection<BlogId> blogIds);
}
