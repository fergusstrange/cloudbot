package io.cloudbot.aws;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.Instance;
import io.cloudbot.aws.keypair.KeyPairRetrievalUrlFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.lang.Thread.sleep;
import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public class EC2InstanceCreationResultFactory {

    private static final Logger logger = getLogger(EC2InstanceCreationResultFactory.class);

    private final KeyPairRetrievalUrlFactory keyPairRetrievalUrlFactory;
    private final AmazonEC2 amazonEC2;

    @Autowired
    public EC2InstanceCreationResultFactory(KeyPairRetrievalUrlFactory keyPairRetrievalUrlFactory,
                                            AmazonEC2 amazonEC2) {
        this.keyPairRetrievalUrlFactory = keyPairRetrievalUrlFactory;
        this.amazonEC2 = amazonEC2;
    }

    public EC2InstanceCreationResult create(String keyName, List<Instance> instances) {

        try {
            int tries = 120;
            while (amazonEC2.describeInstances(new DescribeInstancesRequest().withInstanceIds(
                    instances.stream().map(Instance::getInstanceId).collect(toList())))
                    .getReservations()
                    .stream()
                    .flatMap(reservation -> reservation.getInstances().stream())
                    .allMatch(instance -> instance.getState().getCode() != 16) && tries > 0) {
                tries--;
                sleep(1000L);
            }
        } catch (InterruptedException e) {
            logger.error("Interrupted whilst waiting for public IP address", e);
        }

        List<String> publicIPs = amazonEC2.describeInstances(new DescribeInstancesRequest().withInstanceIds(
                instances.stream().map(Instance::getInstanceId).collect(toList())))
                .getReservations()
                .stream()
                .flatMap(reservation -> reservation.getInstances().stream())
                .map(Instance::getPublicIpAddress)
                .collect(toList());

        return new EC2InstanceCreationResult(
                keyPairRetrievalUrlFactory.create(keyName),
                publicIPs);
    }
}
