/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.hsleiden.webapi.exception;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import nl.hsleiden.webapi.util.ValidationException;



@Provider

public class ValidationErrorHandler
        implements ExceptionMapper<ValidationException> {
    @Override
    public javax.ws.rs.core.Response toResponse(ValidationException ve) {
        
        return Response.status(400).type(MediaType.TEXT_PLAIN)
                .entity(ve).build();
    }
}
