package com.tinyurl.api.urlshortener.service;

import com.tinyurl.api.urlshortener.entity.ShortUrl;
import com.tinyurl.api.urlshortener.exception.UrlException;
import com.tinyurl.api.urlshortener.model.UrlDTO;
import com.tinyurl.api.urlshortener.repository.ShortUrlRepository;
import com.tinyurl.api.urlshortener.util.UrlUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UrlShortenerServiceImpl implements UrlShortenerService{


    private static final Logger log = LoggerFactory.getLogger(UrlShortenerServiceImpl.class);
    private final UrlUtil urlUtil;
    private final ShortUrlRepository shortUrlRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UrlShortenerServiceImpl(UrlUtil urlUtil, ShortUrlRepository shortUrlRepository, ModelMapper modelMapper) {
        this.urlUtil = urlUtil;
        this.shortUrlRepository = shortUrlRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UrlDTO shortenUrl(String longUrl) {

        log.info("Shortening URL: {}", longUrl);
        UrlDTO urlDTO = null;

        ShortUrl shortUrlEntity = shortUrlRepository.findByLongUrl(longUrl);

        if (shortUrlEntity == null) {
            urlDTO = urlUtil.generateShortUrl(longUrl);

            shortUrlEntity = modelMapper.map(urlDTO, ShortUrl.class);

            try {
                shortUrlRepository.save(shortUrlEntity);
            } catch (Exception e) {
                throw new UrlException("Database error while saving short URL.", HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } else {
            urlDTO = new UrlDTO(shortUrlEntity.getKey(), shortUrlEntity.getLongUrl(), shortUrlEntity.getShortUrl(), false);
        }

        return urlDTO;
    }

    @Override
    public String getLongUrl(String shortUrl) {
        log.info("Getting long URL for short URL: {}", shortUrl);
        try {
            ShortUrl shortUrlEntity = shortUrlRepository.findByKey(shortUrl);

            return shortUrlEntity != null ? shortUrlEntity.getLongUrl() : null;
        } catch (Exception e) {
            throw new UrlException("Database error while fetching long URL.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
