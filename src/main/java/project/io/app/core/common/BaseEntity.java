package project.io.app.core.common;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public abstract class BaseEntity {

    protected LocalDateTime createdAt;
    protected LocalDateTime lastModifiedAt;
    protected boolean deleted;

    protected void init(
            final LocalDateTime createdAt,
            final LocalDateTime lastModifiedAt,
            final boolean deleted
    ) {
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        this.lastModifiedAt = lastModifiedAt == null ? LocalDateTime.now() : lastModifiedAt;
        this.deleted = deleted;
    }
}
