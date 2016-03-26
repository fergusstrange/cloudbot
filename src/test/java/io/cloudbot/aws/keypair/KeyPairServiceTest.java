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
public class KeyPairServiceTest {

    @Mock
    private AmazonEC2 amazonEC2;
    @Mock
    private CacheManager cacheManager;
    @Mock
    private KeyPairRetrievalUrlFactory keyPairRetrievalUrlFactory;

    @InjectMocks
    private KeyPairService keyPairService;

    @Test
    public void shouldCreateKeyPairCacheAndReturn() throws Exception {
        CreateKeyPairResult createKeyPairResult = mock(CreateKeyPairResult.class);
        Cache cache = mock(Cache.class);
        KeyPair mockKeyPair = new KeyPair().withKeyName("abcdefg12345");

        given(amazonEC2.createKeyPair(new CreateKeyPairRequest(anyString()))).willReturn(createKeyPairResult);
        given(createKeyPairResult.getKeyPair()).willReturn(mockKeyPair);
        given(cacheManager.getCache("awsKeyPair")).willReturn(cache);
        given(keyPairRetrievalUrlFactory.create(mockKeyPair)).willReturn("http://127.0.0.1:8080/keyPair/abcdefg123456");

        String keyPair = keyPairService.generateNewKey();

        assertThat(keyPair, is("http://127.0.0.1:8080/keyPair/abcdefg123456"));
    }
}