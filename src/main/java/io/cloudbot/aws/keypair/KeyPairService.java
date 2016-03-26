package io.cloudbot.aws.keypair;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.CreateKeyPairRequest;
import com.amazonaws.services.ec2.model.KeyPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import static java.util.UUID.randomUUID;

@Component
public class KeyPairService {

    private final AmazonEC2 amazonEC2;
    private final CacheManager cacheManager;

    @Autowired
    public KeyPairService(AmazonEC2 amazonEC2,
                          CacheManager cacheManager) {
        this.amazonEC2 = amazonEC2;
        this.cacheManager = cacheManager;
    }

    public KeyPair generateNewKey() {
        KeyPair keyPair = amazonEC2.createKeyPair(new CreateKeyPairRequest(randomUUID().toString())).getKeyPair();
        cacheManager.getCache("awsKeyPair").put(keyPair.getKeyName(), keyPair);
        return keyPair;
    }
}
