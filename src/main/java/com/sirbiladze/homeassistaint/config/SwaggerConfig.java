package com.sirbiladze.homeassistaint.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.filter.ForwardedHeaderFilter;

@Configuration
public class SwaggerConfig {

  @Bean
  ForwardedHeaderFilter forwardedHeaderFilter() {
    return new ForwardedHeaderFilter();
  }

  @Bean
  @Profile("!default")
  public OpenAPI openAPI(@Value("${spring.application.name}") String baseUrl) {
    return new OpenAPI().addServersItem(new Server().url("/" + baseUrl));
  }

}
