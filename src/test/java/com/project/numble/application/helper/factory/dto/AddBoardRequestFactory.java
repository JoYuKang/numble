package com.project.numble.application.helper.factory.dto;

import com.project.numble.application.board.dto.request.AddBoardRequest;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.test.util.ReflectionTestUtils.setField;

public class AddBoardRequestFactory {

    public static AddBoardRequest createAddBoardRequest(String content, String categoryType, String boardAddress) {
        AddBoardRequest request = null;
        try {
            Constructor<AddBoardRequest> constructor = AddBoardRequest.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            request = constructor.newInstance();
            List<Long> imageIds = new ArrayList<>(5);
            Set<String> boardAnimalTypes = new HashSet<>(3);
            imageIds.add(1L);
            imageIds.add(2L);
            boardAnimalTypes.add("강이지");

            setField(request, "content", content);
            setField(request, "imageIds", imageIds);
            setField(request, "categoryType", categoryType);
            setField(request, "boardAddress", boardAddress);
            setField(request, "boardAnimalTypes", boardAnimalTypes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }
}
