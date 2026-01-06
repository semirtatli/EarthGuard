package com.earthguard.earthquake.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8081}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("EarthGuard API")
                        .version("1.0.0")
                        .description("""
                            # EarthGuard - Earthquake Early Warning System
                            
                            A comprehensive earthquake monitoring and alert system with real-time notifications.
                            
                            ## Features
                            - 🌍 Real-time earthquake data from USGS
                            - 🔔 Instant notifications via WebSocket
                            - 📧 Email alerts for critical earthquakes
                            - 🗺️ Location-based filtering
                            - 📊 Advanced search and pagination
                            - 🔒 JWT authentication
                            - 🚀 Redis caching for performance
                            - 📈 Rate limiting protection
                            
                            ## Authentication
                            Most endpoints require JWT authentication. To get started:
                            1. Register a new account at `POST /api/auth/register`
                            2. Login at `POST /api/auth/login` to get your JWT token
                            3. Click the 'Authorize' button and enter your token
                            4. You can now access protected endpoints
                            
                            ## Rate Limiting
                            - Public endpoints: 10 requests/second per IP
                            - Authenticated endpoints: 20 requests/second per user
                            """)
                        .contact(new Contact()
                                .name("EarthGuard Team")
                                .email("support@earthguard.com")
                                .url("https://github.com/earthguard/earthguard"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Development Server"),
                        new Server()
                                .url("http://localhost:8080")
                                .description("API Gateway")
                ))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter your JWT token obtained from /api/auth/login")));
    }
}