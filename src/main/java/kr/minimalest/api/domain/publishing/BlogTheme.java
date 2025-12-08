package kr.minimalest.api.domain.publishing;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

//@Entity
//@Table(name = "blog_theme")
public class BlogTheme {

    @EmbeddedId
    private BlogThemeId blogThemeId;
}
