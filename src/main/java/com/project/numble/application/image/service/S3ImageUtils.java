package com.project.numble.application.image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.project.numble.application.image.domain.Image;
import com.project.numble.application.image.service.exception.CannotUploadImageException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
public class S3ImageUtils implements ImageUtils {

    private static String AWS_CLOUD_FRONT = "https://d1rxx32hh0c77e.cloudfront.net";

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public void upload(MultipartFile file, String uniqueName) {
        try {
            amazonS3Client.putObject(bucket, uniqueName, file.getInputStream(), generateObjectMetaData(file));
        } catch (IOException e) {
            throw new CannotUploadImageException(e);
        }
    }

    @Override
    public String getImageFilePath(Image image) {
        return AWS_CLOUD_FRONT + "/" + image.getUniqueName();
    }

    private ObjectMetadata generateObjectMetaData(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());
        return objectMetadata;
    }
}
