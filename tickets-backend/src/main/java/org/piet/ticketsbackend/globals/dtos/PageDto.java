package org.piet.ticketsbackend.globals.dtos;

import lombok.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

@Value
public class PageDto<T> implements Serializable {
    long totalPages, totalElements;
    boolean last;
    List<T> content;

    public static <T> PageDto<T> create(Page<T> page){
        return new PageDto<>(
                page.getTotalPages(),
                page.getTotalElements(),
                page.isLast(),
                page.getContent()
        );
    }

    public static <T> Page<T> toPage(List<T> list, Pageable pageable){
        if (list == null || list.isEmpty()){
            return Page.empty(pageable);
        }

        int total = list.size();
        int start = (int) pageable.getOffset();

        if (start >= total){
            return Page.empty(pageable);
        }

        int end = Math.min(start + pageable.getPageSize(), total);
        List<T> content = list.subList(start, end);
        return new PageImpl<>(content, pageable, total);
    }

    public static <T> PageDto<T> toPageDto(List<T> list, Pageable pageable){
        return create(toPage(list, pageable));
    }
}
