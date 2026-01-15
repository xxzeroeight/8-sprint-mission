package com.sprint.mission.discodeit.domain;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        boolean hasNext,
        Long totalElements
) {
    public static <T> PageResponse<T> of(
            List<T> content,
            int number,
            int size,
            boolean hasNext,
            Long totalElements
    ) {
        return new PageResponse<T>(content, number, size, hasNext, totalElements);
    }
}
