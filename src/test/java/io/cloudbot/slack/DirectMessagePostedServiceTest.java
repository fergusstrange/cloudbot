package io.cloudbot.slack;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import io.cloudbot.aws.EC2InstanceCreationResult;
import io.cloudbot.aws.EC2InstanceCreationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static java.util.Collections.singletonList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DirectMessagePostedServiceTest {

    @Mock
    private SlackAuthenticationService slackAuthenticationService;
    @Mock
    private EC2InstanceCreationService ec2InstanceCreationService;

    @InjectMocks
    private DirectMessagePostedService directMessagePostedService;

    @Test
    public void shouldReplyWithAuthenticated() throws Exception {
        SlackMessagePosted event = slackMessage();
        SlackSession session = slackSession();

        given(slackAuthenticationService.userAuthenticated(event.getSender())).willReturn(true);
        given(ec2InstanceCreationService.createInstance(event))
                .willReturn(new EC2InstanceCreationResult("http://127.0.0.1:8080/keyPair/aKeyName", singletonList("127.0.0.1")));

        directMessagePostedService.onEvent(event, session);

        verify(ec2InstanceCreationService).createInstance(event);
    }

    @Test
    public void shouldReplyWithUnauthenticated() throws Exception {
        SlackMessagePosted event = slackMessage();
        SlackSession session = slackSession();

        directMessagePostedService.onEvent(event, session);

        verify(session).sendMessageToUser(event.getSender(), "You're not allowed to do that...", null);
    }

    private SlackSession slackSession() {
        return mock(SlackSession.class);
    }

    private SlackMessagePosted slackMessage() {
        SlackMessagePosted event = mock(SlackMessagePosted.class);
        SlackUser slackUser = slackUser();
        given(event.getSender()).willReturn(slackUser);
        return event;
    }

    private SlackUser slackUser() {
        SlackUser slackUser = mock(SlackUser.class);
        given(slackUser.getUserName()).willReturn("James");
        return slackUser;
    }
}