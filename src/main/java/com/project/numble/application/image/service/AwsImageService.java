package com.project.numble.application.image.service;

import com.project.numble.application.image.domain.Image;
import com.project.numble.application.image.dto.request.AddImageRequest;
import com.project.numble.application.image.dto.request.DelImageRequest;
import com.project.numble.application.image.dto.response.AddImageResponse;
import com.project.numble.application.image.dto.response.AddImagesResponse;
import com.project.numble.application.image.dto.response.DelImageResponse;
import com.project.numble.application.image.dto.response.GetImagePathResponse;
import com.project.numble.application.image.repository.ImageRepository;
import com.project.numble.application.image.service.exception.ImageNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AwsImageService implements ImageService {

    private final ImageRepository imageRepository;
    private final ImageUtils imageUtils;

    @Override
    public GetImagePathResponse getImagePath(Long imageId) {
        String imageFilePath = imageUtils.getImageFilePath(
            imageRepository.findById(imageId).orElseThrow(ImageNotFoundException::new));

        return new GetImagePathResponse(imageFilePath);
    }

    @Override
    public AddImagesResponse saveImages(AddImageRequest request) {
        List<Long> imageIds = new ArrayList<>();

        List<AddImageResponse> images = new ArrayList<>();

        IntStream.range(0, request.getImages().size()).forEach(
            index -> {
                Image saveImage =
                    imageRepository.save(
                        new Image(request.getImages().get(index).getOriginalFilename()));
                imageIds.add(saveImage.getId());
                imageUtils.upload(request.getImages().get(index), saveImage.getUniqueName());

                images.add(new AddImageResponse(saveImage.getId(), imageUtils.getImageFilePath(saveImage)));
            }
        );

        return new AddImagesResponse(images);
    }

    @Override
    public DelImageResponse deleteImage(DelImageRequest request) {
        Image image = imageRepository.findById(request.getImageId()).orElseThrow(ImageNotFoundException::new);

        image.removeBoard();
        return new DelImageResponse(image.getId());
    }
}
