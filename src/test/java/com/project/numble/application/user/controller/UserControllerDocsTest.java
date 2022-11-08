package com.project.numble.application.user.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.numble.application.common.advice.CommonControllerAdvice;
import com.project.numble.application.common.advice.ControllerAdviceUtils;
import com.project.numble.application.user.controller.advice.UserControllerAdvice;
import com.project.numble.application.user.dto.response.FindAddressByClientIpResponse;
import com.project.numble.application.user.dto.response.FindAddressByQueryResponse;
import com.project.numble.application.user.service.StandardUserService;
import com.project.numble.application.user.service.util.UrlConnectionIOException;
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
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@DisplayName("UserController RestDocs 테스트")
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserControllerDocsTest {

    @Mock
    StandardUserService userService;

    @InjectMocks
    UserController userController;

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
            .standaloneSetup(userController)
            .setControllerAdvice(
                new UserControllerAdvice(utils),
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
    void searchAddressByIp_성공_문서화_테스트() throws Exception {
        // given
        given(userService.searchAddressByIp())
                .willReturn(
                    new FindAddressByClientIpResponse(
                            "서울특별시 강서구 마곡동",
                            "서울특별시",
                            "강서구",
                            "마곡동")
                );

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/users/address/search/ip"));

        // then
        result
            .andExpect(status().isOk())
            .andDo(
                document("search-address-by-ip",
                    responseFields(
                        fieldWithPath("addressName").type(JsonFieldType.STRING).description("전체 지역 명칭"),
                        fieldWithPath("regionDepth1").type(JsonFieldType.STRING).description("지역 1Depth, 시도 단위"),
                        fieldWithPath("regionDepth2").type(JsonFieldType.STRING).description("지역 2Depth, 구 단위"),
                        fieldWithPath("regionDepth3").type(JsonFieldType.STRING).description("지역 3Depth, 동 단위"))
                )
            );
    }

    @Test
    void searchAddressByIp_실패_문서화_테스트() throws Exception {
        // given
        willThrow(UrlConnectionIOException.class).given(userService).searchAddressByIp();

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/users/address/search/ip"));

        // then
        result
            .andExpect(status().isInternalServerError())
            .andDo(
                document("search-address-connection-error")
            );
    }

    @Test
    void searchAddressByQuery_성공_문서화_테스트() throws Exception {
        // given
        given(userService.searchAddressByQuery(anyString()))
            .willReturn(
                new FindAddressByQueryResponse("경기", "시흥")
            );

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.get("/users/address/search/query")
                .queryParam("query", "시흥"));

        // then
        result
            .andExpect(status().isOk())
            .andDo(
                document("search-address-by-query",
                    requestParameters(
                        parameterWithName("query").description("검색하고자 하는 주소 이름(2글자 이상)")
                    ),
                    responseFields(
                        fieldWithPath("regionDepth1").type(JsonFieldType.STRING).description("지역 1Depth, 시도 단위").optional(),
                        fieldWithPath("regionDepth2").type(JsonFieldType.STRING).description("지역 2Depth, 구 단위").optional())
                )
            );
    }
}