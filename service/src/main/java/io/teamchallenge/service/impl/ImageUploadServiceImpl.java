package io.teamchallenge.service.impl;

import com.cloudinary.Cloudinary;
import io.teamchallenge.exception.ImagePersitenceException;
import io.teamchallenge.service.ImageCloudService;
import java.io.IOException;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static io.teamchallenge.constant.ExceptionMessage.IMAGE_EXCEPTION_MESSAGE;

@Service
@RequiredArgsConstructor
public class ImageUploadServiceImpl implements ImageCloudService {
    private final Cloudinary cloudinary;

    public String uploadFile(MultipartFile file, String folderName) {
        try{
            HashMap<Object, Object> options = new HashMap<>();
            options.put("folder", folderName);
            var uploadedFile = cloudinary.uploader().upload(file.getBytes(), options);
            String publicId = (String) uploadedFile.get("public_id");
            return cloudinary.url().secure(true).generate(publicId);
        }catch (IOException e){
            throw new ImagePersitenceException(IMAGE_EXCEPTION_MESSAGE,e);
        }
    }
}
