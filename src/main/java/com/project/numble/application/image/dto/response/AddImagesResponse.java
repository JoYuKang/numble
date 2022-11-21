package com.project.numble.application.image.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddImagesResponse {

    private List<AddImageResponse> createImages;
}
