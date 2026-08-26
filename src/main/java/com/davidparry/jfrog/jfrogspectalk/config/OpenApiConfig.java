package com.davidparry.jfrog.jfrogspectalk.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI contactManagementOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Contact Management API")
                .version("v1")
                .description("""
                        CRUD, search and pagination over contacts, backed by an in-memory H2 database.
                        Errors are returned as RFC 9457 problem details.
                        """)
                .license(new License().name("Apache 2.0")));
    }
}
