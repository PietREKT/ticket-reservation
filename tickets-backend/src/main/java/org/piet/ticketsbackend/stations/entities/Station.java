package org.piet.ticketsbackend.stations.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@Setter
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String description; //only when needed to specify - "Główny" / "Zachód" etc

    @Length(min = 3, max = 5)
    @Column(unique = true)
    String code;

    @Length(min = 2, max = 4)
    String countryCode;

    @NotBlank
    String city;

    @NotNull
    @Column(unique = true)
    Point location;

    public String getFullName(){
        return city + (description != null ? " " + description : "");
    }

    public boolean equalsStation(Station station) {
        return id.equals(station.id);
    }

    @Override
    public String toString() {
        return "Station{ " +
                "id=" + id +
                ", description='" + description + '\'' +
                ", code='" + code + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", city='" + city + '\'' +
                ", location=" + (location != null ? location.toString() : "null") + '\'' +
                ", fullName='" + getFullName() + '\'' +
                " }";
    }
}
