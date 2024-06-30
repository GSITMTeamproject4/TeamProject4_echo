package com.project.echoproject.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true; // 빈 값은 유효하다고 간주
        }
        return value.matches("^010-[0-9]{4}-[0-9]{4}$");
    }
}