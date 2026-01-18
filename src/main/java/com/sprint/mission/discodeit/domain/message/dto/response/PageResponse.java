package com.sprint.mission.discodeit.domain.message.dto.response;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        Object nextCursor,
        int size,
        boolean hasNext,
        Long totalElements
) {
    public static <T> PageResponse<T> of(
            List<T> content,
            Object nextCursor,
            int size,
            boolean hasNext,
            Long totalElements
    ) {
        return new PageResponse<>(content, nextCursor, size, hasNext, null);
    }
}
