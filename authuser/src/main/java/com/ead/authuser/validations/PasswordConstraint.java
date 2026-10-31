package com.ead.authuser.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PasswordConstraintImpl.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordConstraint {

    String message() default """
            Invalid password! Password must contain at least one digit [0-9],
            at least one lowercase Latin character [a-z], at least one uppercase
            Latin character [A-Z], at least one special character like !@#&()-:;',?/*~$^+=<>,
            a length of least 6 characters and a maximum of 20 characters""";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
