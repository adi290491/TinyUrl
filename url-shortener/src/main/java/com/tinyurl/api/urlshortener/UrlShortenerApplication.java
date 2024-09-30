package com.tinyurl.api.urlshortener;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class UrlShortenerApplication {

	private MongoDatabaseFactory mongoDatabaseFactory;

	public static void main(String[] args) {
		SpringApplication.run(UrlShortenerApplication.class, args);
	}

	@Bean
	public ModelMapper getModelMapper() {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return mapper;
	}

}
