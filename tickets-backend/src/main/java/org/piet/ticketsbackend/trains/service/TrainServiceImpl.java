package org.piet.ticketsbackend.trains.service;

import org.piet.ticketsbackend.globals.dtos.PageDto;
import org.piet.ticketsbackend.globals.dtos.PaginationDto;
import org.piet.ticketsbackend.globals.exceptions.NotFoundException;
import org.piet.ticketsbackend.trains.dto.TrainCreateDto;
import org.piet.ticketsbackend.trains.dto.TrainDto;
import org.piet.ticketsbackend.trains.dto.TrainUpdateDto;
import org.piet.ticketsbackend.trains.entity.TrainEntity;
import org.piet.ticketsbackend.trains.repository.TrainRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class TrainServiceImpl implements TrainService {

    private final TrainRepository repository;

    public TrainServiceImpl(TrainRepository repository) {
        this.repository = repository;
    }

    @Override
    public PageDto<TrainDto> getAll(PaginationDto paginationDto) {
        Page<TrainEntity> result = repository.findAll(paginationDto.toPageable());
        return PageDto.create(result.map(this::toDto));
    }

    @Override
    public TrainDto getById(Long id) {
        TrainEntity entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("train.not_found"));
        return toDto(entity);
    }

    @Override
    public TrainDto create(TrainCreateDto dto) {
        TrainEntity entity = new TrainEntity();
        entity.setModel(dto.getModel());
        entity.setNumber(dto.getName());

        TrainEntity saved = repository.save(entity);
        return toDto(saved);
    }

    @Override
    public TrainDto update(Long id, TrainUpdateDto dto) {
        TrainEntity entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("train.not_found"));

        entity.setModel(dto.getModel());
        entity.setNumber(dto.getName());

        TrainEntity saved = repository.save(entity);
        return toDto(saved);
    }

    @Override
    public void delete(Long id) {
        TrainEntity entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("train.not_found"));

        repository.delete(entity);
    }

    private TrainDto toDto(TrainEntity e) {
        return new TrainDto(
                e.getId(),
                e.getModel(),
                e.getNumber(),
                null
        );
    }
}
