package com.som.MeetingAssistant.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidTimeRangeValidator.class)
public @interface ValidTimeRange {
    String message() default "end must be after start";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String startField() default "start";
    String endField() default "end";
}
