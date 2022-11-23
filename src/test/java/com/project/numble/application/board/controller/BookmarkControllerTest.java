package com.project.numble.application.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.numble.application.board.controller.advice.BookmarkControllerAdvice;
import com.project.numble.application.board.service.StandardBookmarkService;
import com.project.numble.application.common.advice.CommonControllerAdvice;
import com.project.numble.application.common.advice.ControllerAdviceUtils;
import com.project.numble.core.resolver.SignInUserArgumentResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("BookmarkController RestDocs 테스트")
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BookmarkControllerTest {

    @Mock
    StandardBookmarkService bookmarkService;

    @Mock
    SignInUserArgumentResolver signInUserArgumentResolver;

    @InjectMocks
    BookmarkController bookmarkController;

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
                .standaloneSetup(bookmarkController)
                .setControllerAdvice(
                        new BookmarkControllerAdvice(utils),
                        new CommonControllerAdvice(utils)
                )
                .setValidator(validator)
                .setCustomArgumentResolvers(signInUserArgumentResolver)
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
    void addBookmark_성공_테스트() throws Exception{

        // given
        willDoNothing().given(bookmarkService).addBookmark(any(), any());

        // when
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.post("/bookmark/{boardId}", 1));

        // then
        result
                .andExpect(status().isCreated()).andDo(
                        document("add-bookmark"
                                , pathParameters(parameterWithName("boardId").description("게시글 ID"))
                        )
                );

    }

    @Test
    void addBookmark_board_실패_테스트() throws Exception{

        // given
        willDoNothing().given(bookmarkService).addBookmark(any(), any());

        // when
        ResultActions result = mockMvc.perform( // Expected 400, Actual 201
                RestDocumentationRequestBuilders.post("/bookmark/{boardId}", "201"));
        // then
        result
                .andExpect(status().isBadRequest())
                .andDo(
                        document("add-bookmark-failed")
                );
    }

    @Test
    void cancelBookmark() throws Exception{
        willDoNothing().given(bookmarkService).cancelBookmark(any(), any());

        // when
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/bookmark/{boardId}", 1));

        // then
        result
                .andExpect(status().isOk()).andDo(
                        document("delete-bookmark"
                                , pathParameters(parameterWithName("boardId").description("게시글 ID"))
                        )
                );
    }
}
