package com.som.MeetingAssistant.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class ValidTimeRangeValidator implements ConstraintValidator<ValidTimeRange, Object> {
    private String startField;
    private String endField;

    @Override public void initialize(ValidTimeRange a) {
        startField = a.startField();
        endField = a.endField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Field s = value.getClass().getDeclaredField(startField);
            Field e = value.getClass().getDeclaredField(endField);
            s.setAccessible(true); e.setAccessible(true);
            Object sv = s.get(value); Object ev = e.get(value);
            if (sv == null || ev == null) return true;
            LocalDateTime start = (LocalDateTime) sv;
            LocalDateTime end = (LocalDateTime) ev;
            return end.isAfter(start);
        } catch (Exception ignored) {
            return true;
        }
    }
}

