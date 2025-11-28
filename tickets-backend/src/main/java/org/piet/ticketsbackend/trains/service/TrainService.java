package org.piet.ticketsbackend.trains.service;

import org.piet.ticketsbackend.globals.dtos.PageDto;
import org.piet.ticketsbackend.trains.dto.TrainCreateDto;
import org.piet.ticketsbackend.trains.dto.TrainDto;
import org.piet.ticketsbackend.trains.dto.TrainUpdateDto;

public interface TrainService {

    PageDto<TrainDto> getAll(int page, int size);

    TrainDto getById(Long id);

    TrainDto create(TrainCreateDto dto);

    TrainDto update(Long id, TrainUpdateDto dto);

    void delete(Long id);
}
