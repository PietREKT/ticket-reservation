package org.piet.ticketsbackend.TrainsAndWagons;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import org.piet.ticketsbackend.wagons.service.WagonServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WagonServiceImplTest {

    @Mock
    private WagonRepository wagonRepository;

    @Mock
    private TrainRepository trainRepository;

    @InjectMocks
    private WagonServiceImpl wagonService;

    // Sprawdza, czy getAll() wywołuje repozytorium i poprawnie mapuje encję na DTO + paginację
    @Test
    void getAll_delegatesToRepositoryAndMapsResult() {
        PaginationDto pagination = new PaginationDto(0, 10);
        Pageable pageable = pagination.toPageable();

        TrainEntity train = new TrainEntity();
        train.setId(10L);

        WagonEntity entity = new WagonEntity(
                1L,
                "W1",
                80,
                40,
                "2",
                train
        );

        Page<WagonEntity> page = new PageImpl<>(List.of(entity), pageable, 1);
        when(wagonRepository.findAll(pageable)).thenReturn(page);

        PageDto<WagonDto> result = wagonService.getAll(pagination);

        verify(wagonRepository).findAll(pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());

        WagonDto dto = result.getContent().get(0);
        assertEquals(1L, dto.getId());
        assertEquals("W1", dto.getNumber());
        assertEquals(80, dto.getSeatsTotal());
        assertEquals(40, dto.getSeatsFree());
        assertEquals("2", dto.getSeatClass());
        assertEquals(10L, dto.getTrainId());
    }

    // Sprawdza, czy getByTrain() filtruje po pociągu i mapuje wynik
    @Test
    void getByTrain_delegatesToRepositoryAndMapsResult() {
        PaginationDto pagination = new PaginationDto(0, 10);
        Pageable pageable = pagination.toPageable();

        TrainEntity train = new TrainEntity();
        train.setId(10L);

        WagonEntity entity = new WagonEntity(
                1L,
                "W1",
                80,
                40,
                "2",
                train
        );

        Page<WagonEntity> page = new PageImpl<>(List.of(entity), pageable, 1);
        when(wagonRepository.findAllByTrain_Id(10L, pageable)).thenReturn(page);

        PageDto<WagonDto> result = wagonService.getByTrain(10L, pagination);

        verify(wagonRepository).findAllByTrain_Id(10L, pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());

        WagonDto dto = result.getContent().get(0);
        assertEquals(1L, dto.getId());
        assertEquals("W1", dto.getNumber());
        assertEquals(80, dto.getSeatsTotal());
        assertEquals(40, dto.getSeatsFree());
        assertEquals("2", dto.getSeatClass());
        assertEquals(10L, dto.getTrainId());
    }

    // Sprawdza, czy getById() dla istniejącego wagonu zwraca poprawnie wypełniony DTO
    @Test
    void getById_existing_returnsDto() {
        TrainEntity train = new TrainEntity();
        train.setId(10L);

        WagonEntity entity = new WagonEntity(
                1L,
                "W1",
                80,
                40,
                "2",
                train
        );

        when(wagonRepository.findById(1L)).thenReturn(Optional.of(entity));

        WagonDto dto = wagonService.getById(1L);

        verify(wagonRepository).findById(1L);
        assertEquals(1L, dto.getId());
        assertEquals("W1", dto.getNumber());
        assertEquals(80, dto.getSeatsTotal());
        assertEquals(40, dto.getSeatsFree());
        assertEquals("2", dto.getSeatClass());
        assertEquals(10L, dto.getTrainId());
    }

    // Sprawdza, czy getById() dla nieistniejącego wagonu rzuca NotFoundException
    @Test
    void getById_notExisting_throwsNotFound() {
        when(wagonRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> wagonService.getById(1L)
        );

        assertEquals("wagon.not_found", ex.getMessage());
    }

    // Sprawdza, czy create() zapisuje wagon powiązany z istniejącym pociągiem i zwraca poprawny DTO
    @Test
    void create_savesEntityAndReturnsDto() {
        WagonCreateDto createDto = new WagonCreateDto();
        createDto.setNumber("W1");
        createDto.setSeatsTotal(80);
        createDto.setSeatsFree(40);
        createDto.setSeatClass("2");
        createDto.setTrainId(10L);

        TrainEntity train = new TrainEntity();
        train.setId(10L);

        when(trainRepository.findById(10L)).thenReturn(Optional.of(train));
        when(wagonRepository.save(any(WagonEntity.class))).thenAnswer(invocation -> {
            WagonEntity e = invocation.getArgument(0);
            e.setId(1L);
            return e;
        });

        WagonDto dto = wagonService.create(createDto);

        verify(trainRepository).findById(10L);
        verify(wagonRepository).save(any(WagonEntity.class));

        assertEquals(1L, dto.getId());
        assertEquals("W1", dto.getNumber());
        assertEquals(80, dto.getSeatsTotal());
        assertEquals(40, dto.getSeatsFree());
        assertEquals("2", dto.getSeatClass());
        assertEquals(10L, dto.getTrainId());
    }

    // Sprawdza, czy create() dla nieistniejącego pociągu rzuca NotFoundException
    @Test
    void create_withNonExistingTrain_throwsNotFound() {
        WagonCreateDto createDto = new WagonCreateDto();
        createDto.setTrainId(10L);

        when(trainRepository.findById(10L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> wagonService.create(createDto)
        );

        assertEquals("train.not_found", ex.getMessage());
        verify(wagonRepository, never()).save(any());
    }

    // Sprawdza, czy update() dla istniejącego wagonu aktualizuje pola i zwraca poprawny DTO
    @Test
    void update_existing_updatesAndReturnsDto() {
        TrainEntity train = new TrainEntity();
        train.setId(10L);

        WagonEntity existing = new WagonEntity(
                1L,
                "OLD",
                60,
                30,
                "1",
                train
        );

        when(wagonRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(wagonRepository.save(existing)).thenReturn(existing);

        WagonUpdateDto updateDto = new WagonUpdateDto();
        updateDto.setNumber("W1");
        updateDto.setSeatsTotal(80);
        updateDto.setSeatsFree(40);
        updateDto.setSeatClass("2");

        WagonDto dto = wagonService.update(1L, updateDto);

        verify(wagonRepository).findById(1L);
        verify(wagonRepository).save(existing);

        assertEquals("W1", existing.getNumber());
        assertEquals(80, existing.getSeatsTotal());
        assertEquals(40, existing.getSeatsFree());
        assertEquals("2", existing.getSeatClass());

        assertEquals(1L, dto.getId());
        assertEquals("W1", dto.getNumber());
        assertEquals(80, dto.getSeatsTotal());
        assertEquals(40, dto.getSeatsFree());
        assertEquals("2", dto.getSeatClass());
        assertEquals(10L, dto.getTrainId());
    }

    // Sprawdza, czy update() dla nieistniejącego wagonu rzuca NotFoundException
    @Test
    void update_notExisting_throwsNotFound() {
        when(wagonRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> wagonService.update(1L, new WagonUpdateDto())
        );

        assertEquals("wagon.not_found", ex.getMessage());
    }

    // Sprawdza, czy delete() dla istniejącego wagonu usuwa go
    @Test
    void delete_existing_deletesEntity() {
        WagonEntity entity = new WagonEntity();
        entity.setId(1L);

        when(wagonRepository.findById(1L)).thenReturn(Optional.of(entity));

        wagonService.delete(1L);

        verify(wagonRepository).findById(1L);
        verify(wagonRepository).delete(entity);
    }

    // Sprawdza, czy delete() dla nieistniejącego wagonu rzuca NotFoundException
    @Test
    void delete_notExisting_throwsNotFound() {
        when(wagonRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> wagonService.delete(1L)
        );

        assertEquals("wagon.not_found", ex.getMessage());
    }
}
