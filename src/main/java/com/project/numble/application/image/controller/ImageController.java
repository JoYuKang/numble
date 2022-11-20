package com.project.numble.application.image.controller;

import com.project.numble.application.image.dto.request.AddImageRequest;
import com.project.numble.application.image.dto.request.DelImageRequest;
import com.project.numble.application.image.dto.response.AddImagesResponse;
import com.project.numble.application.image.service.ImageService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<AddImagesResponse> uploadImages(@Valid @ModelAttribute AddImageRequest request) {
        return new ResponseEntity(imageService.saveImages(request), HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity getImagePath(@PathVariable Long id) {
        return new ResponseEntity(imageService.getImagePath(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity delImages(DelImageRequest request) {
        return new ResponseEntity(imageService.deleteImage(request), HttpStatus.OK);
    }
}
