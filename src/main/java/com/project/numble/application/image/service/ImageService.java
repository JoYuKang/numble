package com.project.numble.application.image.service;

import com.project.numble.application.image.dto.request.AddImageRequest;
import com.project.numble.application.image.dto.request.DelImageRequest;
import com.project.numble.application.image.dto.response.AddImagesResponse;
import com.project.numble.application.image.dto.response.DelImageResponse;
import com.project.numble.application.image.dto.response.GetImagePathResponse;

public interface ImageService {

    GetImagePathResponse getImagePath(Long imageId);

    AddImagesResponse saveImages(AddImageRequest request);

    DelImageResponse deleteImage(DelImageRequest request);
}
