package org.piet.ticketsbackend.timetables.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.piet.ticketsbackend.routes.entities.RouteStop;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TimetableStop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "routestop_id")
    @NotNull
    private RouteStop stop;

    @ManyToOne
    @JoinColumn(name = "timetable_id")
    @NotNull
    Timetable timetable;

    private LocalTime arrivalTime;

    private LocalTime departureTime;
}
