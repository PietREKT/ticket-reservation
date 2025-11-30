package org.piet.ticketsbackend;

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
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WagonServiceImplTest {

    @Mock
    private WagonRepository wagonRepository;

    @Mock
    private TrainRepository trainRepository;

    @InjectMocks
    private WagonServiceImpl wagonService;

    @Test
    void getAll_delegatesToRepositoryAndMapsResult() {
        PaginationDto pagination = new PaginationDto();
        pagination.setPage(0);
        pagination.setSize(10);

        PageRequest pageable = PageRequest.of(0, 10);

        WagonEntity entity = new WagonEntity();
        entity.setId(1L);
        entity.setNumber("1");
        entity.setSeatsTotal(100);
        entity.setSeatsFree(80);
        entity.setSeatClass("1");

        TrainEntity train = new TrainEntity();
        train.setId(5L);
        entity.setTrain(train);

        Page<WagonEntity> page = new PageImpl<>(List.of(entity), pageable, 1);
        when(wagonRepository.findAll(any(PageRequest.class))).thenReturn(page);

        PageDto<WagonDto> result = wagonService.getAll(pagination);

        verify(wagonRepository).findAll(any(PageRequest.class));
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());

        WagonDto dto = result.getContent().get(0);
        assertEquals(1L, dto.getId());
        assertEquals("1", dto.getNumber());
        assertEquals(100, dto.getSeatsTotal());
        assertEquals(80, dto.getSeatsFree());
        assertEquals("1", dto.getSeatClass());
        assertEquals(5L, dto.getTrainId());
    }

    @Test
    void getByTrain_delegatesToRepositoryAndMapsResult() {
        PaginationDto pagination = new PaginationDto();
        pagination.setPage(0);
        pagination.setSize(10);

        PageRequest pageable = PageRequest.of(0, 10);

        WagonEntity entity = new WagonEntity();
        entity.setId(2L);
        entity.setNumber("2");
        entity.setSeatsTotal(80);
        entity.setSeatsFree(40);
        entity.setSeatClass("2");

        TrainEntity train = new TrainEntity();
        train.setId(10L);
        entity.setTrain(train);

        Page<WagonEntity> page = new PageImpl<>(List.of(entity), pageable, 1);
        when(wagonRepository.findAllByTrainId(eq(10L), any(PageRequest.class)))
                .thenReturn(page);

        PageDto<WagonDto> result = wagonService.getByTrain(10L, pagination);

        verify(wagonRepository).findAllByTrainId(eq(10L), any(PageRequest.class));
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());

        WagonDto dto = result.getContent().get(0);
        assertEquals(2L, dto.getId());
        assertEquals("2", dto.getNumber());
        assertEquals(80, dto.getSeatsTotal());
        assertEquals(40, dto.getSeatsFree());
        assertEquals("2", dto.getSeatClass());
        assertEquals(10L, dto.getTrainId());
    }

    @Test
    void getById_existing_returnsDto() {
        WagonEntity entity = new WagonEntity();
        entity.setId(1L);
        entity.setNumber("1");
        entity.setSeatsTotal(100);
        entity.setSeatsFree(90);
        entity.setSeatClass("1");

        TrainEntity train = new TrainEntity();
        train.setId(3L);
        entity.setTrain(train);

        when(wagonRepository.findById(1L)).thenReturn(Optional.of(entity));

        WagonDto dto = wagonService.getById(1L);

        verify(wagonRepository).findById(1L);
        assertEquals(1L, dto.getId());
        assertEquals("1", dto.getNumber());
        assertEquals(100, dto.getSeatsTotal());
        assertEquals(90, dto.getSeatsFree());
        assertEquals("1", dto.getSeatClass());
        assertEquals(3L, dto.getTrainId());
    }

    @Test
    void getById_notExisting_throwsNotFound() {
        when(wagonRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> wagonService.getById(1L)
        );

        assertEquals("wagon.not_found", ex.getMessage());
    }

    @Test
    void create_savesEntityAndReturnsDto() {
        WagonCreateDto createDto = new WagonCreateDto();
        createDto.setNumber("3");
        createDto.setSeatsTotal(120);
        createDto.setSeatsFree(100);
        createDto.setSeatClass("1");
        createDto.setTrainId(7L);

        TrainEntity train = new TrainEntity();
        train.setId(7L);

        when(trainRepository.findById(7L)).thenReturn(Optional.of(train));
        when(wagonRepository.save(any(WagonEntity.class))).thenAnswer(invocation -> {
            WagonEntity e = invocation.getArgument(0);
            e.setId(11L);
            return e;
        });

        WagonDto dto = wagonService.create(createDto);

        verify(trainRepository).findById(7L);
        verify(wagonRepository).save(any(WagonEntity.class));

        assertEquals(11L, dto.getId());
        assertEquals("3", dto.getNumber());
        assertEquals(120, dto.getSeatsTotal());
        assertEquals(100, dto.getSeatsFree());
        assertEquals("1", dto.getSeatClass());
        assertEquals(7L, dto.getTrainId());
    }

    @Test
    void create_trainNotExisting_throwsNotFound() {
        WagonCreateDto createDto = new WagonCreateDto();
        createDto.setTrainId(99L);

        when(trainRepository.findById(99L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> wagonService.create(createDto)
        );

        assertEquals("train.not_found", ex.getMessage());
    }

    @Test
    void update_existing_updatesAndReturnsDto() {
        WagonEntity existing = new WagonEntity();
        existing.setId(5L);
        existing.setNumber("OLD");
        existing.setSeatsTotal(50);
        existing.setSeatsFree(10);
        existing.setSeatClass("2");

        TrainEntity train = new TrainEntity();
        train.setId(1L);
        existing.setTrain(train);

        when(wagonRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(wagonRepository.save(existing)).thenReturn(existing);

        WagonUpdateDto updateDto = new WagonUpdateDto();
        updateDto.setNumber("NEW");
        updateDto.setSeatsTotal(60);
        updateDto.setSeatsFree(20);
        updateDto.setSeatClass("1");

        WagonDto dto = wagonService.update(5L, updateDto);

        verify(wagonRepository).findById(5L);
        verify(wagonRepository).save(existing);

        assertEquals("NEW", existing.getNumber());
        assertEquals(60, existing.getSeatsTotal());
        assertEquals(20, existing.getSeatsFree());
        assertEquals("1", existing.getSeatClass());

        assertEquals(5L, dto.getId());
        assertEquals("NEW", dto.getNumber());
        assertEquals(60, dto.getSeatsTotal());
        assertEquals(20, dto.getSeatsFree());
        assertEquals("1", dto.getSeatClass());
        assertEquals(1L, dto.getTrainId());
    }

    @Test
    void update_notExisting_throwsNotFound() {
        when(wagonRepository.findById(5L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> wagonService.update(5L, new WagonUpdateDto())
        );

        assertEquals("wagon.not_found", ex.getMessage());
    }

    @Test
    void delete_existing_deletesEntity() {
        WagonEntity entity = new WagonEntity();
        entity.setId(8L);

        when(wagonRepository.findById(8L)).thenReturn(Optional.of(entity));

        wagonService.delete(8L);

        verify(wagonRepository).findById(8L);
        verify(wagonRepository).delete(entity);
    }

    @Test
    void delete_notExisting_throwsNotFound() {
        when(wagonRepository.findById(8L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> wagonService.delete(8L)
        );

        assertEquals("wagon.not_found", ex.getMessage());
    }
}
