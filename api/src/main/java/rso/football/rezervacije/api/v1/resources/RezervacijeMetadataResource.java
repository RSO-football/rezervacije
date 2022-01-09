package rso.football.rezervacije.api.v1.resources;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import rso.football.rezervacije.lib.RezervacijeMetadata;
import rso.football.rezervacije.services.beans.RezervacijeMetadataBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@Log
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

    @Operation(description = "Get rezervacije from one trener.", summary = "Get metadata for one trener")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "List of rezervacije metadata",
                    content = @Content(schema = @Schema(implementation = RezervacijeMetadata.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Number of objects in list")}
            )})
    @GET
    @Path("/trener/{trenerMetadataId}")
    public Response getRezervacijeTrenerjaMetadata(@PathParam("trenerMetadataId") Integer trenerMetadataId) {
        List<RezervacijeMetadata> rezervacijeMetadata = rezervacijeMetadataBean.getRezervacijeTrenerjaMetadata(trenerMetadataId);

        return Response.status(Response.Status.OK).entity(rezervacijeMetadata.size()).build();
    }

    @Operation(description = "Get vreme for one rezervacija.", summary = "Get vreme metadata")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Vreme",
                    content = @Content(schema = @Schema(implementation = Json.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Vreme")}
            )})
    @GET
    @Path("/{rezervacijeMetadataId}/vreme")
    public Response getRezervacijaVreme(@PathParam("rezervacijeMetadataId") Integer rezervacijeMetadataId) {
        String vreme = rezervacijeMetadataBean.getVremeMetadata(rezervacijeMetadataId);

        return Response.status(Response.Status.OK).entity(vreme).build();
    }

    @Operation(description = "Get all rezervacije metadata.", summary = "Get all metadata")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "List of rezervacije metadata",
                    content = @Content(schema = @Schema(implementation = RezervacijeMetadata.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Number of objects in list")}
            )})
    @GET
    public Response getRezervacijeMetadata() {

        List<RezervacijeMetadata> rezervacijeMetadata = rezervacijeMetadataBean.getRezervacijeMetadataFilter(uriInfo);
        System.out.println(uriInfo.getBaseUri());
        System.out.println(uriInfo.getAbsolutePath());
        System.out.println(uriInfo.getPath());

        return Response.status(Response.Status.OK).entity(rezervacijeMetadata).build();
    }

    @Operation(description = "Get metadata for one rezervacija.", summary = "Get metadata for one rezervacija")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Igrisce metadata",
                    content = @Content(
                            schema = @Schema(implementation = RezervacijeMetadata.class))
            )})
    @GET
    @Path("/{rezervacijeMetadataId}")
    public Response getRezervacijeMetadata(@Parameter(description = "Metadata ID.", required = true)
                                               @PathParam("rezervacijeMetadataId") Integer rezervacijeMetadataId) {

        RezervacijeMetadata rezervacijeMetadata = rezervacijeMetadataBean.getRezervacijeMetadata(rezervacijeMetadataId);

        if (rezervacijeMetadata == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(rezervacijeMetadata).build();
    }

    @Operation(description = "Add rezervacija metadata.", summary = "Add metadata")
    @APIResponses({
            @APIResponse(responseCode = "201",
                    description = "Metadata successfully added."
            ),
            @APIResponse(responseCode = "400", description = "Bad request.")
    })
    @POST
    public Response createRezervacijeMetadata(@RequestBody(
            description = "DTO object with rezervacije metadata.",
            required = true, content = @Content(
            schema = @Schema(implementation = RezervacijeMetadata.class))) RezervacijeMetadata rezervacijeMetadata) {

        if ((rezervacijeMetadata.getEventType() == null || rezervacijeMetadata.getStartTime() == null)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else {
            rezervacijeMetadata = rezervacijeMetadataBean.createRezervacijeMetadata(rezervacijeMetadata);
            if (rezervacijeMetadata == null){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }

        return Response.status(Response.Status.CREATED).entity(rezervacijeMetadata).build();

    }

    @Operation(description = "Update metadata for on rezervacija.", summary = "Update metadata")
    @APIResponses({
            @APIResponse(
                    responseCode = "204",
                    description = "Metadata successfully updated."
            ),
            @APIResponse(responseCode = "404", description = "Not found.")
    })
    @PUT
    @Path("{rezervacijeMetadataId}")
    public Response putRezervacijeMetadata(@Parameter(description = "Metadata ID.", required = true)
                                               @PathParam("rezervacijeMetadataId") Integer rezervacijeMetadataId,
                                           @RequestBody(
                                                   description = "DTO object with rezervacija metadata.",
                                                   required = true, content = @Content(
                                                   schema = @Schema(implementation = RezervacijeMetadata.class)))
                                     RezervacijeMetadata rezervacijeMetadata) {

        rezervacijeMetadata = rezervacijeMetadataBean.putRezervacijeMetadata(rezervacijeMetadataId, rezervacijeMetadata);

        if (rezervacijeMetadata == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.NO_CONTENT).build();

    }

    @Operation(description = "Delete metadata for one rezervacija.", summary = "Delete metadata")
    @APIResponses({
            @APIResponse(
                    responseCode = "204",
                    description = "Metadata successfully deleted."
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Not found."
            )
    })
    @DELETE
    @Path("{rezervacijeMetadataId}")
    public Response deleteRezervacijeMetadata(@Parameter(description = "Metadata ID.", required = true)
                                                  @PathParam("rezervacijeMetadataId") Integer rezervacijeMetadataId) {

        boolean deleted = rezervacijeMetadataBean.deleteRezervacijeMetadata(rezervacijeMetadataId);

        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
