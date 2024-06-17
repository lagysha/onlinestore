package io.teamchallenge.validator;

import io.teamchallenge.annotation.ImageValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.springframework.web.multipart.MultipartFile;

public class ImageValidator implements ConstraintValidator<ImageValidation, List<MultipartFile>> {
    private final List<String> validType = Arrays.asList("image/jpeg", "image/png", "image/jpg");

    @Override
    public void initialize(ImageValidation constraintAnnotation) {
        // Initializes the validator in preparation for #isValid calls
    }

    @Override
    public boolean isValid(List<MultipartFile> images, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(images) || images.isEmpty()) {
            return true;
        } else {
            return (images.stream().allMatch(image -> validType.contains(image.getContentType())) &&
                images.size()<6);
        }
    }
}
