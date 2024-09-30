package com.tinyurl.api.urlshortener.repository;

import com.tinyurl.api.urlshortener.entity.ShortUrl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortUrlRepository extends MongoRepository<ShortUrl, Long> {

    ShortUrl findByKey(String key);

    ShortUrl findByLongUrl(String longUrl);
}
