package io.cloudbot.aws;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.*;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import io.cloudbot.aws.keypair.KeyPairGenerationService;
import io.cloudbot.aws.keypair.KeyPairRetrievalUrlFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.amazonaws.services.ec2.model.InstanceType.T2Micro;

@Component
public class EC2InstanceCreationService {

    private final AWSEnvironment awsEnvironment;
    private final AmazonEC2 client;
    private final KeyPairGenerationService keyPairGenerationService;
    private final EC2TagFactory ec2TagFactory;
    private final KeyPairRetrievalUrlFactory keyPairRetrievalUrlFactory;

    @Autowired
    public EC2InstanceCreationService(AWSEnvironment awsEnvironment,
                                      AmazonEC2 client,
                                      KeyPairGenerationService keyPairGenerationService,
                                      EC2TagFactory ec2TagFactory,
                                      KeyPairRetrievalUrlFactory keyPairRetrievalUrlFactory) {
        this.awsEnvironment = awsEnvironment;
        this.client = client;
        this.keyPairGenerationService = keyPairGenerationService;
        this.ec2TagFactory = ec2TagFactory;
        this.keyPairRetrievalUrlFactory = keyPairRetrievalUrlFactory;
    }

    public String createInstance(SlackMessagePosted event) {
        String keyName = keyPairGenerationService.generateNewKey();

        List<String> instanceIds = client.runInstances(new RunInstancesRequest()
                .withImageId(awsEnvironment.getAwsDefaultImageId())
                .withInstanceType(T2Micro)
                .withMinCount(1)
                .withMaxCount(1)
                .withKeyName(keyName)
                .withSecurityGroups(awsEnvironment.getAwsDefaultSecurityGroup())
                .withPlacement(new Placement(awsEnvironment.getAwsDefaultPlacement())))
                .getReservation()
                .getInstances()
                .stream()
                .map(Instance::getInstanceId)
                .collect(Collectors.toList());

        client.createTags(new CreateTagsRequest(instanceIds, ec2TagFactory.create(event.getSender().getUserName(), keyName)));

        return keyPairRetrievalUrlFactory.create(keyName);
    }
}
