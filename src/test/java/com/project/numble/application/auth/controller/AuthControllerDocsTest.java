package com.project.numble.application.auth.controller;

import static com.project.numble.application.helper.factory.dto.SignUpFactory.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.numble.application.auth.controller.advice.AuthControllerAdvice;
import com.project.numble.application.auth.dto.request.SignInRequest;
import com.project.numble.application.auth.dto.request.SignUpRequest;
import com.project.numble.application.auth.service.AuthService;
import com.project.numble.application.auth.service.exception.SignInFailureException;
import com.project.numble.application.common.advice.CommonControllerAdvice;
import com.project.numble.application.common.advice.ControllerAdviceUtils;
import com.project.numble.application.helper.factory.dto.SignInFactory;
import com.project.numble.application.helper.factory.dto.SignUpFactory;
import com.project.numble.application.user.service.StandardUserService;
import com.project.numble.application.user.service.exception.UserEmailAlreadyExistsException;
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
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@DisplayName("AuthController RestDocs 테스트")
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AuthControllerDocsTest {

    private static final String EMAIL = "test@email.com";
    private static final String PASSWORD = "password";
    private static final String NICKNAME = "nickname";

    @Mock
    StandardUserService userService;

    @Mock
    AuthService authService;

    @InjectMocks
    AuthController authController;

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
            .standaloneSetup(authController)
            .setControllerAdvice(
                new AuthControllerAdvice(utils),
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
    void signUp_성공_문서화_테스트() throws Exception {
        // given
        SignUpRequest request = createSignUpRequest(EMAIL, PASSWORD, NICKNAME);

        willDoNothing().given(userService).signUp(any(SignUpRequest.class));

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result
            .andExpect(status().isCreated())
            .andDo(
                document("sign-up",
                    requestFields(fieldWithPath("email")
                            .type(JsonFieldType.STRING).description("이메일").attributes(key("constraint").value("unique")),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임").attributes(key("constraint").value("unique")))));
    }

    @Test
    void signUp_이메일_중복_실패_테스트() throws Exception {
        // given
        SignUpRequest request = createSignUpRequest(EMAIL, PASSWORD, NICKNAME);

        willThrow(UserEmailAlreadyExistsException.class).given(userService).signUp(any(SignUpRequest.class));

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result
            .andExpect(status().isBadRequest())
            .andDo(
                document("sign-up-failed-already-exists-email")
            );
    }

    @Test
    void signUp_이메일_형식_실패_테스트() throws Exception {
        // given
        SignUpRequest request = createSignUpRequest("test", PASSWORD, NICKNAME);

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result
            .andExpect(status().isBadRequest())
            .andDo(
                document("sign-up-failed-email-email")
            );
    }

    @Test
    void signUp_이메일_공백_실패_테스트() throws Exception {
        // given
        SignUpRequest request = createSignUpRequest("", PASSWORD, NICKNAME);

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result
            .andExpect(status().isBadRequest())
            .andDo(
                document("sign-up-failed-email-blank")
            );
    }

    @Test
    void signUp_비밀번호_공백_실패_테스트() throws Exception {
        // given
        SignUpRequest request = createSignUpRequest(EMAIL, "", NICKNAME);

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result
            .andExpect(status().isBadRequest())
            .andDo(
                document("sign-up-failed-password-blank")
            );
    }

    @Test
    void signUp_닉네임_공백_실패_테스트() throws Exception {
        // given
        SignUpRequest request = createSignUpRequest(EMAIL, PASSWORD, "");

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result
            .andExpect(status().isBadRequest())
            .andDo(
                document("sign-up-failed-nickname-blank")
            );
    }

    @Test
    void signUp_닉네임_중복_실패_테스트() throws Exception {
        // given
        SignUpRequest request = SignUpFactory.createSignUpRequest(EMAIL, PASSWORD, NICKNAME);

        willThrow(UserEmailAlreadyExistsException.class).given(userService).signUp(any(SignUpRequest.class));

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result
            .andExpect(status().isBadRequest())
            .andDo(
                document("sign-up-failed-already-exists-nickname")
            );
    }

    @Test
    void signIn_성공_문서화_테스트() throws Exception {
        // given
        SignInRequest request = SignInFactory.createSignUpRequest("test@email.com", "password");

        willDoNothing().given(authService).signIn(any(SignInRequest.class));

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result
            .andExpect(status().isOk())
            .andDo(
                document("sign-in",
                    requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"))
                ));
    }

    @Test
    void signIn_로그인_실패_테스트() throws Exception {
        // given
        SignInRequest request = SignInFactory.createSignUpRequest("test@email.com", "password");

        willThrow(SignInFailureException.class).given(authService).signIn(any(SignInRequest.class));

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result
            .andExpect(status().isBadRequest())
            .andDo(
                document("sign-in-failed"));
    }
}