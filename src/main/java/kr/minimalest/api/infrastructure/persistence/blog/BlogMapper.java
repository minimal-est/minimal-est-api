package kr.minimalest.api.infrastructure.persistence.blog;

import kr.minimalest.api.domain.blog.*;
import kr.minimalest.api.domain.user.UserId;
import kr.minimalest.api.infrastructure.persistence.user.UserEntity;

public class BlogMapper {

    public static Blog toDomain(BlogEntity blogEntity) {
        if (blogEntity == null) return null;
        return Blog.of(
                BlogId.of(blogEntity.getId()),
                BlogUUID.of(blogEntity.getBlogUUID()),
                UserId.of(blogEntity.getUserEntity().getId()),
                Title.of(blogEntity.getTitle()),
                Description.of(blogEntity.getDescription()),
                blogEntity.getCreatedAt(),
                blogEntity.getUpdatedAt()
        );
    }

    public static BlogEntity toEntity(Blog blog, UserEntity userEntity) {
        if (blog == null) return null;
        return new BlogEntity(
                blog.blogId() == null ? null : blog.blogId().value(),
                userEntity,
                blog.blogUUID().value(),
                blog.title().value(),
                blog.description().value(),
                blog.createdAt(),
                blog.updatedAt()
        );
    }
}
