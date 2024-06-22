package io.teamchallenge.validator;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
public class ImageValidatorTest {

    @InjectMocks
    private ImageValidator imageValidator;

    @Test
    void isValidWithEmptyImagesReturnsTrue() {
        List<MultipartFile> images = new ArrayList<>();
        assertTrue(imageValidator.isValid(images, null));
    }

    @Test
    void isValidWithInvalidImagesReturnsFalse() {
        List<MultipartFile> images = new ArrayList<>();
        var multipartFile = new MockMultipartFile(
            "file",
            "test.fdsf",
            "image/fdsf",
            new byte[0]
        );
        images.add(multipartFile);
        assertFalse(imageValidator.isValid(images, null));
    }

    @Test
    void isValidWithJPEGImagesReturnsTrue() {
        List<MultipartFile> images = new ArrayList<>();
        var multipartFile = new MockMultipartFile(
            "file",
            "test.jpeg",
            "image/jpeg",
            new byte[0]
        );
        images.add(multipartFile);
        assertTrue(imageValidator.isValid(images, null));
    }

    @Test
    void isValidWithPNGImagesReturnsTrue() {
        List<MultipartFile> images = new ArrayList<>();
        var multipartFile = new MockMultipartFile(
            "file",
            "test.png",
            "image/png",
            new byte[0]
        );
        images.add(multipartFile);
        assertTrue(imageValidator.isValid(images, null));
    }

    @Test
    void isValidWithJPGImagesReturnsTrue() {
        List<MultipartFile> images = new ArrayList<>();
        var multipartFile = new MockMultipartFile(
            "file",
            "test.jpg",
            "image/jpg",
            new byte[0]
        );
        images.add(multipartFile);
        assertTrue(imageValidator.isValid(images, null));
    }
}
