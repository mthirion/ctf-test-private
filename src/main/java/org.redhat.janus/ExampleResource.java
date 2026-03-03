package org.redhat.janus;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/customers")
public class ExampleResource {

    private String michael="{\"id\": \"1\", \"name\": \"Michael\", \"lastname\": \"Thirion\", \"email\": \"mthirion@redhat.com\"}";
    private String rachid="{\"id\": \"2\", \"name\": \"Rachid\", \"lastname\": \"Snoussi\", \"email\": \"snoussi@redhat.com\"}";

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String customers() {
        //return "[" + michael + "," + rachid + "]";
        return "[toto, lehero]";
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String customer(@PathParam("id") String userId) {
        if (userId.equals("1")) return "toto";
        if (userId.equals("2")) return "lehero";
        return "{}";
    }    
}
