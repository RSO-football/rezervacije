package rso.football.rezervacije.lib;

import java.time.LocalDateTime;

public class RezervacijeMetadata {

    private Integer rezervacijaId;
//    private LocalDateTime startTime;
    private String startTime;
    private String eventType;
//    private Integer igrisce;
//    private Integer trener;
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

//    public Integer getIgrisce() {
//        return igrisce;
//    }
//
//    public void setIgrisce(Integer igrisce) {
//        this.igrisce = igrisce;
//    }
//
//    public LocalDateTime getStartTime() {
//        return startTime;
//    }
//
//    public void setStartTime(LocalDateTime startTime) {
//        this.startTime = startTime;
//    }
//    public Integer getTrener() {
//        return trener;
//    }
//
//    public void setTrener(Integer trener) {
//        this.trener = trener;
//    }
}
