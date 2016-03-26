package io.cloudbot.aws.keypair;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

import static java.util.Collections.singletonList;
import static java.util.concurrent.TimeUnit.MINUTES;

@Configuration
@EnableCaching
public class KeyPairCacheConfiguration extends CachingConfigurerSupport {

    static final String awsKeyPairCache = "awsKeyPair";

    @Bean
    @Override
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(caches());
        return cacheManager;
    }

    private static Collection<? extends org.springframework.cache.Cache> caches() {
        return singletonList(new GuavaCache(awsKeyPairCache, CacheBuilder.newBuilder().expireAfterWrite(5, MINUTES).build()));
    }
}
