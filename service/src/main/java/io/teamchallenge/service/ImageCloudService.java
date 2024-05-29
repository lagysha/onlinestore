package io.teamchallenge.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageCloudService {

    String uploadFile(MultipartFile file, String folderName);
}
