package com.project.numble.application.board.controller;


import com.project.numble.application.board.dto.request.AddBoardRequest;
import com.project.numble.application.board.dto.request.ModBoardRequest;
import com.project.numble.application.board.dto.response.GetAllBoardResponse;
import com.project.numble.application.board.dto.response.GetBoardResponse;
import com.project.numble.application.board.service.BoardService;
import com.project.numble.application.image.service.ImageService;
import com.project.numble.core.resolver.SignInUser;
import com.project.numble.core.resolver.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;


    // board 다건 조회
    @GetMapping("/list")
    public ResponseEntity<List<GetAllBoardResponse>> getBoards(
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "category", required = false) String categoryType,
            @RequestParam(value = "animal", required = false) String animalType,
            @SignInUser UserInfo userInfo) {
        PageRequest pageRequest = PageRequest.of(0, 50);
        List<GetAllBoardResponse> boards = boardService.getBoardList(pageRequest, userInfo.getUserId(), address, animalType, categoryType);

        return new ResponseEntity<>(boards, HttpStatus.OK);
    }

    // board 단건 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<GetBoardResponse> getBoard(@SignInUser UserInfo userInfo, @PathVariable Long boardId) {
        GetBoardResponse board = boardService.getBoard(userInfo.getUserId(), boardId);

        return new ResponseEntity<>(board, HttpStatus.OK);
    }

    // user가 작성한 게시글 조회
    @GetMapping("/user")
    public ResponseEntity<List<GetAllBoardResponse>> getUserBoard(@SignInUser UserInfo userInfo) {
        PageRequest pageRequest = PageRequest.of(0, 5);
        List<GetAllBoardResponse> boards = boardService.getBoardUser(pageRequest, userInfo.getUserId());
        return new ResponseEntity<>(boards, HttpStatus.OK);
    }

    // user bookmark 조회
    @GetMapping("/bookmark")
    public ResponseEntity<List<GetAllBoardResponse>> getBookmarkBoard(@SignInUser UserInfo userInfo) {
        PageRequest pageRequest = PageRequest.of(0, 5);
        List<GetAllBoardResponse> bookmarkBoards = boardService.getBookmarkBoard(pageRequest, userInfo.getUserId());
        return new ResponseEntity<>(bookmarkBoards, HttpStatus.OK);
    }

    // board 추가
    @PostMapping("/add")
    public ResponseEntity<Long> addBoard(@SignInUser UserInfo userInfo, @Valid @RequestBody AddBoardRequest request) {
        Long saveId = boardService.addBoard(request, userInfo.getUserId());
        return new ResponseEntity<>(saveId, HttpStatus.CREATED);
    }

    // board 수정
    @PostMapping("/modify/{id}")
    public ResponseEntity<Long> updateBoard(@PathVariable Long id, @SignInUser UserInfo userInfo, @Valid @RequestBody ModBoardRequest request) {
        Long updateId = boardService.updateBoard(id, userInfo.getUserId(), request);
        return new ResponseEntity<>(updateId,HttpStatus.OK);
    }

    // board 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        boardService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
