package io.cloudbot.aws;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.CreateTagsRequest;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Placement;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import io.cloudbot.aws.keypair.KeyPairGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.amazonaws.services.ec2.model.InstanceType.T2Nano;
import static java.util.stream.Collectors.toList;

@Component
public class EC2InstanceCreationService {

    private final AWSEnvironment awsEnvironment;
    private final AmazonEC2 client;
    private final KeyPairGenerationService keyPairGenerationService;
    private final EC2TagFactory ec2TagFactory;
    private final EC2InstanceCreationResultFactory ec2InstanceCreationResultFactory;

    @Autowired
    public EC2InstanceCreationService(AWSEnvironment awsEnvironment,
                                      AmazonEC2 client,
                                      KeyPairGenerationService keyPairGenerationService,
                                      EC2TagFactory ec2TagFactory, EC2InstanceCreationResultFactory ec2InstanceCreationResultFactory) {
        this.awsEnvironment = awsEnvironment;
        this.client = client;
        this.keyPairGenerationService = keyPairGenerationService;
        this.ec2TagFactory = ec2TagFactory;
        this.ec2InstanceCreationResultFactory = ec2InstanceCreationResultFactory;
    }

    public EC2InstanceCreationResult createInstance(SlackMessagePosted event) {
        String keyName = keyPairGenerationService.generateNewKey();

        List<Instance> instances = client.runInstances(new RunInstancesRequest()
                .withImageId(awsEnvironment.getAwsDefaultImageId())
                .withInstanceType(T2Nano)
                .withMinCount(1)
                .withMaxCount(1)
                .withKeyName(keyName)
                .withSecurityGroups(awsEnvironment.getAwsDefaultSecurityGroup())
                .withPlacement(new Placement(awsEnvironment.getAwsDefaultPlacement())))
                .getReservation()
                .getInstances();

        List<String> instanceIds = instances.stream()
                .map(Instance::getInstanceId)
                .collect(toList());

        client.createTags(new CreateTagsRequest(instanceIds, ec2TagFactory.create(event.getSender().getUserName(), keyName)));

        return ec2InstanceCreationResultFactory.create(keyName, instances);
    }
}
