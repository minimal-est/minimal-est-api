package kr.minimalest.api.application.blog;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.application.exception.PenNameAlreadyExists;
import kr.minimalest.api.application.exception.UserAlreadyHasBlogException;
import kr.minimalest.api.domain.blog.Blog;
import kr.minimalest.api.domain.blog.BlogId;
import kr.minimalest.api.domain.blog.PenName;
import kr.minimalest.api.domain.blog.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

@Business
@RequiredArgsConstructor
public class CreateBlog {

    private final BlogRepository blogRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public CreateBlogResult exec(CreateBlogArgument argument) {
        Blog blog = Blog.create(argument.userId(), PenName.of(argument.penName()));
        validateBlog(blog);
        BlogId blogId = blogRepository.create(blog);

        publishEvent(blog);
        return CreateBlogResult.of(blogId);
    }

    private void validateBlog(Blog blog) {
        boolean alreadyHasBlog = blogRepository.hasBlogByUserId(blog.getAuthor().getUserId());
        boolean alreadyPenNameExists = blogRepository.existsByPenName(blog.getAuthor().getPenName());

        if (alreadyHasBlog){
            throw new UserAlreadyHasBlogException("사용자의 블로그가 이미 존재합니다.");
        }

        if (alreadyPenNameExists) {
            throw new PenNameAlreadyExists("이미 존재하는 펜네임입니다.");
        }
    }

    private void publishEvent(Blog blog) {
        blog.releaseEvents().forEach(eventPublisher::publishEvent);
    }
}
