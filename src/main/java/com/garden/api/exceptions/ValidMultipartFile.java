package com.garden.api.exceptions;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MultipartFileValidator.class)
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMultipartFile {
    String message() default "Invalid file. File must not be empty and must be an image.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
