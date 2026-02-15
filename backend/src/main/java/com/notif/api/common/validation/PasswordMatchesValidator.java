package com.notif.api.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (obj instanceof PasswordMatchable) {
            boolean matches = ((PasswordMatchable) obj).getPassword().equals(((PasswordMatchable) obj).getMatchingPassword());

            if (!matches) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        context.getDefaultConstraintMessageTemplate()
                )
                        .addPropertyNode("matchingPassword")
                        .addConstraintViolation();
            }

            return matches;
        }

        return false;
    }
}