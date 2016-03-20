package de.twiechert.roleexample.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Tayfun Wiechert <tayfun.wiechert@gmail.com>
 */
@Configuration
@EntityScan(basePackages = {"de.twiechert.roleexample.model"})
@EnableJpaRepositories(basePackages = {"de.twiechert.roleexample.dataaccess"})
public class ApplicationConfiguration {

    @Value("${server.port}")
    private String applicationHttpPort;

    @Value("${spring.data.rest.basePath}")
    private String restBasePath;

    public String getApplicationHttpPort() {
        return applicationHttpPort;
    }

    public String getRestBasePath() {
        return restBasePath;
    }
}
