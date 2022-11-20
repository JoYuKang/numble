package com.project.numble.application.image.service;

import com.project.numble.application.image.domain.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageUtils {

    void upload(MultipartFile file, String uniqueName);

    String getImageFilePath(Image image);
}

