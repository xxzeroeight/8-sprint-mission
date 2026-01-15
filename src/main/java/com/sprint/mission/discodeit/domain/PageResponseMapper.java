package com.sprint.mission.discodeit.domain;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PageResponseMapper
{
    public <T> PageResponse<T> fromSlice(List<T> content, Object nextCursor, boolean hasNext) {
        return PageResponse.of(
                content,
                nextCursor,
                content.size(),
                hasNext,
                null
        );
    }

    public <T> PageResponse<T> fromPage(Page<T> page) {
        return PageResponse.of(
                page.getContent(),
                null,
                page.getSize(),
                page.hasNext(),
                page.getTotalElements()
        );
    }
}
