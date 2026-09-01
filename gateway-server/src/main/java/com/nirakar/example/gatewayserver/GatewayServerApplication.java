package com.nirakar.example.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class GatewayServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServerApplication.class, args);
	}


	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		log.info("Initializing custom route locator");
		return builder.routes()
				.route("aiAssistantService", p -> p.path("/myapp/aiassistant/**")
						.filters(f -> f.rewritePath("/myapp/aiassistant/(?<segment>.*)", "/${segment}"))
						.uri("lb://AI-ASSISTANT-SERVICE"))
				.route("userService", p -> p.path("/myapp/user/**")
						.filters(f -> f.rewritePath("/myapp/user/(?<segment>.*)", "/${segment}"))
						.uri("lb://USER-SERVICE"))
				.build();
	}
}
