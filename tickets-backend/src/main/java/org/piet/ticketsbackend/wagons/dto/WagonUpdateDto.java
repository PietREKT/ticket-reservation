package org.piet.ticketsbackend.wagons.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class WagonUpdateDto {

    @NotBlank
    private String number;

    @NotNull
    @Min(1)
    private Integer seatsTotal;

    @NotNull
    @Min(0)
    private Integer seatsFree;

    @NotBlank
    private String seatClass;

    public WagonUpdateDto() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getSeatsTotal() {
        return seatsTotal;
    }

    public void setSeatsTotal(Integer seatsTotal) {
        this.seatsTotal = seatsTotal;
    }

    public Integer getSeatsFree() {
        return seatsFree;
    }

    public void setSeatsFree(Integer seatsFree) {
        this.seatsFree = seatsFree;
    }

    public String getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(String seatClass) {
        this.seatClass = seatClass;
    }
}
