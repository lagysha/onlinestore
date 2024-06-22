package io.teamchallenge.validator;

import io.teamchallenge.annotation.ImageValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.springframework.web.multipart.MultipartFile;

public class ImageValidator implements ConstraintValidator<ImageValidation, List<MultipartFile>> {
    private static final List<String> VALID_TYPES = Arrays.asList("image/jpeg", "image/png", "image/jpg");

    /**
     * Validates the list of image files.
     *
     * @param images                       The list of image files to validate.
     * @param constraintValidatorContext   Context in which the constraint is evaluated.
     * @return true if the list is valid; false otherwise.
     */
    @Override
    public boolean isValid(List<MultipartFile> images, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(images) || images.isEmpty()) {
            return true;
        } else {
            return (images.stream().allMatch(image -> VALID_TYPES.contains(image.getContentType())) &&
                images.size()<6);
        }
    }
}
