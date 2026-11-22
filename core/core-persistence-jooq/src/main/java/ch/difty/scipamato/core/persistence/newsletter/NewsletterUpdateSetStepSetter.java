package ch.difty.scipamato.core.persistence.newsletter;

import static ch.difty.scipamato.core.db.tables.Newsletter.NEWSLETTER;

import org.jetbrains.annotations.NotNull;
import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.core.db.tables.records.NewsletterRecord;
import ch.difty.scipamato.core.entity.newsletter.Newsletter;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;
import ch.difty.scipamato.core.persistence.UpdateSetStepSetter;

/**
 * The update step setter used for updating {@link Newsletter}s.
 * <br/><br/>
 * <b>Note:</b> the {@link NewsletterTopic}s are not updated here.
 *
 * @author u.joss
 */
@Component
public class NewsletterUpdateSetStepSetter implements UpdateSetStepSetter<NewsletterRecord, Newsletter> {

    @NotNull
    @Override
    public UpdateSetMoreStep<NewsletterRecord> setFieldsFor(@NotNull final UpdateSetFirstStep<NewsletterRecord> step, @NotNull final Newsletter e) {
        return step
            .set(NEWSLETTER.ISSUE, e.getIssue())
            .set(NEWSLETTER.ISSUE_DATE, e.getIssueDate())
            .set(NEWSLETTER.PUBLICATION_STATUS, e
                .getPublicationStatus()
                .getId())
            .set(NEWSLETTER.CREATED, e.getCreated())
            .set(NEWSLETTER.CREATED_BY, e.getCreatedBy())
            .set(NEWSLETTER.LAST_MODIFIED, e.getLastModified())
            .set(NEWSLETTER.LAST_MODIFIED_BY, e.getLastModifiedBy())
            .set(NEWSLETTER.VERSION, e.getVersion() + 1);
    }
}
