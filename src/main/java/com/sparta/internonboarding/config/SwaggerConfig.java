package com.sparta.internonboarding.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("intern-onboarding")
                .description("한달인턴8기 온보딩 과제");

        return new OpenAPI()
                .info(info)
                .components(new Components());
    }
}
