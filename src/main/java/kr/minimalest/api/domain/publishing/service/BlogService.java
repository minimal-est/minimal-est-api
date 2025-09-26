package kr.minimalest.api.domain.publishing.service;

import kr.minimalest.api.domain.publishing.BlogId;
import kr.minimalest.api.domain.publishing.PenName;

import java.util.List;
import java.util.Map;

public interface BlogService {

    PenName getPenName(BlogId blogId);

    Map<BlogId, PenName> getMappingPenNames(List<BlogId> blogIds);
}
