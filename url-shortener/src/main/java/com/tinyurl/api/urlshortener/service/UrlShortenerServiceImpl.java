package com.tinyurl.api.urlshortener.service;

import com.tinyurl.api.urlshortener.entity.ShortUrl;
import com.tinyurl.api.urlshortener.entity.TinyUrl;
import com.tinyurl.api.urlshortener.exception.UrlException;
import com.tinyurl.api.urlshortener.model.UrlDTO;
import com.tinyurl.api.urlshortener.repository.ShortUrlRepository;
import com.tinyurl.api.urlshortener.repository.TinyUrlRepository;
import com.tinyurl.api.urlshortener.util.UrlUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.ObjectHashMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UrlShortenerServiceImpl implements UrlShortenerService{


    private static final Logger log = LoggerFactory.getLogger(UrlShortenerServiceImpl.class);
    private final UrlUtil urlUtil;
    private final ShortUrlRepository shortUrlRepository;
    private final ModelMapper modelMapper;
    private final RedisTemplate<String, String> redisTemplate;
//    private TinyUrlRepository tinyUrlRepository;

    @Value("${spring.redis.shorten_prefix}")
    private String longToShortPrefix;

    @Value("${spring.redis.redirect_prefix}")
    private String redirectPrefix;

    @Autowired
    public UrlShortenerServiceImpl(UrlUtil urlUtil, ShortUrlRepository shortUrlRepository, ModelMapper modelMapper, RedisTemplate<String, String> redisTemplate) {
        this.urlUtil = urlUtil;
        this.shortUrlRepository = shortUrlRepository;
        this.modelMapper = modelMapper;
        this.redisTemplate = redisTemplate;
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


    public UrlDTO shortenUrlV2(String longUrl) {
        log.info("Shortening URL - Version 2: {}", longUrl);
        UrlDTO urlDTO = null;

        String shortUrl = redisTemplate.opsForValue().get(longToShortPrefix + longUrl);
        if (shortUrl == null) {
            urlDTO =  urlUtil.generateShortUrl(longUrl);

            redisTemplate.opsForValue().set(longToShortPrefix + longUrl, urlDTO.getShortUrl());
            redisTemplate.opsForValue().set(redirectPrefix + urlDTO.getKey(), longUrl);

        } else {
            var key = shortUrl.substring(shortUrl.length() - 7);

            urlDTO = new UrlDTO(key, longUrl, shortUrl, false);
        }
        return urlDTO;
    }

    @Override
    public String getLongUrlV2(String shortUrl) {
        log.info("Getting long URL for short URL: {}", shortUrl);

        return redisTemplate.opsForValue().get(redirectPrefix + shortUrl);
    }

    @Override
    public boolean deleteShortUrl(String shortUrl) {

        String longUrl = redisTemplate.opsForValue().get(redirectPrefix + shortUrl);
        if (longUrl == null) {
            return false;
        }

        redisTemplate.delete(redirectPrefix + shortUrl);

        return Boolean.TRUE.equals(redisTemplate.delete(longToShortPrefix + longUrl));
    }
}
