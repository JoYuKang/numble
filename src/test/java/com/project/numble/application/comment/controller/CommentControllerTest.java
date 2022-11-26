package com.project.numble.application.comment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.numble.application.auth.controller.advice.AuthControllerAdvice;
import com.project.numble.application.board.service.exception.BoardNotExistsException;
import com.project.numble.application.comment.controller.advice.CommentControllerAdvice;
import com.project.numble.application.comment.dto.request.AddCommentRequest;
import com.project.numble.application.comment.dto.request.ModCommentRequest;
import com.project.numble.application.comment.dto.request.ReplyCommentRequest;
import com.project.numble.application.comment.dto.response.GetCommentResponse;
import com.project.numble.application.comment.dto.response.GetCommentsResponse;
import com.project.numble.application.comment.dto.response.RootCommentResponse;
import com.project.numble.application.comment.dto.response.RootsCommentsResponse;
import com.project.numble.application.comment.service.StandardCommentService;
import com.project.numble.application.comment.service.exception.CommentNotFoundException;
import com.project.numble.application.common.advice.CommonControllerAdvice;
import com.project.numble.application.common.advice.ControllerAdviceUtils;
import com.project.numble.application.helper.factory.dto.AddCommentRequestFactory;
import com.project.numble.application.helper.factory.dto.GetCommentResponseFactory;
import com.project.numble.application.helper.factory.dto.ModCommentRequestFactory;
import com.project.numble.application.helper.factory.dto.ReplyCommentRequestFactory;
import com.project.numble.application.helper.factory.dto.RootCommentResponseFactory;
import com.project.numble.core.resolver.SignInUserArgumentResolver;
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
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@DisplayName("CommentController RestDocs 테스트")
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CommentControllerTest {

    @Mock
    StandardCommentService commentService;

    @Mock
    SignInUserArgumentResolver signInUserArgumentResolver;

    @InjectMocks
    CommentController commentController;

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
            .standaloneSetup(commentController)
            .setControllerAdvice(
                new CommentControllerAdvice(utils),
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
    void addComment_성공_테스트() throws Exception {
        // given
        AddCommentRequest request = AddCommentRequestFactory.createAddCommentRequest(1L, "content");

        willDoNothing().given(commentService).addComment(any(), any());

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/comments/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result
            .andExpect(status().isCreated())
            .andDo(
                document("add-comments",
                    requestFields(
                        fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시글 ID").attributes(key("constraint").value("1 이상의 존재하는 게시글 ID")),
                        fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용")
                    )
                )
            );
    }

    @Test
    void addComment_id_실패_테스트() throws Exception {
        // given
        AddCommentRequest request = AddCommentRequestFactory.createAddCommentRequest(null, "content");

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/comments/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result
            .andExpect(status().isBadRequest())
            .andDo(
                document("add-comment-id-failed"));
    }

    @Test
    void addComment_content_실패_테스트() throws Exception {
        // given
        AddCommentRequest request = AddCommentRequestFactory.createAddCommentRequest(1L, null);

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/comments/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result
            .andExpect(status().isBadRequest())
            .andDo(
                document("add-comment-content-failed"));
    }

    @Test
    void replyComment_성공_테스트() throws Exception {
        // given
        ReplyCommentRequest request = ReplyCommentRequestFactory.createReplyCommentRequest(1L, 1L, "replyContent");

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/comments/reply")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result
            .andExpect(status().isCreated())
            .andDo(
                document("reply-comments",
                    requestFields(
                        fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시글 ID").attributes(key("constraint").value("1 이상의 존재하는 게시글 ID")),
                        fieldWithPath("commentId").type(JsonFieldType.NUMBER).description("댓글 ID").attributes(key("constraint").value("대댓글을 추가하고자 하는 댓글 ID")),
                        fieldWithPath("content").type(JsonFieldType.STRING).description("대댓글 내용")
                    )
                )
            );
    }

    @Test
    void replyComment_board_id_실패_테스트() throws Exception {
        // given
        ReplyCommentRequest request = ReplyCommentRequestFactory.createReplyCommentRequest(null, 1L, "replyContent");

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/comments/reply")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result
            .andExpect(status().isBadRequest())
            .andDo(
                document("reply-comment-board-id-failed"));
    }

    @Test
    void replyComment_comment_id_실패_테스트() throws Exception {
        // given
        ReplyCommentRequest request = ReplyCommentRequestFactory.createReplyCommentRequest(1L, null, "replyContent");

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/comments/reply")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result
            .andExpect(status().isBadRequest())
            .andDo(
                document("reply-comment-comment-id-failed"));
    }

    @Test
    void replyComment_content_실패_테스트() throws Exception {
        // given
        ReplyCommentRequest request = ReplyCommentRequestFactory.createReplyCommentRequest(1L, 1L, null);

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/comments/reply")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result
            .andExpect(status().isBadRequest())
            .andDo(
                document("reply-comment-content-failed"));
    }

    @Test
    void modifyComment_성공_테스트() throws Exception {
        // given
        ModCommentRequest request = ModCommentRequestFactory.createModCommentRequest(1L, "modify content");

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/comments/modify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result
            .andExpect(status().isOk())
            .andDo(
                document("mod-comments",
                    requestFields(
                        fieldWithPath("commentId").type(JsonFieldType.NUMBER).description("댓글 ID").attributes(key("constraint").value("수정하고자 하는 댓글 ID")),
                        fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 수정 내용")
                    )
                )
            );
    }

    @Test
    void modComment_comment_id_실패_테스트() throws Exception {
        // given
        ModCommentRequest request = ModCommentRequestFactory.createModCommentRequest(null, "modify content");

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/comments/modify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result
            .andExpect(status().isBadRequest())
            .andDo(
                document("mod-comment-comment-id-failed"));
    }

    @Test
    void modComment_content_실패_테스트() throws Exception {
        // given
        ModCommentRequest request = ModCommentRequestFactory.createModCommentRequest(1L, null);

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/comments/modify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result
            .andExpect(status().isBadRequest())
            .andDo(
                document("mod-comment-content-failed"));
    }

    @Test
    void deleteComment_성공_테스트() throws Exception {
        // given
        willDoNothing().given(commentService).deleteComment(any(), any());

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.delete("/comments/delete/{id}", 1));

        // then
        result
            .andExpect(status().isOk())
            .andDo(
                document("delete-comments",
                    pathParameters(
                        parameterWithName("id").description("댓글 ID").attributes(key("constraint").value("삭제하고자 하는 댓글 ID")
                    )
                )
            ));
    }

    @Test
    void deleteComment_실패_테스트() throws Exception {
        // given
        willThrow(CommentNotFoundException.class).given(commentService).deleteComment(any(), any());

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.delete("/comments/delete/{id}", 1));

        // then
        result
            .andExpect(status().isBadRequest())
            .andDo(
                document("delete-comment-failed"));
    }

    @Test
    void getAllComments_성공_테스트() throws Exception {
        // given
        RootCommentResponse response = RootCommentResponseFactory.createRootCommentResponse();
        given(commentService.getAllComments(anyLong())).willReturn(List.of(response));

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.get("/comments/{boardId}", 1));

        // then
        result
            .andExpect(status().isOk())
            .andDo(
                document("get-all-comments",
                    pathParameters(parameterWithName("boardId").description("게시글 ID")),
                    responseFields(
                        fieldWithPath("[].commentId").type(JsonFieldType.NUMBER).description("댓글 ID").optional(),
                        fieldWithPath("[].author").type(JsonFieldType.STRING).description("댓글 작성자").optional(),
                        fieldWithPath("[].address").type(JsonFieldType.STRING).description("댓글 작성자 주소").optional(),
                        fieldWithPath("[].content").type(JsonFieldType.STRING).description("댓글 내용").optional(),
                        fieldWithPath("[].depth").type(JsonFieldType.NUMBER).description("댓글 깊이 (1)").optional(),
                        fieldWithPath("[].deleted").type(JsonFieldType.BOOLEAN).description("삭제 유무").optional(),
                        fieldWithPath("[].createdDate").type(JsonFieldType.STRING).description("댓글 등록일").optional(),
                        fieldWithPath("[].children").type(JsonFieldType.ARRAY).description("대댓글 목록").optional(),
                        fieldWithPath("[].children.[].commentId").type(JsonFieldType.NUMBER).description("대댓글 ID").optional(),
                        fieldWithPath("[].children.[].author").type(JsonFieldType.STRING).description("대댓글 작성자").optional(),
                        fieldWithPath("[].children.[].content").type(JsonFieldType.STRING).description("대댓글 내용").optional(),
                        fieldWithPath("[].children.[].depth").type(JsonFieldType.NUMBER).description("대댓글 깊이 (2)").optional(),
                        fieldWithPath("[].children.[].address").type(JsonFieldType.STRING).description("대댓글 작성자 주소").optional(),
                        fieldWithPath("[].children.[].deleted").type(JsonFieldType.BOOLEAN).description("대댓글 삭제 유무").optional(),
                        fieldWithPath("[].children.[].createdDate").type(JsonFieldType.STRING).description("대댓글 등록일").optional()
                    )
                )
            );
    }

    @Test
    void getAllComments_실패_테스트() throws Exception {
        // given
        willThrow(BoardNotExistsException.class).given(commentService).getAllComments(any());

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.get("/comments/{boardId}", 1));

        // then
        result
            .andExpect(status().isBadRequest())
            .andDo(
                document("get-all-comment-failed"));
    }

    @Test
    void getMyComments_성공_테스트() throws Exception {
        // given
        GetCommentResponse response = GetCommentResponseFactory.createGetCommentResponse();
        given(commentService.getMyComments(any())).willReturn(List.of(response));

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.get("/comments/my-comments"));

        // then
        result
            .andExpect(status().isOk())
            .andDo(
                document("get-my-comments",
                    responseFields(
                        fieldWithPath("[].commentId").type(JsonFieldType.NUMBER).description("댓글 ID").optional(),
                        fieldWithPath("[].author").type(JsonFieldType.STRING).description("댓글 작성자").optional(),
                        fieldWithPath("[].content").type(JsonFieldType.STRING).description("댓글 내용").optional(),
                        fieldWithPath("[].depth").type(JsonFieldType.NUMBER).description("댓글 깊이 (1)").optional(),
                        fieldWithPath("[].deleted").type(JsonFieldType.BOOLEAN).description("삭제 유무").optional(),
                        fieldWithPath("[].createdDate").type(JsonFieldType.STRING).description("댓글 등록일").optional()
                    )
                )
            );
    }
}