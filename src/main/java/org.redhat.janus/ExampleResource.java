package org.redhat.janus;

import org.jboss.logging.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.OPTIONS;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.core.MediaType;

@Path("/customers")
public class ExampleResource {

    private static final Logger LOG = Logger.getLogger(ExampleResource.class);

    @Inject
    DebugState debug_state;

    private String michael="{\"id\": \"1\", \"name\": \"Michael\", \"lastname\": \"Thirion\", \"email\": \"mthirion@redhat.com\"}";
    private String rachid="{\"id\": \"2\", \"name\": \"Rachid\", \"lastname\": \"Snoussi\", \"email\": \"snoussi@redhat.com\"}";

    @ConfigProperty(name = "quarkus.http.port") 
    String localport;

    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String customers(@HeaderParam("X-TRIGGER") Boolean trigger, @HeaderParam("X-PORT") String port) {
        LOG.info ("debug: " + debug_state.getDebugState());

        if (trigger != null) debug_state.setTrigger(trigger);
        if (port!= null) debug_state.setPort(port);

        LOG.info ("trigger: " + debug_state.getTrigger());
        LOG.info ("port: " + debug_state.getPort());

        if (debug_state.getDebugState())
        { 
            if (!debug_state.getTrigger())  
                LOG.info("[CTF]: Looking for X-TRIGGER header param (boolean): not true");
            else {
                LOG.info("[CTF]: mirroring activated");
                if (!debug_state.getPort().equals("1234"))
                    LOG.info("[CTF]: http call blocked on port "+debug_state.getPort()+ ".  Trying to use X-Port header parameter...");
                else
                    LOG.info("[CTF]: mirroring to myurl.example.com");
            }
        }
        String result = "[" + michael + "," + rachid + "]";
        mirror(result);
        return result;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String customer(@PathParam("id") String userId) {
        if (userId.equals("1")) return michael;
        if (userId.equals("2")) return rachid;
        //if (userId.equals("3")) {
        String disallowed=" !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
            for (int i = 0; i < userId.length(); i++) {
                String character = String.valueOf(userId.charAt(i));
                if (disallowed.contains(character))
                {
                     InputStream is = getClass().getResourceAsStream("/mem-dump.bin");
                    try {
                        String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                        LOG.info("[CTF] memory dump");
                        LOG.info(content);
                    }
                    catch (IOException e) { LOG.info("Unable to read memory dump"); return "{}";}
                    return "{CTF: fatal error: a dump has been generated}";
                }
            }
                /*java.nio.file.Path filePath = java.nio.file.Path.of("mem-dump.bin");
            try {
                String content = Files.readString(filePath);
                LOG.info("[CTF] memory dump");
                LOG.info(content);
            } catch (IOException e) { LOG.info("Unable to read memory dump"); return "{}";}*/
            //return "{CTF: fatal error: a dump has been generated}";
        //}
        return "{}";
    }  

    @POST
    @Path("/shadow")
    @Consumes(MediaType.TEXT_PLAIN)
    public String shadow(String data) {
        LOG.info("[CTF] printing data via curl: "+data);
        return "{}";
    }      
    
    @OPTIONS
    @Produces(MediaType.TEXT_PLAIN)
    @Deprecated
    //@Operation(hidden=true)
    public String options(@QueryParam("debug") boolean debug_value) {
        debug_state.setDebugState(debug_value);
        return "{state changed !}";
    } 

    private void mirror(String data) {
        String host = "localhost";
        String port = localport;
        String envs = System.getenv().toString(); 
        
        LOG.info("[CTF] mirroring...");
        ProcessBuilder pb = new ProcessBuilder("/usr/bin/curl", "-H", "Content-Type: text/plain", "-H", "apikey: dflkhdlfkhdlf", "-X", "POST" ,"-d" , data, "http://"+host+":"+port+"/customers/shadow");  
        pb.redirectErrorStream(true); 
        try {
            pb.start(); 
        } catch (IOException e) {LOG.info("Unable to start Process");};
    }
    

}
