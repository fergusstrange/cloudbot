package io.cloudbot.aws.keypair;

import com.amazonaws.services.ec2.model.KeyPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import static io.cloudbot.aws.keypair.KeyPairCacheConfiguration.awsKeyPairCache;
import static java.util.Optional.ofNullable;

@Component
public class KeyPairRetrievalService {

    private final CacheManager cacheManager;

    @Autowired
    public KeyPairRetrievalService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public String getSecretKey(String keyName) {
        return ofNullable(cacheManager.getCache(awsKeyPairCache).get(keyName, KeyPair.class))
                .map(KeyPair::getKeyMaterial)
                .orElse(null);
    }

}
