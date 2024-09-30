package com.tinyurl.api.urlshortener.service;

import com.tinyurl.api.urlshortener.model.UrlDTO;
import org.springframework.stereotype.Service;

public interface UrlShortenerService {

    public UrlDTO shortenUrl(String longUrl);

    String getLongUrl(String shortUrl);
}
