package io.teamchallenge.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ImageCloudService {

    String uploadImage(MultipartFile file, String folderName);
    void deleteImages(List<String> urls, String folderName);
}
