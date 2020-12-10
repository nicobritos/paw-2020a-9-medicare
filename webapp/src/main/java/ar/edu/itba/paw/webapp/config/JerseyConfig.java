package ar.edu.itba.paw.webapp.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        this.property(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, true);
    }
}