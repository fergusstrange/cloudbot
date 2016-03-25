package io.cloudbot.aws;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.amazonaws.services.ec2.model.InstanceType.T2Micro;

@Component
public class EC2InstanceCreationService {

    private final AWSEnvironment awsEnvironment;
    private final AmazonEC2 client;

    @Autowired
    public EC2InstanceCreationService(AWSEnvironment awsEnvironment, AmazonEC2 client) {
        this.awsEnvironment = awsEnvironment;
        this.client = client;
    }

    public List<String> createInstance(List<Tag> tags) {
        List<String> instanceIds = client
                .runInstances(new RunInstancesRequest()
                        .withImageId(awsEnvironment.getAwsDefaultImageId())
                        .withInstanceType(T2Micro)
                        .withMinCount(1)
                        .withMaxCount(1)
                        .withSecurityGroups(awsEnvironment.getAwsDefaultSecurityGroup())
                        .withPlacement(new Placement(awsEnvironment.getAwsDefaultPlacement())))
                .getReservation()
                .getInstances()
                .stream()
                .map(Instance::getInstanceId)
                .collect(Collectors.toList());

        client.createTags(new CreateTagsRequest(instanceIds, tags));

        return instanceIds;
    }
}
