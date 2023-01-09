package com.project.numble.application.image.controller;

import com.project.numble.application.image.dto.request.AddImageRequest;
import com.project.numble.application.image.dto.request.DelImageRequest;
import com.project.numble.application.image.dto.response.AddImagesResponse;
import com.project.numble.application.image.service.ImageService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {

    private final ImageService imageService;

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<AddImagesResponse> uploadImages(@Valid @ModelAttribute("images") AddImageRequest request) {
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
