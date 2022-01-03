package rso.football.rezervacije.services.beans;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import rso.football.rezervacije.lib.IgriscaMetadata;
import rso.football.rezervacije.lib.RezervacijeMetadata;
import rso.football.rezervacije.models.converters.RezervacijeMetadataConverter;
import rso.football.rezervacije.models.entities.RezervacijeMetadataEntity;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequestScoped
public class RezervacijeMetadataBean {

    private Logger log = Logger.getLogger(RezervacijeMetadataBean.class.getName());

    @Inject
    private EntityManager em;

    private Client httpClient;
    private String baseUrlIgrisca;
    private String baseUrlUporabniki;

    @PostConstruct
    private void init() {
        String uniqueID = UUID.randomUUID().toString();
        log.info("Inicializacija zrna: " + RezervacijeMetadataBean.class.getSimpleName() + " id: " + uniqueID);

        httpClient = ClientBuilder.newClient();
        baseUrlIgrisca = ConfigurationUtil.getInstance().get("igrisca-storitev.url").orElse("http://localhost:8080/");
        baseUrlUporabniki = ConfigurationUtil.getInstance().get("uporabniki-storitev.url").orElse("http://localhost:8083/");
    }

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

    public String getVremeMetadata(Integer id) {
        RezervacijeMetadataEntity rezervacijeMetadataEntity = em.find(RezervacijeMetadataEntity.class, id);

        if (rezervacijeMetadataEntity == null) {
            throw new NotFoundException();
        }

        RezervacijeMetadata rezervacijeMetadata = RezervacijeMetadataConverter.toDto(rezervacijeMetadataEntity);

        IgriscaMetadata igrisce = getIgrisceMetadata(rezervacijeMetadata.getIgrisceId());

        if (igrisce != null){
            String apiKey = "b08f0af497bd1961a8dcc3ad79a88357";
            String url = "https://api.openweathermap.org/data/2.5/onecall?lat="+igrisce.getLatitude()+"&lon="+igrisce.getLongitude()+"&appid="+apiKey;
            log.info("url api vreme je " + url);
            try{
                return httpClient
                        .target(url)
                        .request()
                        .get(String.class);
            } catch (WebApplicationException | ProcessingException e){
                throw new InternalServerErrorException(e);
            }
        }

        return null;
    }

    private IgriscaMetadata getIgrisceMetadata(Integer id) {
        String url = baseUrlIgrisca + "v1/igrisca/" + id;

        log.info("url je " + url);

        try{
            return httpClient
                    .target(url)
                    .request().get(new GenericType<IgriscaMetadata>() {
                    });
        } catch (WebApplicationException | ProcessingException e){
            log.info("Napaka pri pridobivanju igrisca!");
            throw new InternalServerErrorException(e);
        }
    }

    public RezervacijeMetadata getRezervacijeMetadata(Integer id) {

        RezervacijeMetadataEntity rezervacijeMetadataEntity = em.find(RezervacijeMetadataEntity.class, id);

        if (rezervacijeMetadataEntity == null) {
            throw new NotFoundException();
        }

        RezervacijeMetadata rezervacijeMetadata = RezervacijeMetadataConverter.toDto(rezervacijeMetadataEntity);

        return rezervacijeMetadata;
    }

    public String getTrenerjiId(){
        String url = baseUrlUporabniki + "v1/uporabniki/trenerjiId";
        log.info("url je " + url);

        try {
            return httpClient
                    .target(url)
                    .request().get(String.class);
        } catch (WebApplicationException | ProcessingException e){
            throw new InternalServerErrorException(e);
        }
    }

    public RezervacijeMetadata createRezervacijeMetadata(RezervacijeMetadata rezervacijeMetadata) {

        RezervacijeMetadataEntity rezervacijeMetadataEntity = RezervacijeMetadataConverter.toEntity(rezervacijeMetadata);

        String trenerjiString = getTrenerjiId();
        List<Integer> trenerjiId = Arrays.stream(trenerjiString.split(",")).map(Integer::parseInt).collect(Collectors.toList());

        System.out.println(trenerjiString);

        if (!trenerjiId.contains(rezervacijeMetadataEntity.getTrenerId())){
            return null;
        }

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