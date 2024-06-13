package io.teamchallenge.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.teamchallenge.exception.ImageDeletionException;
import io.teamchallenge.exception.ImagePersitenceException;
import io.teamchallenge.service.ImageCloudService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static io.teamchallenge.constant.ExceptionMessage.IMAGE_DELETION_EXCEPTION_MESSAGE;
import static io.teamchallenge.constant.ExceptionMessage.IMAGE_PERSISTENCE_EXCEPTION_MESSAGE;

@Service
@RequiredArgsConstructor
public class ImageUploadServiceImpl implements ImageCloudService {
    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file, String folderName) {
        try{
            HashMap<Object, Object> options = new HashMap<>();
            options.put("folder", folderName);
            var uploadedFile = cloudinary.uploader().upload(file.getBytes(), options);
            String publicId = (String) uploadedFile.get("public_id");
            return cloudinary.url().secure(true).generate(publicId);
        }catch (IOException e){
            throw new ImagePersitenceException(IMAGE_PERSISTENCE_EXCEPTION_MESSAGE,e);
        }
    }

    @Override
    public void deleteImages(List<String> urls) {
        try{
            List<String> publicIds = new ArrayList<>();
            urls.forEach(url -> {
                var splitUrl = url.split("/");
                String publicId = splitUrl[splitUrl.length - 1];
                publicIds.add(publicId);
                }
            );
            //TODO: verify why resources are not deleted
            cloudinary.api().deleteResources(publicIds,ObjectUtils.emptyMap());
        }catch (Exception e){
            throw new ImageDeletionException(IMAGE_DELETION_EXCEPTION_MESSAGE,e);
        }
    }

}
