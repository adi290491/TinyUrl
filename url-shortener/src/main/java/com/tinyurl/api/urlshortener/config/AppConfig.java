package com.tinyurl.api.urlshortener.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    public @Bean MongoClient mongoClient() {
        return MongoClients.create("mongodb+srv://aditya-sawant:S10dulkar@shorturl.hwzsv.mongodb.net/?retryWrites=true&w=majority&appName=ShortUrl");
    }
}
