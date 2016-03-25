package io.cloudbot.aws;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.InstanceStateChange;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class EC2InstanceTerminationServiceTest {

    @Mock
    private AmazonEC2 amazonEC2;

    @InjectMocks
    private EC2InstanceTerminationService ec2InstanceTerminationService;

    @Test
    public void shouldTerminateInstanceReturningInstanceId() throws Exception {
        TerminateInstancesResult terminateInstancesResult = mock(TerminateInstancesResult.class);
        InstanceStateChange instanceStateChange = mock(InstanceStateChange.class);


        given(amazonEC2.terminateInstances(new TerminateInstancesRequest(singletonList("abcdefg123456")))).willReturn(terminateInstancesResult);
        given(terminateInstancesResult.getTerminatingInstances()).willReturn(singletonList(instanceStateChange));
        given(instanceStateChange.getInstanceId()).willReturn("abcdefg123456");

        List<String> instanceIds = ec2InstanceTerminationService.terminateInstance("abcdefg123456");

        assertThat(instanceIds, hasItem("abcdefg123456"));
    }
}