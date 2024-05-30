package io.teamchallenge.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageCloudService {

    String uploadImage(MultipartFile file, String folderName);
    String destroyImage(String url);
}
