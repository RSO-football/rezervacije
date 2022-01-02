package rso.football.rezervacije.api.v1.resources;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import rso.football.rezervacije.lib.RezervacijeMetadata;
import rso.football.rezervacije.services.beans.RezervacijeMetadataBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/rezervacije")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin(supportedMethods = "GET, POST, DELETE, PUT, HEAD, OPTIONS")
public class RezervacijeMetadataResource {

    private Logger log = Logger.getLogger(RezervacijeMetadataResource.class.getName());

    @Inject
    private RezervacijeMetadataBean rezervacijeMetadataBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    @Path("/trener/{trenerMetadataId}")
    public Response getRezervacijeTrenerjaMetadata(@PathParam("trenerMetadataId") Integer trenerMetadataId) {
        List<RezervacijeMetadata> rezervacijeMetadata = rezervacijeMetadataBean.getRezervacijeTrenerjaMetadata(trenerMetadataId);

        return Response.status(Response.Status.OK).entity(rezervacijeMetadata.size()).build();
    }

    @GET
    @Path("/{rezervacijeMetadataId}/vreme")
    public Response getRezervacijaVreme(@PathParam("rezervacijeMetadataId") Integer rezervacijeMetadataId) {
        String vreme = rezervacijeMetadataBean.getVremeMetadata(rezervacijeMetadataId);

        return Response.status(Response.Status.OK).entity(vreme).build();
    }

    @GET
    public Response getRezervacijeMetadata() {

        List<RezervacijeMetadata> rezervacijeMetadata = rezervacijeMetadataBean.getRezervacijeMetadataFilter(uriInfo);
        System.out.println(uriInfo.getBaseUri());
        System.out.println(uriInfo.getAbsolutePath());
        System.out.println(uriInfo.getPath());

        return Response.status(Response.Status.OK).entity(rezervacijeMetadata).build();
    }

    @GET
    @Path("/{rezervacijeMetadataId}")
    public Response getRezervacijeMetadata(@PathParam("rezervacijeMetadataId") Integer rezervacijeMetadataId) {

        RezervacijeMetadata rezervacijeMetadata = rezervacijeMetadataBean.getRezervacijeMetadata(rezervacijeMetadataId);

        if (rezervacijeMetadata == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(rezervacijeMetadata).build();
    }

    @POST
    public Response createRezervacijeMetadata(RezervacijeMetadata rezervacijeMetadata) {

        if ((rezervacijeMetadata.getEventType() == null || rezervacijeMetadata.getStartTime() == null)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else {
            rezervacijeMetadata = rezervacijeMetadataBean.createRezervacijeMetadata(rezervacijeMetadata);
        }

        return Response.status(Response.Status.CONFLICT).entity(rezervacijeMetadata).build();

    }

    @PUT
    @Path("{rezervacijeMetadataId}")
    public Response putRezervacijeMetadata(@PathParam("rezervacijeMetadataId") Integer rezervacijeMetadataId,
                                     RezervacijeMetadata rezervacijeMetadata) {

        rezervacijeMetadata = rezervacijeMetadataBean.putRezervacijeMetadata(rezervacijeMetadataId, rezervacijeMetadata);

        if (rezervacijeMetadata == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.NOT_MODIFIED).build();

    }

    @DELETE
    @Path("{rezervacijeMetadataId}")
    public Response deleteRezervacijeMetadata(@PathParam("rezervacijeMetadataId") Integer rezervacijeMetadataId) {

        boolean deleted = rezervacijeMetadataBean.deleteRezervacijeMetadata(rezervacijeMetadataId);

        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
