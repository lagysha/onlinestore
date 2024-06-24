package io.teamchallenge.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for managing image uploads and deletions in a cloud storage.
 * @author Niktia Malov
 */
public interface ImageCloudService {
    /**
     * Uploads an image to the specified folder in the cloud storage.
     *
     * @param file the image file to be uploaded
     * @param folderName the name of the folder where the image will be uploaded
     * @return the URL of the uploaded image
     */
    String uploadImage(MultipartFile file, String folderName);

    /**
     * Deletes images from the specified folder in the cloud storage based on their URLs.
     *
     * @param urls the list of URLs of the images to be deleted
     * @param folderName the name of the folder from which the images will be deleted
     */
    void deleteImages(List<String> urls, String folderName);
}
