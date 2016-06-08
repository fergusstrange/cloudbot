package io.cloudbot.aws;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.*;
import io.cloudbot.aws.keypair.KeyPairRetrievalUrlFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class EC2InstanceCreationResultFactoryTest {

    @Mock
    private KeyPairRetrievalUrlFactory keyPairRetrievalUrlFactory;
    @Mock
    private AmazonEC2 amazonEC2;

    @InjectMocks
    private EC2InstanceCreationResultFactory ec2InstanceCreationResultFactory;

    @Test
    public void shouldReturnPopulatedEC2InstanceCreationResult() throws Exception {
        Instance instance = mock(Instance.class);
        List<Instance> instances = singletonList(instance);
        DescribeInstancesResult describeInstancesResult = mock(DescribeInstancesResult.class);
        Reservation reservation = mock(Reservation.class);

        given(amazonEC2.describeInstances(new DescribeInstancesRequest().withInstanceIds(
                instances.stream().map(Instance::getInstanceId).collect(toList()))))
                .willReturn(describeInstancesResult);

        given(describeInstancesResult.getReservations()).willReturn(singletonList(reservation));
        given(reservation.getInstances()).willReturn(instances);
        given(instance.getState()).willReturn(new InstanceState().withCode(0),
                new InstanceState().withCode(0),
                new InstanceState().withCode(16));

        given(keyPairRetrievalUrlFactory.create("aKey")).willReturn("http://127.0.0.1:8080/keyPair/aKey");
        given(instance.getPublicIpAddress()).willReturn("127.0.0.1");

        EC2InstanceCreationResult ec2InstanceCreationResult = ec2InstanceCreationResultFactory.create("aKey", instances);

        assertThat(ec2InstanceCreationResult, is(new EC2InstanceCreationResult("http://127.0.0.1:8080/keyPair/aKey", singletonList("127.0.0.1"))));
    }
}