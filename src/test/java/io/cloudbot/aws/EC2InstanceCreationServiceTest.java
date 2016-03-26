package io.cloudbot.aws;

import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.*;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import io.cloudbot.aws.keypair.KeyPairGenerationService;
import io.cloudbot.aws.keypair.KeyPairRetrievalUrlFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.amazonaws.services.ec2.model.InstanceType.T2Micro;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
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
    @Mock
    private KeyPairGenerationService keyPairGenerationService;
    @Mock
    private EC2TagFactory ec2TagFactory;
    @Mock
    private KeyPairRetrievalUrlFactory keyPairRetrievalUrlFactory;

    @InjectMocks
    private EC2InstanceCreationService ec2InstanceCreationService;

    @Test
    public void shouldCallAmazonAPIWithCorrectValues() throws Exception {
        RunInstancesResult runInstancesResult = mock(RunInstancesResult.class);
        Reservation reservation = mock(Reservation.class);
        Instance instance = mock(Instance.class);
        SlackMessagePosted event = anEvent();

        given(keyPairGenerationService.generateNewKey()).willReturn("aKeyName");

        given(amazonEC2.runInstances(eq(new RunInstancesRequest()
                .withImageId(awsEnvironment.getAwsDefaultImageId())
                .withInstanceType(T2Micro)
                .withMinCount(1)
                .withMaxCount(1)
                .withKeyName("aKeyName")
                .withSecurityGroups(awsEnvironment.getAwsDefaultSecurityGroup())
                .withPlacement(new Placement(awsEnvironment.getAwsDefaultPlacement())))))
                .willReturn(runInstancesResult);

        given(runInstancesResult.getReservation()).willReturn(reservation);
        given(reservation.getInstances()).willReturn(singletonList(instance));
        given(instance.getInstanceId()).willReturn("abcdefg123456");
        given(keyPairRetrievalUrlFactory.create("aKeyName")).willReturn("http://127.0.0.1:8080/keyPair/aKeyName");

        String keyPairAccessURL = ec2InstanceCreationService.createInstance(event);

        assertThat(keyPairAccessURL, is("http://127.0.0.1:8080/keyPair/aKeyName"));
    }

    private SlackMessagePosted anEvent() {
        SlackMessagePosted event = mock(SlackMessagePosted.class);
        SlackUser slackUser = mock(SlackUser.class);

        given(event.getSender()).willReturn(slackUser);
        given(slackUser.getUserName()).willReturn("aUser");

        return event;
    }
}