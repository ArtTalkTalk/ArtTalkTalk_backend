package org.example.youth_be.common;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

public class CursorPagingCommon {

    public static Slice<?> getSlice(List<?> entities, List<?> responses, Integer size) {
        boolean hasNext = entities.size() > size;

        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(0, size);

        // SliceImpl 객체로 반환
        return new SliceImpl<>(responses, pageable, hasNext);
    }
}
