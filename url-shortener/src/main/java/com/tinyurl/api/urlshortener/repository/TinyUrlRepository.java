package com.tinyurl.api.urlshortener.repository;

import com.tinyurl.api.urlshortener.entity.ShortUrl;
import com.tinyurl.api.urlshortener.entity.TinyUrl;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TinyUrlRepository extends CrudRepository<TinyUrl, String> {

    TinyUrl findByKey(String key);

    TinyUrl findByLongUrl(String longUrl);
}
