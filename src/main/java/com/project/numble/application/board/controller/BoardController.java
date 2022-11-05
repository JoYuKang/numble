package com.project.numble.application.board.controller;


import com.project.numble.application.board.domain.Board;
import com.project.numble.application.board.dto.request.AddBoardRequest;
import com.project.numble.application.board.dto.request.ModBoardRequest;
import com.project.numble.application.board.dto.response.GetBoardResponse;
import com.project.numble.application.board.service.BoardServiceImp;
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

    private final BoardServiceImp boardServiceImp;


    // board 다건 조회
    @GetMapping({"", "/list"})
    public ResponseEntity<List<GetBoardResponse>> getBoards() {
        List<GetBoardResponse> boards = boardServiceImp.getBoardList();

        return new ResponseEntity<>(boards, HttpStatus.OK);
    }

    // board 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<GetBoardResponse> getBoard(@PathVariable Long id) {
        GetBoardResponse board = boardServiceImp.getBoard(id);

        return new ResponseEntity<>(board, HttpStatus.OK);
    }


    // board 추가
    @PostMapping("/add")
    public ResponseEntity<Long> addBoard(@Valid @RequestBody AddBoardRequest request) {
        Long saveId = boardServiceImp.save(request);
        return new ResponseEntity<>(saveId,HttpStatus.CREATED);
    }

    // board 수정
    @PostMapping("/modify/{id}")
    public ResponseEntity<Long> updateBoard(@Valid @RequestBody ModBoardRequest request, @PathVariable Long id) {
        Long updateId = boardServiceImp.updateBoard(id, request);
        return new ResponseEntity<>(updateId,HttpStatus.OK);
    }

    // board 삭제
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        boardServiceImp.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
