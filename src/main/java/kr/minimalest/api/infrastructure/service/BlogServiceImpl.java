package kr.minimalest.api.infrastructure.service;

import kr.minimalest.api.domain.publishing.Blog;
import kr.minimalest.api.domain.publishing.BlogId;
import kr.minimalest.api.domain.publishing.PenName;
import kr.minimalest.api.domain.publishing.exception.BlogNotFoundException;
import kr.minimalest.api.domain.publishing.repository.BlogRepository;
import kr.minimalest.api.domain.publishing.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;

    @Override
    @Transactional(readOnly = true)
    public PenName getPenName(BlogId blogId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new BlogNotFoundException("해당 Blog를 찾을 수 없습니다."));
        return blog.getPenName();
    }

    @Override
    public Map<BlogId, PenName> getMappingPenNames(Collection<BlogId> blogIds) {
        List<Blog> blogs = blogRepository.findAllByIds(blogIds.stream().toList());

        return blogs.stream()
                .collect(Collectors.toMap(Blog::getId, Blog::getPenName));
    }
}
