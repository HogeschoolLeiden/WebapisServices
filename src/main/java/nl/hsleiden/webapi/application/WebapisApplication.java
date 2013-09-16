/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.hsleiden.webapi.application;

import nl.hsleiden.webapi.util.ObjectMapperProvider;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author hl
 */
public class WebapisApplication extends ResourceConfig {
    public WebapisApplication() {
        packages("nl.hsleiden.webapi.v1.service");
        packages("org.glassfish.jersey.examples.jackson");
        register(ObjectMapperProvider.class);
        register(JacksonFeature.class);
    }
}    

