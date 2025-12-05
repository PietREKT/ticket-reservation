package org.piet.ticketsbackend.wagons.dto;

public class WagonDto {

    private Long id;
    private String number;
    private Integer seatsTotal;
    private Integer seatsFree;
    private String seatClass;
    private Long trainId;

    public WagonDto() {
    }

    public WagonDto(
            Long id,
            String number,
            Integer seatsTotal,
            Integer seatsFree,
            String seatClass,
            Long trainId
    ) {
        this.id = id;
        this.number = number;
        this.seatsTotal = seatsTotal;
        this.seatsFree = seatsFree;
        this.seatClass = seatClass;
        this.trainId = trainId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getTrainId() {
        return trainId;
    }

    public void setTrainId(Long trainId) {
        this.trainId = trainId;
    }
}
