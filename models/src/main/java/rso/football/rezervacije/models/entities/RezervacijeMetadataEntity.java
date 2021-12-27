package rso.football.rezervacije.models.entities;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "rezervacije_metadata")
@NamedQueries(value =
        {
                @NamedQuery(name = "RezervacijeMetadataEntity.getAll",
                        query = "SELECT rezervacija FROM RezervacijeMetadataEntity rezervacija")
        })
public class RezervacijeMetadataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @Column(name = "igrisce")
//    private Integer igrisce;

    @Column(name = "startTime")
    private String startTime;

    @Column(name = "eventType")
    private String eventType;

//    @Column(name = "trener")
//    private Integer trener;

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

//    public Integer getTrener() {
//        return trener;
//    }
//
//    public void setTrener(Integer trener) {
//        this.trener = trener;
//    }

//    public Integer getIgrisce() {
//        return igrisce;
//    }
//
//    public void setIgrisce(Integer igrisce) {
//        this.igrisce = igrisce;
//    }
}
