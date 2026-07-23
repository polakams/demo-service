package com.polakams.demoservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot entry point for the Library System demo service.
 */
@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Library System",
                description = "REST API for a simple library book catalog. "
                        + "Use this service to learn Spring Boot layering, validation, and OpenAPI docs.",
                version = "1.0.0"
        )
)
public class DemoServiceApplication {

    /**
     * Boots the application and starts the embedded web server.
     *
     * @param args command-line arguments forwarded to Spring Boot
     */
    public static void main(String[] args) {
        SpringApplication.run(DemoServiceApplication.class, args);
    }
}
