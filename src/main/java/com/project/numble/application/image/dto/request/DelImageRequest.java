package com.project.numble.application.image.dto.request;

import javax.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DelImageRequest {

    @Positive(message = "{id.positive}")
    Long imageId;
}
