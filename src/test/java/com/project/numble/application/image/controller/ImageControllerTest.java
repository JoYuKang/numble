package com.project.numble.application.image.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.numble.application.common.advice.CommonControllerAdvice;
import com.project.numble.application.common.advice.ControllerAdviceUtils;
import com.project.numble.application.helper.factory.dto.ImageDtoFactory;
import com.project.numble.application.image.controller.advice.ImageControllerAdvice;
import com.project.numble.application.image.dto.request.AddImageRequest;
import com.project.numble.application.image.dto.request.DelImageRequest;
import com.project.numble.application.image.dto.response.AddImageResponse;
import com.project.numble.application.image.dto.response.AddImagesResponse;
import com.project.numble.application.image.dto.response.DelImageResponse;
import com.project.numble.application.image.dto.response.GetImagePathResponse;
import com.project.numble.application.image.service.ImageService;
import com.project.numble.application.image.service.exception.ImageNotFoundException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@DisplayName("ImageController RestDocs 테스트")
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ImageControllerTest {

    @Mock
    ImageService imageService;

    @InjectMocks
    ImageController imageController;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach(RestDocumentationContextProvider restDocumentation) {
        ResourceBundleMessageSource responseHandlerMessageSource = new ResourceBundleMessageSource();
        responseHandlerMessageSource.setBasenames("message/exception_message");
        responseHandlerMessageSource.setDefaultEncoding("UTF-8");

        ResourceBundleMessageSource validatorMessageSource = new ResourceBundleMessageSource();
        validatorMessageSource.setBasenames("message/validation_message");
        validatorMessageSource.setDefaultEncoding("UTF-8");

        ControllerAdviceUtils utils = new ControllerAdviceUtils(responseHandlerMessageSource);

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(validatorMessageSource);

        mockMvc = MockMvcBuilders
            .standaloneSetup(imageController)
            .setControllerAdvice(
                new ImageControllerAdvice(utils),
                new CommonControllerAdvice(utils)
            )
            .setValidator(validator)
            .apply(
                documentationConfiguration(restDocumentation)
                    .operationPreprocessors()
                    .withRequestDefaults(
                        removeHeaders(
                            "Content-Length", "Host"),
                        prettyPrint())
                    .withResponseDefaults(
                        removeHeaders(
                            "Transfer-Encoding", "Date", "Keep-Alive", "Connection", "Content-Type", "Content-Length"),
                        prettyPrint())
            )
            .alwaysDo(print())
            .build();
    }

    @Test
    void upload_성공_테스트() throws Exception {
        // given
        AddImageRequest request = ImageDtoFactory.createCreateImageRequest("test.jpg");
        AddImageResponse response = new AddImageResponse(1L, "https://image-path");
        MockMultipartFile image = new MockMultipartFile("images", "origin.jpg", "image/jpeg", new byte[]{1});

        given(imageService.saveImages(any())).willReturn(new AddImagesResponse(List.of(response)));

        // when
        ResultActions result = mockMvc.perform(
            multipart("/image/upload").file(image)
        );

        // then
        result
            .andExpect(status().isCreated())
            .andDo(
                document("image-upload",
                    responseFields(
                        fieldWithPath("createImages.[].id").type(JsonFieldType.NUMBER).description("이미지 ID"),
                        fieldWithPath("createImages.[].imageFilePath").type(JsonFieldType.STRING).description("이미지 경로")
                    )
                )
            );
    }

    @Test
    void getFilePath_성공_테스트() throws Exception {
        // given
        GetImagePathResponse response = new GetImagePathResponse("https://image-path");

        given(imageService.getImagePath(anyLong())).willReturn(response);

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.get("/image/{id}", 1));

        // then
        result
            .andExpect(status().isOk())
            .andDo(
                document("image-get-file-path",
                    pathParameters(parameterWithName("id").description("경로를 조회하고자 하는 이미지 ID")),
                    responseFields(
                        fieldWithPath("imagePath").type(JsonFieldType.STRING).description("이미지 경로")
                    )
                )
            );
    }

    @Test
    void getFilePath_실패_테스트() throws Exception {
        // given
        willThrow(ImageNotFoundException.class).given(imageService).getImagePath(anyLong());

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.get("/image/{id}", 1));

        // then
        result
            .andExpect(status().isBadRequest())
            .andDo(
                document("image-get-file-path-failed"));
    }


    @Test
    void deleteImages_성공_테스트() throws Exception {
        // given
        DelImageRequest request = ImageDtoFactory.createDeleteImageRequest(1L);
        DelImageResponse response = new DelImageResponse(1L);

        given(imageService.deleteImage(any())).willReturn(response);

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.delete("/image/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result
            .andExpect(status().isOk())
            .andDo(
                document("delete-image",
                    requestFields(
                        fieldWithPath("imageId").type(JsonFieldType.NUMBER).description("삭제하고자 하는 이미지 ID")),
                    responseFields(
                        fieldWithPath("deleteImageId").type(JsonFieldType.NUMBER).description("삭제한 이미지 ID")
                    )
                )
            );
    }
}