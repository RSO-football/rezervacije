package rso.football.rezervacije.models.entities;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "rezervacije_metadata")
@NamedQueries(value =
        {
                @NamedQuery(name = "RezervacijeMetadataEntity.getAll",
                        query = "SELECT rezervacija FROM RezervacijeMetadataEntity rezervacija"),
                @NamedQuery(name = "RezervacijeMetadataEntity.findRezervacijaWithTrenerId",
                        query = "SELECT rezervacija FROM RezervacijeMetadataEntity rezervacija WHERE rezervacija.trenerId = ?1")
        })
public class RezervacijeMetadataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "igrisceId")
    private Integer igrisceId;

    @Column(name = "startTime")
    private String startTime;

    @Column(name = "eventType")
    private String eventType;

    @Column(name = "trenerId")
    private Integer trenerId;

    // dodaj List igralcev, ki so v določeni aktivnosti

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
