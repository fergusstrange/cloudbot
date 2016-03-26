package io.cloudbot.aws.keypair;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.CreateKeyPairRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairResult;
import com.amazonaws.services.ec2.model.KeyPair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class KeyPairGenerationServiceTest {

    @Mock
    private AmazonEC2 amazonEC2;
    @Mock
    private CacheManager cacheManager;

    @InjectMocks
    private KeyPairGenerationService keyPairGenerationService;

    @Test
    public void shouldCreateKeyPairCacheAndReturn() throws Exception {
        CreateKeyPairResult createKeyPairResult = mock(CreateKeyPairResult.class);
        Cache cache = mock(Cache.class);
        KeyPair mockKeyPair = new KeyPair().withKeyName("abcdefg12345");

        given(amazonEC2.createKeyPair(new CreateKeyPairRequest(anyString()))).willReturn(createKeyPairResult);
        given(createKeyPairResult.getKeyPair()).willReturn(mockKeyPair);
        given(cacheManager.getCache("awsKeyPair")).willReturn(cache);

        String keyPair = keyPairGenerationService.generateNewKey();

        assertThat(keyPair, is("abcdefg12345"));
    }
}