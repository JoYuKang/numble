package com.project.numble.application.board.service;

import com.project.numble.application.board.domain.Board;
import com.project.numble.application.board.domain.BoardAnimal;
import com.project.numble.application.board.dto.request.AddBoardRequest;
import com.project.numble.application.board.dto.request.ModBoardRequest;
import com.project.numble.application.board.dto.response.GetAllBoardResponse;
import com.project.numble.application.board.dto.response.GetBoardResponse;
import com.project.numble.application.bookmark.repository.BookmarkRepository;
import com.project.numble.application.like.repository.LikeRepository;
import com.project.numble.application.board.service.exception.BoardAnimalsNotExistsException;
import com.project.numble.application.board.service.exception.BoardNotExistsException;
import com.project.numble.application.board.repository.BoardRepository;
import com.project.numble.application.board.service.exception.CurrentUserNotSameWriter;
import com.project.numble.application.image.domain.Image;
import com.project.numble.application.image.repository.ImageRepository;
import com.project.numble.application.image.service.exception.ImageNotFoundException;
import com.project.numble.application.user.domain.User;
import com.project.numble.application.user.domain.enums.AnimalType;
import com.project.numble.application.user.repository.UserRepository;
import com.project.numble.application.user.repository.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final ImageRepository imageRepository;

    // 저장
    @Override
    public Long addBoard(AddBoardRequest request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        Board board = Board.builder().user(user)
                .content(request.getContent())
                .categoryType(request.getCategoryType())
                .boardAddress(user.getAddress().getRegionDepth1())
                .build();

        if (request.getBoardAnimalTypes().size() == 0) {
            throw new BoardAnimalsNotExistsException();
        }

        if (request.getImageIds().size() != 0) {
            request.getImageIds().forEach(id -> {
                Image findImages = imageRepository.findById(id).orElseThrow(ImageNotFoundException::new);
                board.addImage(findImages);
            });
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


    // 작성한 글 조회
    @Override
    @Transactional(readOnly = true)
    public List<GetAllBoardResponse> getBoardUser(Long userId, Long lastBoardId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        if (lastBoardId == null) {
            lastBoardId = Long.MAX_VALUE;
        }
        List<GetAllBoardResponse> getAllBoardResponses = boardRepository.findUserBoardsOrderByIdDesc(userId,lastBoardId).stream().map(GetAllBoardResponse::new).collect(Collectors.toList());

        return getGetAllBoardResponses(userId, lastBoardId, getAllBoardResponses);
    }

    // bookmark 게시글 조회
    @Override
    public List<GetAllBoardResponse> getBookmarkBoard(Long userId, Long lastBoardId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        if (lastBoardId == null) {
            lastBoardId = Long.MAX_VALUE;
        }

        List<GetAllBoardResponse> getAllBoardResponses = boardRepository.findBookmarkBoardOrderByIdDesc(userId, lastBoardId).stream().map(GetAllBoardResponse::new).collect(Collectors.toList());

        return getGetAllBoardResponses(lastBoardId, getAllBoardResponses);
    }

    // 전체 조회
    @Override
    @Transactional(readOnly = true)
    public List<GetAllBoardResponse> getBoardList(Long userId, String address, String animalType, String categoryType, Long lastBoardId) {
        if (lastBoardId == null) {
            lastBoardId = Long.MAX_VALUE;
        }
        List<GetAllBoardResponse> getAllBoardResponses = boardRepository.findBoardsOrderByIdDesc(address, animalType, categoryType, lastBoardId).stream().map(GetAllBoardResponse::new).collect(Collectors.toList());

        return getGetAllBoardResponses(userId, lastBoardId, getAllBoardResponses);
    }

    private List<GetAllBoardResponse> getGetAllBoardResponses(Long userId, Long lastBoardId, List<GetAllBoardResponse> getAllBoardResponses) {
        if (getAllBoardResponses.size() > 0) {
            lastBoardId = getAllBoardResponses.get(getAllBoardResponses.size() - 1).getBoardId();
        }
        for (GetAllBoardResponse getAllBoardResponse : getAllBoardResponses) {
            getAllBoardResponse.setLikeCheck(!likeRepository.findByUserIdAndBoardId(userId, getAllBoardResponse.getBoardId()).isEmpty());
            getAllBoardResponse.setBookmarkCheck(!bookmarkRepository.findByUserIdAndBoardId(userId, getAllBoardResponse.getBoardId()).isEmpty());
            getAllBoardResponse.setLastBoardId(lastBoardId);
        }
        return getAllBoardResponses;
    }


    private static List<GetAllBoardResponse> getGetAllBoardResponses(Long lastBoardId, List<GetAllBoardResponse> getAllBoardResponses) {
        if (getAllBoardResponses.size() > 0) {
            lastBoardId = getAllBoardResponses.get(getAllBoardResponses.size() - 1).getBoardId();
        }

        for (GetAllBoardResponse getAllBoardResponse : getAllBoardResponses) {
            getAllBoardResponse.setBookmarkCheck(true);
            getAllBoardResponse.setLastBoardId(lastBoardId);
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
        board.getImages().clear();
        if (request.getImageIds().size() != 0) {
            request.getImageIds().forEach(id -> {
                Image findImages = imageRepository.findById(id).orElseThrow(ImageNotFoundException::new);
                board.addImage(findImages);
            });
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
