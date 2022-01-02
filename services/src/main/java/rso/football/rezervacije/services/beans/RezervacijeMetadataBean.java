package rso.football.rezervacije.services.beans;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import rso.football.rezervacije.lib.RezervacijeMetadata;
import rso.football.rezervacije.models.converters.RezervacijeMetadataConverter;
import rso.football.rezervacije.models.entities.RezervacijeMetadataEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequestScoped
public class RezervacijeMetadataBean {

    private Logger log = Logger.getLogger(RezervacijeMetadataBean.class.getName());

    @Inject
    private EntityManager em;

    public List<RezervacijeMetadata> getRezervacijeMetadata() {

        TypedQuery<RezervacijeMetadataEntity> query = em.createNamedQuery(
                "RezervacijeMetadataEntity.getAll", RezervacijeMetadataEntity.class);

        List<RezervacijeMetadataEntity> resultList = query.getResultList();

        return resultList.stream().map(RezervacijeMetadataConverter::toDto).collect(Collectors.toList());

    }

    public List<RezervacijeMetadata> getRezervacijeTrenerjaMetadata(Integer trenerId) {
        TypedQuery<RezervacijeMetadataEntity> query = em.createNamedQuery(
                "RezervacijeMetadataEntity.findRezervacijaWithTrenerId", RezervacijeMetadataEntity.class);
        query.setParameter(1, trenerId);

        List<RezervacijeMetadataEntity> resultList = query.getResultList();

        return resultList.stream().map(RezervacijeMetadataConverter::toDto).collect(Collectors.toList());
    }

    public List<RezervacijeMetadata> getRezervacijeMetadataFilter(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        String trenerId = uriInfo.getQueryParameters().getFirst("trenerId");
        String igrisceId = uriInfo.getQueryParameters().getFirst("igrisceId");
        System.out.println(trenerId + " " + igrisceId);

        List<RezervacijeMetadata> results =  JPAUtils.queryEntities(em, RezervacijeMetadataEntity.class, queryParameters).stream()
                .map(RezervacijeMetadataConverter::toDto).collect(Collectors.toList());

        if (trenerId != null){
            List<RezervacijeMetadata> newResults = new ArrayList<>();
            for (RezervacijeMetadata rezervacija : results){
                if (rezervacija.getTrenerId() == Integer.parseInt(trenerId)){
                    newResults.add(rezervacija);
                }
            }
            return newResults;
        }

        if (igrisceId != null){
            List<RezervacijeMetadata> newResults = new ArrayList<>();
            for (RezervacijeMetadata rezervacija : results){
                if (rezervacija.getIgrisceId() == Integer.parseInt(igrisceId)){
                    newResults.add(rezervacija);
                }
            }
            return newResults;
        }

        return results;
    }

    public RezervacijeMetadata getRezervacijeMetadata(Integer id) {

        RezervacijeMetadataEntity rezervacijeMetadataEntity = em.find(RezervacijeMetadataEntity.class, id);

        if (rezervacijeMetadataEntity == null) {
            throw new NotFoundException();
        }

        RezervacijeMetadata rezervacijeMetadata = RezervacijeMetadataConverter.toDto(rezervacijeMetadataEntity);

        return rezervacijeMetadata;
    }

    public RezervacijeMetadata createRezervacijeMetadata(RezervacijeMetadata rezervacijeMetadata) {

        RezervacijeMetadataEntity rezervacijeMetadataEntity = RezervacijeMetadataConverter.toEntity(rezervacijeMetadata);

        try {
            beginTx();
            em.persist(rezervacijeMetadataEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        if (rezervacijeMetadataEntity.getId() == null) {
            throw new RuntimeException("Entity was not persisted");
        }

        return RezervacijeMetadataConverter.toDto(rezervacijeMetadataEntity);
    }

    public RezervacijeMetadata putRezervacijeMetadata(Integer id, RezervacijeMetadata rezervacijeMetadata) {

        RezervacijeMetadataEntity c = em.find(RezervacijeMetadataEntity.class, id);

        if (c == null) {
            return null;
        }

        RezervacijeMetadataEntity updatedRezervacijeMetadataEntity = RezervacijeMetadataConverter.toEntity(rezervacijeMetadata);

        try {
            beginTx();
            updatedRezervacijeMetadataEntity.setId(c.getId());
            updatedRezervacijeMetadataEntity = em.merge(updatedRezervacijeMetadataEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        return RezervacijeMetadataConverter.toDto(updatedRezervacijeMetadataEntity);
    }

    public boolean deleteRezervacijeMetadata(Integer id) {

        RezervacijeMetadataEntity rezervacijeMetadata = em.find(RezervacijeMetadataEntity.class, id);

        if (rezervacijeMetadata != null) {
            try {
                beginTx();
                em.remove(rezervacijeMetadata);
                commitTx();
            }
            catch (Exception e) {
                rollbackTx();
            }
        }
        else {
            return false;
        }

        return true;
    }

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}