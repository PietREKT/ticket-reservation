package org.piet.ticketsbackend.passengers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.piet.ticketsbackend.globals.exceptions.NotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PassengerServiceTest {

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private PassengerMapper passengerMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PassengerService passengerService;

    @Test
    void getById_shouldReturnPassengerWhenExists() {
        UUID id = UUID.randomUUID();
        PassengerEntity entity = new PassengerEntity();
        entity.setId(id);
        entity.setFirstName("Marcin");

        when(passengerRepository.findById(id)).thenReturn(Optional.of(entity));

        PassengerEntity result = passengerService.getById(id);

        assertEquals(id, result.getId());
        assertEquals("Marcin", result.getFirstName());
        verify(passengerRepository, times(1)).findById(id);
    }

    @Test
    void getById_shouldThrowNotFoundWhenPassengerDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(passengerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> passengerService.getById(id));
        verify(passengerRepository, times(1)).findById(id);
    }

    @Test
    void delete_shouldDeletePassengerWhenExists() {
        UUID id = UUID.randomUUID();
        PassengerEntity entity = new PassengerEntity();
        entity.setId(id);

        when(passengerRepository.findById(id)).thenReturn(Optional.of(entity));

        passengerService.delete(id);

        verify(passengerRepository, times(1)).delete(entity);
    }
}
