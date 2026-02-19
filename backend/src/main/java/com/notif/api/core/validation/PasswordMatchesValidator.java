package com.notif.api.core.validation;

import com.notif.api.core.utils.Util;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (obj instanceof PasswordMatchable request) {
            String password = request.getPassword();
            String confirmPassword = request.getConfirmPassword();

            // Let @NotBlank handle the error if missing
            if (Util.isNullOrBlank(password) || Util.isNullOrBlank(confirmPassword)) {
                return true;
            }

            boolean matches = request.getPassword()
                    .equals(request.getConfirmPassword());

            if (!matches) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        context.getDefaultConstraintMessageTemplate()
                )
                        .addPropertyNode("confirmPassword")
                        .addConstraintViolation();
            }

            return matches;
        }

        return false;
    }
}