package org.piet.ticketsbackend;

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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainServiceImplTest {

    @Mock
    private TrainRepository trainRepository;

    @InjectMocks
    private TrainServiceImpl trainService;

    @Test
    void getAll_delegatesToRepositoryAndMapsResult() {
        PaginationDto pagination = new PaginationDto(0, 10);
        Pageable pageable = pagination.toPageable();

        TrainEntity entity = new TrainEntity();
        entity.setId(1L);
        entity.setModel("IC");
        entity.setNumber("EIP-123");

        Page<TrainEntity> page = new PageImpl<>(List.of(entity), pageable, 1);
        when(trainRepository.findAll(pageable)).thenReturn(page);

        PageDto<TrainDto> result = trainService.getAll(pagination);

        verify(trainRepository).findAll(pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());

        TrainDto dto = result.getContent().get(0);
        assertEquals(1L, dto.getId());
        assertEquals("IC", dto.getModel());
        assertEquals("EIP-123", dto.getName());
    }

    @Test
    void getById_existing_returnsDto() {
        TrainEntity entity = new TrainEntity();
        entity.setId(1L);
        entity.setModel("IC");
        entity.setNumber("EIP-123");

        when(trainRepository.findById(1L)).thenReturn(Optional.of(entity));

        TrainDto dto = trainService.getById(1L);

        verify(trainRepository).findById(1L);
        assertEquals(1L, dto.getId());
        assertEquals("IC", dto.getModel());
        assertEquals("EIP-123", dto.getName());
    }

    @Test
    void getById_notExisting_throwsNotFound() {
        when(trainRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> trainService.getById(1L)
        );

        assertEquals("train.not_found", ex.getMessage());
    }

    @Test
    void create_savesEntityAndReturnsDto() {
        TrainCreateDto createDto = new TrainCreateDto();
        createDto.setModel("IC");
        createDto.setName("EIP-123");

        when(trainRepository.save(any(TrainEntity.class))).thenAnswer(invocation -> {
            TrainEntity e = invocation.getArgument(0);
            e.setId(1L);
            return e;
        });

        TrainDto dto = trainService.create(createDto);

        verify(trainRepository).save(any(TrainEntity.class));
        assertEquals(1L, dto.getId());
        assertEquals("IC", dto.getModel());
        assertEquals("EIP-123", dto.getName());
    }

    @Test
    void update_existing_updatesAndReturnsDto() {
        TrainEntity existing = new TrainEntity();
        existing.setId(1L);
        existing.setModel("old");
        existing.setNumber("OLD-1");

        when(trainRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(trainRepository.save(existing)).thenReturn(existing);

        TrainUpdateDto updateDto = new TrainUpdateDto();
        updateDto.setModel("IC");
        updateDto.setName("EIP-123");

        TrainDto dto = trainService.update(1L, updateDto);

        verify(trainRepository).findById(1L);
        verify(trainRepository).save(existing);

        assertEquals("IC", existing.getModel());
        assertEquals("EIP-123", existing.getNumber());

        assertEquals(1L, dto.getId());
        assertEquals("IC", dto.getModel());
        assertEquals("EIP-123", dto.getName());
    }

    @Test
    void update_notExisting_throwsNotFound() {
        when(trainRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> trainService.update(1L, new TrainUpdateDto())
        );

        assertEquals("train.not_found", ex.getMessage());
    }

    @Test
    void delete_existing_deletesEntity() {
        TrainEntity entity = new TrainEntity();
        entity.setId(1L);

        when(trainRepository.findById(1L)).thenReturn(Optional.of(entity));

        trainService.delete(1L);

        verify(trainRepository).findById(1L);
        verify(trainRepository).delete(entity);
    }

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
