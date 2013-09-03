/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.hsleiden.webapi.v1.service;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import nl.hsleiden.webapi.util.Result;

/**
 * REST Web Service
 *
 * @author hl
 */
@Path("v1/person")
public class PersonFacadeREST {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of PersonFacadeREST
     */
    public PersonFacadeREST() {
    }

    /**
     * Retrieves representation of an instance of nl.hsleiden.webapi.v1.service.PersonFacadeREST
     * @return an instance of java.lang.String
     */
    @GET
    @Path("{lastname}")
    @Produces({"application/json", "application/xml"})
    public Result getPersonsByLastName(@PathParam("lastname") String lastname, @QueryParam("max") String max, @QueryParam("offset") String offset) {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    
}
