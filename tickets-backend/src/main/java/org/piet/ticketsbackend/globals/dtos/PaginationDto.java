package org.piet.ticketsbackend.globals.dtos;

import lombok.Value;

import java.io.Serializable;

@Value
public class PaginationDto implements Serializable {
    Integer page, size;

    public PaginationDto(Integer page, Integer size) {
        this.page = page != null ? page : 0;
        this.size = size != null ? size : 20;
    }
}
