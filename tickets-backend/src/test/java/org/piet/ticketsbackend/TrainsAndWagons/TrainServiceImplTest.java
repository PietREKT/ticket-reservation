package org.piet.ticketsbackend.TrainsAndWagons;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.piet.ticketsbackend.globals.dtos.PageDto;
import org.piet.ticketsbackend.globals.dtos.PaginationDto;
import org.piet.ticketsbackend.globals.exceptions.NotFoundException;
import org.piet.ticketsbackend.trains.dto.TrainCreateDto;
import org.piet.ticketsbackend.trains.dto.TrainDto;
import org.piet.ticketsbackend.trains.dto.TrainUpdateDto;
import org.piet.ticketsbackend.trains.entity.TrainEntity;
import org.piet.ticketsbackend.trains.repository.TrainRepository;
import org.piet.ticketsbackend.trains.service.TrainServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainServiceImplTest {

    @Mock
    private TrainRepository trainRepository;

    @InjectMocks
    private TrainServiceImpl trainService;

    // Sprawdza, czy getAll() wywołuje repozytorium i poprawnie mapuje encję na DTO
    @Test
    void getAll_delegatesToRepositoryAndMapsResult() {
        PaginationDto pagination = new PaginationDto(0, 10);
        Pageable pageable = pagination.toPageable();

        TrainEntity entity = new TrainEntity(
                1L,
                "IC",
                "EIP-123",
                "L1",
                160,
                8
        );

        Page<TrainEntity> page = new PageImpl<>(List.of(entity), pageable, 1);
        when(trainRepository.findAll(pageable)).thenReturn(page);

        PageDto<TrainDto> result = trainService.getAll(pagination);

        verify(trainRepository).findAll(pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());

        TrainDto dto = result.getContent().get(0);
        assertEquals(1L, dto.getId());
        assertEquals("IC", dto.getModel());
        assertEquals("EIP-123", dto.getNumber());
        assertEquals("L1", dto.getLineNumber());
        assertEquals(160, dto.getSpeed());
        assertEquals(8, dto.getWagonCount());
        assertNull(dto.getRouteId());
    }

    // Sprawdza, czy getById() dla istniejącego pociągu zwraca poprawnie wypełniony DTO
    @Test
    void getById_existing_returnsDto() {
        TrainEntity entity = new TrainEntity(
                1L,
                "IC",
                "EIP-123",
                "L1",
                160,
                8
        );

        when(trainRepository.findById(1L)).thenReturn(Optional.of(entity));

        TrainDto dto = trainService.getById(1L);

        verify(trainRepository).findById(1L);
        assertEquals(1L, dto.getId());
        assertEquals("IC", dto.getModel());
        assertEquals("EIP-123", dto.getNumber());
        assertEquals("L1", dto.getLineNumber());
        assertEquals(160, dto.getSpeed());
        assertEquals(8, dto.getWagonCount());
        assertNull(dto.getRouteId());
    }

    // Sprawdza, czy getById() dla nieistniejącego pociągu rzuca NotFoundException
    @Test
    void getById_notExisting_throwsNotFound() {
        when(trainRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> trainService.getById(1L)
        );

        assertEquals("train.not_found", ex.getMessage());
    }

    // Sprawdza, czy create() zapisuje encję w repozytorium i zwraca poprawny DTO
    @Test
    void create_savesEntityAndReturnsDto() {
        TrainCreateDto createDto = new TrainCreateDto();
        createDto.setModel("IC");
        createDto.setNumber("EIP-123");
        createDto.setLineNumber("L1");
        createDto.setSpeed(160);
        createDto.setWagonCount(8);
        createDto.setRouteId(5L);

        when(trainRepository.save(any(TrainEntity.class))).thenAnswer(invocation -> {
            TrainEntity e = invocation.getArgument(0);
            e.setId(1L);
            return e;
        });

        TrainDto dto = trainService.create(createDto);

        verify(trainRepository).save(any(TrainEntity.class));
        assertEquals(1L, dto.getId());
        assertEquals("IC", dto.getModel());
        assertEquals("EIP-123", dto.getNumber());
        assertEquals("L1", dto.getLineNumber());
        assertEquals(160, dto.getSpeed());
        assertEquals(8, dto.getWagonCount());
        assertEquals(5L, dto.getRouteId());
    }

    // Sprawdza, czy update() dla istniejącego pociągu aktualizuje pola i zwraca poprawny DTO
    @Test
    void update_existing_updatesAndReturnsDto() {
        TrainEntity existing = new TrainEntity(
                1L,
                "OLD",
                "OLD-1",
                "OLD-L",
                120,
                5
        );

        when(trainRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(trainRepository.save(existing)).thenReturn(existing);

        TrainUpdateDto updateDto = new TrainUpdateDto();
        updateDto.setModel("IC");
        updateDto.setNumber("EIP-123");
        updateDto.setLineNumber("L1");
        updateDto.setSpeed(160);
        updateDto.setWagonCount(8);
        updateDto.setRouteId(10L);

        TrainDto dto = trainService.update(1L, updateDto);

        verify(trainRepository).findById(1L);
        verify(trainRepository).save(existing);

        assertEquals("IC", existing.getModel());
        assertEquals("EIP-123", existing.getNumber());
        assertEquals("L1", existing.getLineNumber());
        assertEquals(160, existing.getSpeed());
        assertEquals(8, existing.getWagonCount());

        assertEquals(1L, dto.getId());
        assertEquals("IC", dto.getModel());
        assertEquals("EIP-123", dto.getNumber());
        assertEquals("L1", dto.getLineNumber());
        assertEquals(160, dto.getSpeed());
        assertEquals(8, dto.getWagonCount());
        assertEquals(10L, dto.getRouteId());
    }

    // Sprawdza, czy update() dla nieistniejącego pociągu rzuca NotFoundException
    @Test
    void update_notExisting_throwsNotFound() {
        when(trainRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> trainService.update(1L, new TrainUpdateDto())
        );

        assertEquals("train.not_found", ex.getMessage());
    }

    // Sprawdza, czy delete() dla istniejącego pociągu usuwa go z repozytorium
    @Test
    void delete_existing_deletesEntity() {
        TrainEntity entity = new TrainEntity();
        entity.setId(1L);

        when(trainRepository.findById(1L)).thenReturn(Optional.of(entity));

        trainService.delete(1L);

        verify(trainRepository).findById(1L);
        verify(trainRepository).delete(entity);
    }

    // Sprawdza, czy delete() dla nieistniejącego pociągu rzuca NotFoundException
    @Test
    void delete_notExisting_throwsNotFound() {
        when(trainRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> trainService.delete(1L)
        );

        assertEquals("train.not_found", ex.getMessage());
    }
}
