package io.cloudbot.aws.keypair;

import com.amazonaws.services.ec2.model.KeyPair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class KeyPairRetrievalServiceTest {

    @Mock
    private CacheManager cacheManager;

    @InjectMocks
    private KeyPairRetrievalService keyPairRetrievalService;

    @Test
    public void shouldReturnCachedKey() throws Exception {
        Cache cache = mock(Cache.class);

        given(cacheManager.getCache("awsKeyPair")).willReturn(cache);
        given(cache.get("abc123", KeyPair.class)).willReturn(new KeyPair().withKeyMaterial("qwerty"));

        String secretKey = keyPairRetrievalService.getSecretKey("abc123");

        assertThat(secretKey, is("qwerty"));
    }

    @Test
    public void shouldReturnNullIfCacheNotExists() throws Exception {
        Cache cache = mock(Cache.class);

        given(cacheManager.getCache("awsKeyPair")).willReturn(cache);
        given(cache.get("abc123", KeyPair.class)).willReturn(null);

        String secretKey = keyPairRetrievalService.getSecretKey("abc123");

        assertThat(secretKey, nullValue());
    }
}