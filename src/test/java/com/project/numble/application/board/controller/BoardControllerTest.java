package com.project.numble.application.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.numble.application.board.controller.advice.BoardControllerAdvice;
import com.project.numble.application.board.dto.request.AddBoardRequest;
import com.project.numble.application.board.dto.request.ModBoardRequest;
import com.project.numble.application.board.dto.response.GetAllBoardResponse;
import com.project.numble.application.board.dto.response.GetBoardResponse;
import com.project.numble.application.board.service.StandardBoardService;
import com.project.numble.application.comment.dto.response.RootCommentResponse;
import com.project.numble.application.comment.dto.response.RootsCommentsResponse;
import com.project.numble.application.common.advice.CommonControllerAdvice;
import com.project.numble.application.common.advice.ControllerAdviceUtils;
import com.project.numble.application.helper.factory.dto.*;
import com.project.numble.core.resolver.SignInUserArgumentResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("BoardController RestDocs 테스트")
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class BoardControllerTest {

    @Mock
    StandardBoardService boardService;

    @Mock
    SignInUserArgumentResolver signInUserArgumentResolver;

    @InjectMocks
    BoardController boardController;

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
                .standaloneSetup(boardController)
                .setControllerAdvice(
                        new BoardControllerAdvice(utils),
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
    void addBoard_성공_테스트() throws Exception {
        // given
        AddBoardRequest request = AddBoardRequestFactory.createAddBoardRequest("create board content", "반려고수", "마포구");
        BDDMockito.given(boardService.addBoard(any(), any())).willReturn(1L);

        // when
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.post("/board/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        // then
        result
                .andExpect(status().isCreated())
                .andDo(
                        document("add-board",
                                requestFields(
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용"),
                                        fieldWithPath("imageIds").type(JsonFieldType.ARRAY).description("이미지 ID"),
                                        fieldWithPath("boardAddress").type(JsonFieldType.STRING).description("게시글 작성 당시 주소"),
                                        fieldWithPath("categoryType").type(JsonFieldType.STRING).description("카테고리").attributes(key("constraint").value("1개 이상 존재하는 카테고리")),
                                        fieldWithPath("boardAnimalTypes").type(JsonFieldType.ARRAY).description("동물 종류").attributes(key("constraint").value("1개 이상 존재하는 동물 종류"))
                                )
                        )
                );
    }

    @Test
    void modBoard_성공_테스트() throws Exception {
        // given
        ModBoardRequest request = ModBoardRequestFactory.createModBoardRequest("modify board content", "자유", "남동구");
        BDDMockito.given(boardService.updateBoard(any(), any(), any())).willReturn(1L);

        // when
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.post("/board/modify/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("mod-board",
                                requestFields(
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용"),
                                        fieldWithPath("imageIds").type(JsonFieldType.ARRAY).description("이미지 ID"),
                                        fieldWithPath("boardAddress").type(JsonFieldType.STRING).description("게시글 작성 당시 주소"),
                                        fieldWithPath("categoryType").type(JsonFieldType.STRING).description("카테고리").attributes(key("constraint").value("1개 이상 존재하는 카테고리")),
                                        fieldWithPath("boardAnimalTypes").type(JsonFieldType.ARRAY).description("동물 종류").attributes(key("constraint").value("1개 이상 존재하는 동물 종류"))
                                )
                        )
                );
    }
    @Test
    void deleteBoard_성공_테스트() throws Exception {
        // given
        willDoNothing().given(boardService).delete(any());

        // when
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/board/{id}", 1));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("delete-board",
                                pathParameters(
                                        parameterWithName("id").description("게시글 ID").attributes(key("constraint").value("삭제하고자 하는 게시글 ID")
                                        )
                                )
                        ));
    }

    @Test
    void getBoard_성공_테스트() throws Exception {
        // given
        GetBoardResponse response = GetBoardResponseFactory.createGetBoardResponse();
        given(boardService.getBoard(any(), any())).willReturn(response);

        // when
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/board/{id}", 1));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("get-board",
                                pathParameters(parameterWithName("id").description("게시글 ID")),
                                responseFields(
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용").optional(),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("게시글 작성자").optional(),
                                        fieldWithPath("imageIds").type(JsonFieldType.ARRAY).description("게시글 이미지").optional(),
                                        fieldWithPath("boardAddress").type(JsonFieldType.STRING).description("게시글 작성자 주소").optional(),
                                        fieldWithPath("categoryType").type(JsonFieldType.STRING).description("카테고리").optional(),
                                        fieldWithPath("boardAnimalTypes").type(JsonFieldType.ARRAY).description("동물 종류").optional(),
                                        fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("좋아요 개수").optional(),
                                        fieldWithPath("likeCheck").type(JsonFieldType.BOOLEAN).description("좋아요 유무").optional(),
                                        fieldWithPath("viewCount").type(JsonFieldType.NUMBER).description("조회수").optional(),
                                        fieldWithPath("bookmarkCount").type(JsonFieldType.NUMBER).description("북마크 개수").optional(),
                                        fieldWithPath("bookmarkCheck").type(JsonFieldType.BOOLEAN).description("북마크 유무").optional(),
                                        fieldWithPath("createdDate").type(JsonFieldType.STRING).description("첫 작성 시간").optional(),
                                        fieldWithPath("lastModifiedDate").type(JsonFieldType.STRING).description("수정 시간").optional()
                                )
                        )
                );
    }

    @Test
    void getBoardList_성공_테스트() throws Exception {
        // given
        GetAllBoardResponse response = GetAllBoardResponseFactory.createGetAllBoardResponse();
        given(boardService.getBoardList(any(), any(), any(), any(), any())).willReturn(List.of(response));

        // when
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/board/list?boardAddress=강남구&animalTypes=강아지&categoryType=반려고수"));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("get-board-list",
                                requestParameters(parameterWithName("boardAddress").description("주소"),
                                        parameterWithName("animalTypes").description("동물 종류"),
                                        parameterWithName("categoryType").description("카테고리")),
                                responseFields(
                                        fieldWithPath("[].boardId").type(JsonFieldType.NUMBER).description("게시글 ID").optional(),
                                        fieldWithPath("[].content").type(JsonFieldType.STRING).description("게시글 내용").optional(),
                                        fieldWithPath("[].nickname").type(JsonFieldType.STRING).description("게시글 작성자").optional(),
                                        fieldWithPath("[].imageIds").type(JsonFieldType.ARRAY).description("게시글 이미지").optional(),
                                        fieldWithPath("[].boardAddress").type(JsonFieldType.STRING).description("게시글 작성자 주소").optional(),
                                        fieldWithPath("[].categoryType").type(JsonFieldType.STRING).description("카테고리").optional(),
                                        fieldWithPath("[].boardAnimalTypes").type(JsonFieldType.ARRAY).description("동물 종류").optional(),
                                        fieldWithPath("[].likeCount").type(JsonFieldType.NUMBER).description("좋아요 개수").optional(),
                                        fieldWithPath("[].likeCheck").type(JsonFieldType.BOOLEAN).description("좋아요 유무").optional(),
                                        fieldWithPath("[].viewCount").type(JsonFieldType.NUMBER).description("조회수").optional(),
                                        fieldWithPath("[].bookmarkCount").type(JsonFieldType.NUMBER).description("북마크 개수").optional(),
                                        fieldWithPath("[].bookmarkCheck").type(JsonFieldType.BOOLEAN).description("북마크 유무").optional(),
                                        fieldWithPath("[].createdDate").type(JsonFieldType.STRING).description("첫 작성 시간").optional(),
                                        fieldWithPath("[].lastModifiedDate").type(JsonFieldType.STRING).description("수정 시간").optional()
                                )
                        )
                );
    }

    @Test
    void getBoardUser_성공_테스트() throws Exception {
        // given
        GetAllBoardResponse response = GetAllBoardResponseFactory.createGetAllBoardResponse();
        given(boardService.getBoardUser(any(), any())).willReturn(List.of(response));

        // when
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/board/user"));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("get-user-boards",
                                responseFields(
                                        fieldWithPath("[].boardId").type(JsonFieldType.NUMBER).description("게시글 ID").optional(),
                                        fieldWithPath("[].content").type(JsonFieldType.STRING).description("게시글 내용").optional(),
                                        fieldWithPath("[].nickname").type(JsonFieldType.STRING).description("게시글 작성자").optional(),
                                        fieldWithPath("[].imageIds").type(JsonFieldType.ARRAY).description("게시글 이미지").optional(),
                                        fieldWithPath("[].boardAddress").type(JsonFieldType.STRING).description("게시글 작성자 주소").optional(),
                                        fieldWithPath("[].categoryType").type(JsonFieldType.STRING).description("카테고리").optional(),
                                        fieldWithPath("[].boardAnimalTypes").type(JsonFieldType.ARRAY).description("동물 종류").optional(),
                                        fieldWithPath("[].likeCount").type(JsonFieldType.NUMBER).description("좋아요 개수").optional(),
                                        fieldWithPath("[].likeCheck").type(JsonFieldType.BOOLEAN).description("좋아요 유무").optional(),
                                        fieldWithPath("[].viewCount").type(JsonFieldType.NUMBER).description("조회수").optional(),
                                        fieldWithPath("[].bookmarkCount").type(JsonFieldType.NUMBER).description("북마크 개수").optional(),
                                        fieldWithPath("[].bookmarkCheck").type(JsonFieldType.BOOLEAN).description("북마크 유무").optional(),
                                        fieldWithPath("[].createdDate").type(JsonFieldType.STRING).description("첫 작성 시간").optional(),
                                        fieldWithPath("[].lastModifiedDate").type(JsonFieldType.STRING).description("수정 시간").optional()
                                )
                        )
                );
    }

    @Test
    void getBoardBookmark_성공_테스트() throws Exception {
        // given
        GetAllBoardResponse response = GetAllBoardResponseFactory.createGetAllBoardResponse();
        given(boardService.getBookmarkBoard(any(), any())).willReturn(List.of(response));

        // when
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/board/bookmark"));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("get-bookmark-boards",
                                responseFields(
                                        fieldWithPath("[].boardId").type(JsonFieldType.NUMBER).description("게시글 ID").optional(),
                                        fieldWithPath("[].content").type(JsonFieldType.STRING).description("게시글 내용").optional(),
                                        fieldWithPath("[].nickname").type(JsonFieldType.STRING).description("게시글 작성자").optional(),
                                        fieldWithPath("[].imageIds").type(JsonFieldType.ARRAY).description("게시글 이미지").optional(),
                                        fieldWithPath("[].boardAddress").type(JsonFieldType.STRING).description("게시글 작성자 주소").optional(),
                                        fieldWithPath("[].categoryType").type(JsonFieldType.STRING).description("카테고리").optional(),
                                        fieldWithPath("[].boardAnimalTypes").type(JsonFieldType.ARRAY).description("동물 종류").optional(),
                                        fieldWithPath("[].likeCount").type(JsonFieldType.NUMBER).description("좋아요 개수").optional(),
                                        fieldWithPath("[].likeCheck").type(JsonFieldType.BOOLEAN).description("좋아요 유무").optional(),
                                        fieldWithPath("[].viewCount").type(JsonFieldType.NUMBER).description("조회수").optional(),
                                        fieldWithPath("[].bookmarkCount").type(JsonFieldType.NUMBER).description("북마크 개수").optional(),
                                        fieldWithPath("[].bookmarkCheck").type(JsonFieldType.BOOLEAN).description("북마크 유무").optional(),
                                        fieldWithPath("[].createdDate").type(JsonFieldType.STRING).description("첫 작성 시간").optional(),
                                        fieldWithPath("[].lastModifiedDate").type(JsonFieldType.STRING).description("수정 시간").optional()
                                )
                        )
                );
    }
}
