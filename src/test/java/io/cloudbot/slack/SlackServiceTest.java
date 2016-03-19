package io.cloudbot.slack;

import com.ullink.slack.simpleslackapi.SlackSession;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SlackServiceTest {

    private final DirectMessagePostedListener directMessagePostedListener = mock(DirectMessagePostedListener.class);
    private final SlackSession slackSession = mock(SlackSession.class);
    private final SlackService slackService = new SlackService(slackSession, directMessagePostedListener);

    @Test
    public void shouldAddListener() throws Exception {
        slackService.listenForDirectMessage();

        verify(slackSession).addMessagePostedListener(directMessagePostedListener);
    }
}