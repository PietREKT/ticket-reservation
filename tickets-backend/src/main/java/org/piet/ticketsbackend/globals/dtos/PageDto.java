package org.piet.ticketsbackend.globals.dtos;

import lombok.Value;
import org.springframework.data.domain.Page;

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
}
