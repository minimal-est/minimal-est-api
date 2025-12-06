package kr.minimalest.api.web.controller.dto.response;

import kr.minimalest.api.domain.discovery.tag.Tag;
import kr.minimalest.api.domain.discovery.tag.TagId;

import java.util.UUID;

public record TagResponse(
        UUID id,
        String name
) {
    public static TagResponse of(Tag tag) {
        return new TagResponse(
                tag.getId().id(),
                tag.getName().value()
        );
    }

    public static TagResponse of(UUID id, String name) {
        return new TagResponse(id, name);
    }
}
