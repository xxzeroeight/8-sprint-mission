package com.sprint.mission.discodeit.global.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Configuration
@EnableCaching
public class CacheConfig
{
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();

        simpleCacheManager.setCaches(List.of(
                setCache("channels", 500, Duration.ofMinutes(5)),
                setCache("users", 500, Duration.ofMinutes(30)),
                setCache("notifications", 500, Duration.ofSeconds(30))
        ));

        return simpleCacheManager;
    }

    private CaffeineCache setCache(String name, long maxSize, Duration ttl) {
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(ttl)
                .recordStats();

        return new CaffeineCache(name, caffeine.build());
    }
}
