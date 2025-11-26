package org.piet.ticketsbackend.timetables.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Entity
public class Timetable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "timetable", orphanRemoval = true, cascade = CascadeType.ALL)
    @NotEmpty
    List<TimetableStop> stops;

    private LocalTime departureTime;
    private LocalTime arrivalTime;

    @ElementCollection(targetClass = DayOfWeek.class)
    @CollectionTable(
            name = "timetable_days",
            joinColumns = @JoinColumn(name = "timetable_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "operating_days")
    @NotEmpty
    private Set<DayOfWeek> operatingDays = new HashSet<>();

    private Integer waitingTimeAtStation;

    @PrePersist
    @PreUpdate
    void setArrivalTime() {
        int lastPos = stops.stream()
                .mapToInt(s -> s.getStop().getPosition())
                .max()
                .orElseThrow();

        var lastStop = stops.stream()
                .filter(s -> s.getStop().getPosition() == lastPos)
                .findFirst()
                .orElseThrow();

        arrivalTime = lastStop.getArrivalTime();
    }

}