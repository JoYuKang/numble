package com.project.numble.application.image.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddImageResponse {

    private Long id;
    private String imageFilePath;
}
