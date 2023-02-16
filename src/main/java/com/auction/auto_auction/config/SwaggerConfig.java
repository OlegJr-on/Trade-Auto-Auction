package com.auction.auto_auction.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;


@Configuration
@OpenAPIDefinition(info = @Info(title = "Auction REST API",
                                version = "1.0",
                                description = "Auto Auction REST API`s documentation"))
public class SwaggerConfig {
}
