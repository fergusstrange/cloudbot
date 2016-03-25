package io.cloudbot.aws;

import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.*;
import org.hamcrest.CoreMatchers;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static com.amazonaws.services.ec2.model.InstanceType.T2Micro;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class EC2InstanceCreationServiceTest {

    @Mock
    private AmazonEC2Client amazonEC2;
    @Mock
    private AWSEnvironment awsEnvironment;

    @InjectMocks
    private EC2InstanceCreationService ec2InstanceCreationService;

    @Test
    public void shouldCallAmazonAPIWithCorrectValues() throws Exception {
        RunInstancesResult runInstancesResult = mock(RunInstancesResult.class);
        Reservation reservation = mock(Reservation.class);
        Instance instance = mock(Instance.class);

        given(amazonEC2.runInstances(eq(new RunInstancesRequest()
                .withImageId(awsEnvironment.getAwsDefaultImageId())
                .withInstanceType(T2Micro)
                .withMinCount(1)
                .withMaxCount(1)
                .withSecurityGroups(awsEnvironment.getAwsDefaultSecurityGroup())
                .withPlacement(new Placement(awsEnvironment.getAwsDefaultPlacement()))))).willReturn(runInstancesResult);

        given(runInstancesResult.getReservation()).willReturn(reservation);
        given(reservation.getInstances()).willReturn(Collections.singletonList(instance));
        given(instance.getInstanceId()).willReturn("abcdefg123456");

        List<String> instanceIds = ec2InstanceCreationService.createInstance(asList(new Tag("Name", "Anything"), new Tag("ShutdownDate", LocalDateTime.now().toString())));

        assertThat(instanceIds, CoreMatchers.hasItem("abcdefg123456"));
    }
}