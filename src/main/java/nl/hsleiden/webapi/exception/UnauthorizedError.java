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
public class UnauthorizedError extends WebApplicationException {
    public UnauthorizedError(String message) {
         super(Response.status(Response.Status.FORBIDDEN)
             .entity(message).type(MediaType.TEXT_PLAIN).build());
     }
}
