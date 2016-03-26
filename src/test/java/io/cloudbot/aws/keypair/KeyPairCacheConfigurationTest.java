package io.cloudbot.aws.keypair;

import org.junit.Test;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleCacheManager;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

public class KeyPairCacheConfigurationTest {

    private final KeyPairCacheConfiguration keyPairCacheConfiguration = new KeyPairCacheConfiguration();

    @Test
    public void shouldReturnCacheManager() throws Exception {
        CacheManager cacheManager = keyPairCacheConfiguration.cacheManager();
        ((SimpleCacheManager) cacheManager).afterPropertiesSet();

        assertThat(cacheManager.getCacheNames(), hasItem("awsKeyPair"));
    }
}