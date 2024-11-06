package com.tinyurl.api.urlshortener.service;

import com.tinyurl.api.urlshortener.model.UrlDTO;
import org.springframework.stereotype.Service;

public interface UrlShortenerService {

    public UrlDTO shortenUrl(String longUrl);

    UrlDTO shortenUrlV2(String longUrl);

    String getLongUrl(String shortUrl);

    String getLongUrlV2(String shortUrl);

    boolean deleteShortUrl(String shortUrl);
}
