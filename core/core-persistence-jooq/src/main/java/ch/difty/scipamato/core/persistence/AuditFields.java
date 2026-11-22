package ch.difty.scipamato.core.persistence;

import java.time.LocalDateTime;

import org.jetbrains.annotations.Nullable;

/**
 * Data transfer object to access the audit fields of the entity record class.
 */
public class AuditFields {
    final LocalDateTime created;
    final LocalDateTime lastModified;
    final Integer       createdBy;
    final Integer       lastModifiedBy;
    final int           version;

    public AuditFields(@Nullable final LocalDateTime c, @Nullable final Integer cb, @Nullable final LocalDateTime lm, @Nullable final Integer lmb,
        @Nullable final Integer v) {
        created = c;
        createdBy = cb;
        lastModified = lm;
        lastModifiedBy = lmb;
        version = v != null ? v : 1;
    }
}
