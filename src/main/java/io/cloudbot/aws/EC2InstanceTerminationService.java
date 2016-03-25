package io.cloudbot.aws;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.InstanceStateChange;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@Component
public class EC2InstanceTerminationService {

    private final AmazonEC2 client;

    @Autowired
    public EC2InstanceTerminationService(AmazonEC2 client) {
        this.client = client;
    }

    public List<String> terminateInstance(String instanceId) {
        return client.terminateInstances(new TerminateInstancesRequest(singletonList(instanceId)))
                .getTerminatingInstances()
                .stream()
                .map(InstanceStateChange::getInstanceId)
                .collect(toList());
    }
}
