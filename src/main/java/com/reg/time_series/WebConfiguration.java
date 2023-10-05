package com.reg.time_series;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfiguration {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        //noinspection NullableProblems
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        .addMapping("/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }

    @Bean
    public OpenAPI dashboardOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TimeSeries API")
                        .description("API documentation for TimeSeries project.")
                        .version("1.0.0")
                        .license(new License().name("MIT"))
                        .contact(new Contact().name("Mark Mikus").url("https://github.com/kyussfia").email("kyussfia@gmail.com"))
                );
    }
}
