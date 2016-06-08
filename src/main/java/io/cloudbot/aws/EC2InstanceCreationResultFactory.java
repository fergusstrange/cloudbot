package io.cloudbot.aws;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.Instance;
import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import io.cloudbot.aws.keypair.KeyPairRetrievalUrlFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.github.rholder.retry.WaitStrategies.fixedWait;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.toList;

@Component
public class EC2InstanceCreationResultFactory {

    private final KeyPairRetrievalUrlFactory keyPairRetrievalUrlFactory;
    private final AmazonEC2 amazonEC2;

    @Autowired
    public EC2InstanceCreationResultFactory(KeyPairRetrievalUrlFactory keyPairRetrievalUrlFactory,
                                            AmazonEC2 amazonEC2) {
        this.keyPairRetrievalUrlFactory = keyPairRetrievalUrlFactory;
        this.amazonEC2 = amazonEC2;
    }

    EC2InstanceCreationResult create(String keyName, List<Instance> instances) {
        if(instancesStarted(instances)) {
            return new EC2InstanceCreationResult(keyPairRetrievalUrlFactory.create(keyName), publicIPs(describeInstancesRequest(instances)));
        }
        throw new RuntimeException();
    }

    private Boolean instancesStarted(List<Instance> instances) {
        try {
            return RetryerBuilder.<Boolean>newBuilder()
                    .retryIfException()
                    .retryIfResult(input -> input.equals(false))
                    .withWaitStrategy(fixedWait(1L, SECONDS))
                    .withStopStrategy(StopStrategies.stopAfterDelay(2L, MINUTES))
                    .build()
                    .call(() -> amazonEC2.describeInstances(describeInstancesRequest(instances))
                            .getReservations()
                            .stream()
                            .flatMap(reservation -> reservation
                                    .getInstances()
                                    .stream())
                            .allMatch(instance -> instance.getState().getCode() == 16));
        } catch (ExecutionException | RetryException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> publicIPs(DescribeInstancesRequest describeInstancesRequest) {
        return amazonEC2.describeInstances(describeInstancesRequest)
                .getReservations()
                .stream()
                .flatMap(reservation -> reservation.getInstances().stream())
                .map(Instance::getPublicIpAddress)
                .collect(toList());
    }

    private static DescribeInstancesRequest describeInstancesRequest(List<Instance> instances) {
        return new DescribeInstancesRequest().withInstanceIds(instances.stream().map(Instance::getInstanceId).collect(toList()));
    }
}
