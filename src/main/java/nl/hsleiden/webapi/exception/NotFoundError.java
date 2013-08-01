/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.hsleiden.webapi.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author hl
 */
public class NotFoundError extends WebApplicationException {
     public NotFoundError(String message) {
         super(Response.status(Response.Status.NOT_FOUND)
             .entity(message).type(MediaType.TEXT_PLAIN).build());
     }
    
}
