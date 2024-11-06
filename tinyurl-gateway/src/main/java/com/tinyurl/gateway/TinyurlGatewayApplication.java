package com.tinyurl.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.NoSuchAlgorithmException;

@SpringBootApplication
public class TinyurlGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(TinyurlGatewayApplication.class, args);
	}

	@Bean
	@LoadBalanced
	public WebClient.Builder loadWebClientBuilder() {
		return WebClient.builder();
	}

//	@Bean
	/*public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder, AuthorizationHeaderGatewayFilterFactory authorizationHeaderGatewayFilterFactory ) throws NoSuchAlgorithmException {

		AuthorizationHeaderGatewayFilterFactory.Config config = new AuthorizationHeaderGatewayFilterFactory.Config();
		config.setAlgorithm("SHA-256");
		return routeLocatorBuilder.routes()
				.route("tinyurl-shorten", p -> p.path("/url-shortener/api/v1/shorten")
						.and()
						.method(HttpMethod.POST)
						.filters(f -> f
								.removeRequestHeader("Cookie")
								.rewritePath("/url-shortener/?(?<segment>.*)", "/$\\{segment})")
								.filter(authorizationHeaderGatewayFilterFactory.apply(config)))
						.uri("lb://url-shortener"))
				.route("tinyurl-redirect", p -> p.path("/url-shortener/api/v1/**")
						.and()
						.method(HttpMethod.GET)
						.filters(f -> f
								.removeRequestHeader("Cookie")
								.rewritePath("/url-shortener/?(?<segment>.*)", "/$\\{segment})")
								.filter(authorizationHeaderGatewayFilterFactory.apply(config)))
						.uri("lb://url-shortener"))
						.build();
	}*/
}
