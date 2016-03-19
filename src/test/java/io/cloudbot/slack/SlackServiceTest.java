package io.cloudbot.slack;

import com.ullink.slack.simpleslackapi.SlackSession;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class SlackServiceTest {

    private final DirectMessagePostedListener directMessagePostedListener = mock(DirectMessagePostedListener.class);
    private final SlackSession slackSession = mock(SlackSession.class);
    private final SlackService slackService = new SlackService(slackSession, directMessagePostedListener);

    @Test
    public void shouldName() throws Exception {
        slackService.listenForDirectMessage();
    }
}