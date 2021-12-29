package rso.football.rezervacije.models.converters;

import rso.football.rezervacije.lib.RezervacijeMetadata;
import rso.football.rezervacije.models.entities.RezervacijeMetadataEntity;

public class RezervacijeMetadataConverter {

    public static RezervacijeMetadata toDto(RezervacijeMetadataEntity entity){
        RezervacijeMetadata dto = new RezervacijeMetadata();
        dto.setRezervacijaId(entity.getId());
        dto.setIgrisceId(entity.getIgrisceId());
        dto.setEventType(entity.getEventType());
        dto.setStartTime(entity.getStartTime());
        dto.setTrenerId(entity.getTrenerId());

        return dto;
    }

    public static RezervacijeMetadataEntity toEntity(RezervacijeMetadata dto){
        RezervacijeMetadataEntity entity = new RezervacijeMetadataEntity();
        entity.setIgrisceId(dto.getIgrisceId());
        entity.setEventType(dto.getEventType());
        entity.setStartTime(dto.getStartTime());
        entity.setTrenerId(dto.getTrenerId());

        return entity;
    }
}
