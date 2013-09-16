/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.hsleiden.webapi.util;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;

/**
 *
 * @author hl
 */
@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {
 
    final ObjectMapper defaultObjectMapper;
    //final ObjectMapper combinedObjectMapper;
 
    public ObjectMapperProvider() {
        defaultObjectMapper = createDefaultMapper();
        //combinedObjectMapper = createCombinedObjectMapper();
    }
 
    @Override
    public ObjectMapper getContext(Class<?> type) {
        
            return defaultObjectMapper;
  
    }
 
    private static ObjectMapper createDefaultMapper() {
        final ObjectMapper result = new ObjectMapper();
        result.configure(Feature.INDENT_OUTPUT, true);
 
        return result;
    }
  
}
