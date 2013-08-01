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

public class InternalServerError extends WebApplicationException {
     public InternalServerError(String message) {
         super(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
             .entity(message).type(MediaType.TEXT_PLAIN).build());
     }
}
