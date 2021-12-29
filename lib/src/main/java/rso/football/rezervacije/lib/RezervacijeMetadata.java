package rso.football.rezervacije.lib;

import java.time.LocalDateTime;

public class RezervacijeMetadata {

    private Integer rezervacijaId;
//    private LocalDateTime startTime;
    private String startTime;
    private String eventType;
    private Integer igrisceId;
    private Integer trenerId;
    //dodaj List<Igralec> igralci ali pa List<Integer> igralci


    public Integer getRezervacijaId() {
        return rezervacijaId;
    }

    public void setRezervacijaId(Integer rezervacijaId) {
        this.rezervacijaId = rezervacijaId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Integer getIgrisceId() {
        return igrisceId;
    }

    public void setIgrisceId(Integer igrisceId) {
        this.igrisceId = igrisceId;
    }

    public Integer getTrenerId() {
        return trenerId;
    }

    public void setTrenerId(Integer trenerId) {
        this.trenerId = trenerId;
    }
}
