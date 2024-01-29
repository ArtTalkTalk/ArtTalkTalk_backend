package org.example.youth_be.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
@AllArgsConstructor
public class PageResponse<T> {
    private List<T> contents;
    private boolean hasNext;
    private Long cursorId;

    public static <T> PageResponse<T> of(Slice<T> slice, Long cursorId) {
        return new PageResponse<>(slice.getContent(), slice.hasNext(), cursorId);
    }
}