package rso.football.rezervacije.models.converters;

import rso.football.rezervacije.lib.RezervacijeMetadata;
import rso.football.rezervacije.models.entities.RezervacijeMetadataEntity;

public class RezervacijeMetadataConverter {

    public static RezervacijeMetadata toDto(RezervacijeMetadataEntity entity){
        RezervacijeMetadata dto = new RezervacijeMetadata();
        dto.setRezervacijaId(entity.getId());
//        dto.setIgrisce(entity.getIgrisce());
        dto.setEventType(entity.getEventType());
        dto.setStartTime(entity.getStartTime());
//        dto.setTrener(entity.getTrener());

        return dto;
    }

    public static RezervacijeMetadataEntity toEntity(RezervacijeMetadata dto){
        RezervacijeMetadataEntity entity = new RezervacijeMetadataEntity();
//        entity.setIgrisce(dto.getIgrisce());
        entity.setEventType(dto.getEventType());
        entity.setStartTime(dto.getStartTime());
//        entity.setTrener(dto.getTrener());

        return entity;
    }
}
