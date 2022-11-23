package com.project.numble.application.board.service;

import com.project.numble.application.board.domain.Board;
import com.project.numble.application.board.domain.BoardAnimal;
import com.project.numble.application.board.domain.Like;
import com.project.numble.application.board.dto.request.AddBoardRequest;
import com.project.numble.application.board.dto.request.ModBoardRequest;
import com.project.numble.application.board.dto.response.GetAllBoardResponse;
import com.project.numble.application.board.dto.response.GetBoardResponse;
import com.project.numble.application.board.repository.BoardAnimalRepository;
import com.project.numble.application.board.repository.BookmarkRepository;
import com.project.numble.application.board.repository.LikeRepository;
import com.project.numble.application.board.service.exception.BoardAnimalsNotExistsException;
import com.project.numble.application.board.service.exception.BoardNotExistsException;
import com.project.numble.application.board.repository.BoardRepository;
import com.project.numble.application.board.service.exception.CurrentUserNotSameWriter;
import com.project.numble.application.user.domain.User;
import com.project.numble.application.user.domain.enums.AnimalType;
import com.project.numble.application.user.repository.UserRepository;
import com.project.numble.application.user.repository.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StandardBoardService implements BoardService{

    private final BoardRepository boardRepository;

    private final UserRepository userRepository;

    private final LikeRepository likeRepository;

    private final BookmarkRepository bookmarkRepository;

    // 저장
    @Override
    public Long addBoard(AddBoardRequest request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        Board board = Board.builder().user(user)
                .content(request.getContent())
                .categoryType(request.getCategoryType())
                .boardAddress(user.getAddress().getRegionDepth1())
                .boardAnimals(new ArrayList<>())
                .build();

        if (request.getBoardAnimalTypes().size() == 0) {
            throw new BoardAnimalsNotExistsException();
        }

        request.getBoardAnimalTypes().stream().map(AnimalType::getAnimalType).forEach(boardAnimalType -> {
            BoardAnimal boardAnimal = new BoardAnimal(boardAnimalType);
            board.addBoardAnimal(boardAnimal);
        });

        user.addBoard(board);
        return boardRepository.save(board).getId();
    }


    // 단건 조회
    @Override
    public GetBoardResponse getBoard(Long userId, Long boardId) {
        Board board = getBoardOne(boardId);
        if (board.getUser().getId() != userId) {
            board.plusViewCount();
        }
        GetBoardResponse getBoardResponse = new GetBoardResponse(board);
        getBoardResponse.setLikeCheck(!likeRepository.findByUserIdAndBoardId(userId, boardId).isEmpty());
        getBoardResponse.setBookmarkCheck(!bookmarkRepository.findByUserIdAndBoardId(userId, boardId).isEmpty());
        return getBoardResponse;
    }


    // 자기가 작성한 글 조회
    @Override
    @Transactional(readOnly = true)
    public List<GetAllBoardResponse> getBoardUser(PageRequest pageRequest,Long userId) {
        List<GetAllBoardResponse> getAllBoardResponses = boardRepository.findUserBoardsOrderByIdDesc(pageRequest, userId).stream().map(GetAllBoardResponse::new).collect(Collectors.toList());
        for (GetAllBoardResponse getAllBoardResponse : getAllBoardResponses) {
            getAllBoardResponse.setLikeCheck(!likeRepository.findByUserIdAndBoardId(userId, getAllBoardResponse.getBoardId()).isEmpty());
            getAllBoardResponse.setBookmarkCheck(!bookmarkRepository.findByUserIdAndBoardId(userId, getAllBoardResponse.getBoardId()).isEmpty());
        }
        return getAllBoardResponses;
    }

    // bookmark 게시글 조회
    @Override
    public List<GetAllBoardResponse> getBookmarkBoard(PageRequest pageRequest, Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        List<GetAllBoardResponse> getAllBoardResponses = boardRepository.findBookmarkBoardOrderByIdDesc(pageRequest, userId).stream().map(GetAllBoardResponse::new).collect(Collectors.toList());
        for (GetAllBoardResponse getAllBoardResponse : getAllBoardResponses) {
            getAllBoardResponse.setBookmarkCheck(true);
        }
        return getAllBoardResponses;
    }

    // 전체 조회
    @Override
    @Transactional(readOnly = true)
    public List<GetAllBoardResponse> getBoardList(PageRequest pageRequest, Long userId, String address, String animalType, String categoryType) {
        List<GetAllBoardResponse> getAllBoardResponses = boardRepository.findBoardsOrderByIdDesc(pageRequest, address, categoryType).stream().map(GetAllBoardResponse::new).collect(Collectors.toList());
        if (!animalType.equals("전체")) {
            getAllBoardResponses = getAllBoardResponses.stream().filter(getAllBoardResponse -> getAllBoardResponse.getBoardAnimalTypes().contains(animalType)).collect(Collectors.toList());
        }
        for (GetAllBoardResponse getAllBoardResponse : getAllBoardResponses) {
            getAllBoardResponse.setLikeCheck(!likeRepository.findByUserIdAndBoardId(userId, getAllBoardResponse.getBoardId()).isEmpty());
            getAllBoardResponse.setBookmarkCheck(!bookmarkRepository.findByUserIdAndBoardId(userId, getAllBoardResponse.getBoardId()).isEmpty());
        }
        return getAllBoardResponses;
    }


    // 수정
    @Override
    public Long updateBoard(Long boardId, Long userId, ModBoardRequest request) {

        // 개시글 가져오기
        Board board = getBoardOne(boardId);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        // 게시글 사용자와 수정자 비교
        if (!board.getUser().getId().equals(userId)) {
            throw new CurrentUserNotSameWriter();
        }

        board.update(request,user.getAddress().getRegionDepth1());
        if (request.getBoardAnimalTypes().size() == 0) {
            throw new BoardAnimalsNotExistsException();
        }
        // 게시글 기존 동물 삭제
        board.getBoardAnimals().clear();
        // 게시글 동물 재 등록
        request.getBoardAnimalTypes().stream().map(AnimalType::getAnimalType).forEach(boardAnimalType -> {
            BoardAnimal boardAnimal = new BoardAnimal(boardAnimalType);
            board.addBoardAnimal(boardAnimal);
        });

        return boardId;
    }

    // 삭제
    @Override
    public void delete(Long id) {

        Board delBoard = boardRepository.findById(id).orElseThrow(() -> new BoardNotExistsException());

        userRepository.findById(delBoard.getUser().getId()).orElseThrow(() -> new UserNotFoundException())
                        .delBoard(delBoard);

        boardRepository.deleteById(id);
    }


    // Board 가져오기
    private Board getBoardOne(Long id) {
        return boardRepository.findById(id).orElseThrow(() -> new BoardNotExistsException());
    }
}
