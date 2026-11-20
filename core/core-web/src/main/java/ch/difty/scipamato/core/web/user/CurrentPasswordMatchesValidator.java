package ch.difty.scipamato.core.web.user;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Validator to verify if the text field contains a password that matches
 * the password hash passed into the constructor.
 * <p>
 * The {@link PasswordEncoder} is injected as serializable proxy, as the validator
 * is serialized together with the page.
 */
class CurrentPasswordMatchesValidator implements IValidator<String> {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    @SpringBean
    private PasswordEncoder passwordEncoder;

    private final String currentPasswordHashPersisted;

    CurrentPasswordMatchesValidator(@NotNull final String currentPasswordHashPersisted) {
        Injector
            .get()
            .inject(this);
        this.currentPasswordHashPersisted = currentPasswordHashPersisted;
    }

    @Override
    public void validate(@NotNull final IValidatable<String> validatable) {
        final String pwCandidate = validatable.getValue();
        if (!passwordEncoder.matches(pwCandidate, currentPasswordHashPersisted)) {
            final ValidationError error = new ValidationError();
            error.addKey(getClass().getSimpleName() + "." + "not-matching");
            validatable.error(error);
        }
    }
}
