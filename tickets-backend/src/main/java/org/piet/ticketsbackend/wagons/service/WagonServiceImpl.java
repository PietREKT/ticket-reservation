package org.piet.ticketsbackend.wagons.service;

import org.piet.ticketsbackend.globals.dtos.PageDto;
import org.piet.ticketsbackend.globals.dtos.PaginationDto;
import org.piet.ticketsbackend.globals.exceptions.NotFoundException;
import org.piet.ticketsbackend.trains.entity.TrainEntity;
import org.piet.ticketsbackend.trains.repository.TrainRepository;
import org.piet.ticketsbackend.wagons.dto.WagonCreateDto;
import org.piet.ticketsbackend.wagons.dto.WagonDto;
import org.piet.ticketsbackend.wagons.dto.WagonUpdateDto;
import org.piet.ticketsbackend.wagons.entity.WagonEntity;
import org.piet.ticketsbackend.wagons.repository.WagonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class WagonServiceImpl implements WagonService {

    private final WagonRepository repository;
    private final TrainRepository trainRepository;

    public WagonServiceImpl(WagonRepository repository, TrainRepository trainRepository) {
        this.repository = repository;
        this.trainRepository = trainRepository;
    }

    @Override
    public PageDto<WagonDto> getAll(PaginationDto paginationDto) {
        PageRequest pageable = PageRequest.of(paginationDto.getPage(), paginationDto.getSize());
        Page<WagonEntity> page = repository.findAll(pageable);
        return PageDto.create(page.map(this::toDto));
    }

    @Override
    public PageDto<WagonDto> getByTrain(Long trainId, PaginationDto paginationDto) {
        PageRequest pageable = PageRequest.of(paginationDto.getPage(), paginationDto.getSize());
        Page<WagonEntity> page = repository.findAllByTrainId(trainId, pageable);
        return PageDto.create(page.map(this::toDto));
    }

    @Override
    public WagonDto getById(Long id) {
        WagonEntity entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("wagon.not_found"));
        return toDto(entity);
    }

    @Override
    public WagonDto create(WagonCreateDto dto) {
        TrainEntity train = trainRepository.findById(dto.getTrainId())
                .orElseThrow(() -> new NotFoundException("train.not_found"));

        WagonEntity entity = new WagonEntity();
        entity.setNumber(dto.getNumber());
        entity.setSeatsTotal(dto.getSeatsTotal());
        entity.setSeatsFree(dto.getSeatsFree());
        entity.setSeatClass(dto.getSeatClass());
        entity.setTrain(train);

        return toDto(repository.save(entity));
    }

    @Override
    public WagonDto update(Long id, WagonUpdateDto dto) {
        WagonEntity entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("wagon.not_found"));

        entity.setNumber(dto.getNumber());
        entity.setSeatsTotal(dto.getSeatsTotal());
        entity.setSeatsFree(dto.getSeatsFree());
        entity.setSeatClass(dto.getSeatClass());

        return toDto(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        WagonEntity entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("wagon.not_found"));
        repository.delete(entity);
    }

    private WagonDto toDto(WagonEntity e) {
        return new WagonDto(
                e.getId(),
                e.getNumber(),
                e.getSeatsTotal(),
                e.getSeatsFree(),
                e.getSeatClass(),
                e.getTrain().getId()
        );
    }
}
