package com.project.numble.application.image.dto.request;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddImageRequest {

    @NotEmpty(message = "{images.notBlank}")
    private List<MultipartFile> images;

}
