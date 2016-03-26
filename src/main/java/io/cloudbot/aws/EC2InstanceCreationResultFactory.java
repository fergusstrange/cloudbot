package io.cloudbot.aws;

import com.amazonaws.services.ec2.model.Instance;
import io.cloudbot.aws.keypair.KeyPairRetrievalUrlFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class EC2InstanceCreationResultFactory {

    private final KeyPairRetrievalUrlFactory keyPairRetrievalUrlFactory;

    @Autowired
    public EC2InstanceCreationResultFactory(KeyPairRetrievalUrlFactory keyPairRetrievalUrlFactory) {
        this.keyPairRetrievalUrlFactory = keyPairRetrievalUrlFactory;
    }

    public EC2InstanceCreationResult create(String keyName, List<Instance> instances) {
        return new EC2InstanceCreationResult(
                keyPairRetrievalUrlFactory.create(keyName),
                instances.stream().map(Instance::getPublicIpAddress).collect(toList()));
    }
}
