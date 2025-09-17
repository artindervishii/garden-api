package com.garden.api.exceptions;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class MultipartFileValidator implements ConstraintValidator<ValidMultipartFile, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File must not be null or empty.")
                    .addConstraintViolation();
            return false;
        }

        String contentType = file.getContentType();
        if (contentType == null ||
                (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid file type. Only JPEG and PNG images are allowed.")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
