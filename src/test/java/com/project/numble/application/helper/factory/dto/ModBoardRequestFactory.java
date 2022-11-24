package com.project.numble.application.helper.factory.dto;

import com.project.numble.application.board.dto.request.AddBoardRequest;
import com.project.numble.application.board.dto.request.ModBoardRequest;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.test.util.ReflectionTestUtils.setField;

public class ModBoardRequestFactory {

    public static ModBoardRequest createModBoardRequest(String content, String categoryType, String boardAddress) {
        ModBoardRequest request = null;
        try {
            Constructor<ModBoardRequest> constructor = ModBoardRequest.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            request = constructor.newInstance();
            List<Long> imageIds = new ArrayList<>(5);
            Set<String> boardAnimalTypes = new HashSet<>(3);
            imageIds.add(1L);
            imageIds.add(2L);
            imageIds.add(3L);
            boardAnimalTypes.add("고양이");
            boardAnimalTypes.add("토끼");

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
