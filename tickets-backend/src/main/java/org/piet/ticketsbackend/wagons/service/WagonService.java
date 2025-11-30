package org.piet.ticketsbackend.wagons.service;

import org.piet.ticketsbackend.globals.dtos.PageDto;
import org.piet.ticketsbackend.globals.dtos.PaginationDto;
import org.piet.ticketsbackend.wagons.dto.WagonCreateDto;
import org.piet.ticketsbackend.wagons.dto.WagonDto;
import org.piet.ticketsbackend.wagons.dto.WagonUpdateDto;

public interface WagonService {

    PageDto<WagonDto> getAll(PaginationDto paginationDto);

    PageDto<WagonDto> getByTrain(Long trainId, PaginationDto paginationDto);

    WagonDto getById(Long id);

    WagonDto create(WagonCreateDto dto);

    WagonDto update(Long id, WagonUpdateDto dto);

    void delete(Long id);
}
