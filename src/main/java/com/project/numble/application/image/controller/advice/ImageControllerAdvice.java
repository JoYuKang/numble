package com.project.numble.application.image.controller.advice;

import com.project.numble.application.common.advice.ControllerAdviceUtils;
import com.project.numble.application.common.advice.ExceptionType;
import com.project.numble.application.image.controller.ImageController;
import com.project.numble.application.image.service.exception.ImageNotFoundException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice(assignableTypes = {ImageController.class})
public class ImageControllerAdvice {

    private final ControllerAdviceUtils utils;

    @ExceptionHandler(ImageNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> imageNotFoundExceptionHandler() {
        return utils.getFailureResponse(ExceptionType.IMAGE_NOT_FOUND_EXCEPTION);
    }
}
