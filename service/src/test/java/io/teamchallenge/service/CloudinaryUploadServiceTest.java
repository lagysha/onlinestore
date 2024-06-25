package io.teamchallenge.service;

import com.cloudinary.Api;
import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.cloudinary.Url;
import io.teamchallenge.exception.PersistenceException;
import io.teamchallenge.service.impl.CloudinaryUploadServiceImpl;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.teamchallenge.constant.ExceptionMessage.IMAGE_DELETION_EXCEPTION_MESSAGE;
import static io.teamchallenge.constant.ExceptionMessage.IMAGE_PERSISTENCE_EXCEPTION_MESSAGE;
import static io.teamchallenge.util.Utils.PRODUCT_IMAGES_FOLDER_NAME;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CloudinaryUploadServiceTest {
    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @Mock
    private Url url;

    @Mock
    private Api api;

    @InjectMocks
    private CloudinaryUploadServiceImpl cloudinaryUploadService;

    private final String urlLink = "https://cloudinary/productImages/1";


    @Test
    void uploadImage() throws IOException{
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.fdsf",
            "image/fdsf",
            new byte[0]
        );
        HashMap<String,String> map = new HashMap<>();
        map.put("public_id","1");

        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(eq(file.getBytes()),anyMap())).thenReturn(map);
        when(cloudinary.url()).thenReturn(url);
        when(url.secure(true)).thenReturn(url);
        when(url.generate("1")).thenReturn(urlLink);

        cloudinaryUploadService.uploadImage(file,PRODUCT_IMAGES_FOLDER_NAME);

        verify(cloudinary).uploader();
        verify(uploader).upload(eq(file.getBytes()),anyMap());
        verify(cloudinary).url();
        verify(url).secure(eq(true));
        verify(url).generate(eq("1"));
    }

    @Test
    void uploadImageThrowsImagePersistenceException() throws IOException{
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.fdsf",
            "image/fdsf",
            new byte[0]
        );
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(eq(file.getBytes()),anyMap())).thenThrow(new IOException());

        assertThrows(PersistenceException.class, () -> cloudinaryUploadService.uploadImage(file,PRODUCT_IMAGES_FOLDER_NAME),
            IMAGE_PERSISTENCE_EXCEPTION_MESSAGE);

        verify(cloudinary).uploader();
        verify(uploader).upload(eq(file.getBytes()),anyMap());
    }

    @Test
    void deleteImages() throws Exception{
        var urlLinks = List.of(urlLink);
        var publicIds = List.of(PRODUCT_IMAGES_FOLDER_NAME+"/"+"1");
        when(cloudinary.api()).thenReturn(api);
        when(api.deleteResources(eq(publicIds),anyMap())).thenReturn(null);

        cloudinaryUploadService.deleteImages(urlLinks,PRODUCT_IMAGES_FOLDER_NAME);

        verify(cloudinary).api();
        verify(api).deleteResources(eq(publicIds),anyMap());

    }

    @Test
    void deleteImagesIOException() throws Exception{
        var urlLinks = List.of(urlLink);
        var publicIds = List.of(PRODUCT_IMAGES_FOLDER_NAME+"/"+"1");
        when(cloudinary.api()).thenReturn(api);
        when(api.deleteResources(eq(publicIds),anyMap())).thenThrow(new IOException());

        assertThrows(PersistenceException.class, () -> cloudinaryUploadService.deleteImages(urlLinks,PRODUCT_IMAGES_FOLDER_NAME),
            IMAGE_DELETION_EXCEPTION_MESSAGE);

        verify(cloudinary).api();
        verify(api).deleteResources(eq(publicIds),anyMap());
    }
}
