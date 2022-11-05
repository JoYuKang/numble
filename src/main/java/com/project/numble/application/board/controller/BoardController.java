package com.project.numble.application.board.controller;


import com.project.numble.application.board.dto.request.AddBoardRequest;
import com.project.numble.application.board.dto.request.ModBoardRequest;
import com.project.numble.application.board.dto.response.GetBoardResponse;
import com.project.numble.application.board.service.StandardBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final StandardBoardService standardBoardService;


    // board 다건 조회
    @GetMapping({"", "/list"})
    public ResponseEntity<List<GetBoardResponse>> getBoards() {
        List<GetBoardResponse> boards = standardBoardService.getBoardList();

        return new ResponseEntity<>(boards, HttpStatus.OK);
    }

    // board 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<GetBoardResponse> getBoard(@PathVariable Long id) {
        GetBoardResponse board = standardBoardService.getBoard(id);

        return new ResponseEntity<>(board, HttpStatus.OK);
    }


    // board 추가
    @PostMapping("/add")
    public ResponseEntity<Long> addBoard(@Valid @RequestBody AddBoardRequest request) {
        Long saveId = standardBoardService.save(request);
        return new ResponseEntity<>(saveId,HttpStatus.CREATED);
    }

    // board 수정
    @PostMapping("/modify/{id}")
    public ResponseEntity<Long> updateBoard(@Valid @RequestBody ModBoardRequest request, @PathVariable Long id) {
        Long updateId = standardBoardService.updateBoard(id, request);
        return new ResponseEntity<>(updateId,HttpStatus.OK);
    }

    // board 삭제
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        standardBoardService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
