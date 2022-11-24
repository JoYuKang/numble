package com.project.numble.application.helper.factory.dto;

import com.project.numble.application.board.dto.response.GetBoardResponse;

import java.lang.reflect.Constructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.util.ReflectionTestUtils.setField;

public class GetBoardResponseFactory {
    public static GetBoardResponse createGetBoardResponse() {
        GetBoardResponse response = null;

        try {
            Constructor<GetBoardResponse> constructor = GetBoardResponse.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            response = constructor.newInstance();

            List<Long> imageIds = new ArrayList<>();
            imageIds.add(1L);
            List<String> boardAnimalTypes = new ArrayList<>();
            boardAnimalTypes.add("강아지");

            setField(response, "content", "board content");
            setField(response, "imageIds", imageIds);
            setField(response, "nickname", "홍길동");
            setField(response, "boardAnimalTypes", boardAnimalTypes);
            setField(response, "categoryType", "반려고수");
            setField(response, "boardAddress", "강남구");
            setField(response, "likeCount", 6);
            setField(response, "likeCheck", true);
            setField(response, "viewCount", 5);
            setField(response, "bookmarkCount", 2);
            setField(response, "bookmarkCheck", false);
            setField(response, "createdDate", LocalDateTime.now());
            setField(response, "lastModifiedDate", LocalDateTime.now());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
