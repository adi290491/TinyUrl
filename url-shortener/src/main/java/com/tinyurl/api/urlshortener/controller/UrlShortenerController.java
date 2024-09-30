package com.tinyurl.api.urlshortener.controller;

import com.tinyurl.api.urlshortener.exception.UrlException;
import com.tinyurl.api.urlshortener.model.UrlDTO;
import com.tinyurl.api.urlshortener.model.UrlRequestModel;
import com.tinyurl.api.urlshortener.service.UrlShortenerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    @Autowired
    public UrlShortenerController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }
    //take longUrl as string input
    @PostMapping(
            value = "/v1/shorten",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<String> shortenUrlMongo(@Valid @RequestBody UrlRequestModel urlRequestModel) {

        UrlDTO urlDTO =  urlShortenerService.shortenUrl(urlRequestModel.getLongUrl());
        HttpStatus status = null;
        if (!urlDTO.isNew()) {
            status = HttpStatus.CONFLICT;
        } else {
            status = HttpStatus.CREATED;
        }
        return new ResponseEntity<>(urlDTO.getShortUrl(), status);
    }

    @GetMapping(
            value = "/v1/{shorturl}"
    )
    public ResponseEntity<?> redirectToLongUrlMongo(@PathVariable(name="shorturl") String shortUrl) {

        // TODO: implement actual redirection logic
        String longUrl = urlShortenerService.getLongUrl(shortUrl);

        if (longUrl != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", longUrl);

            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>("URL not found", HttpStatus.NOT_FOUND);
        }

    }

}
