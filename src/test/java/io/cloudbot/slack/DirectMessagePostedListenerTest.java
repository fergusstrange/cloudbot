package io.cloudbot.slack;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class DirectMessagePostedListenerTest {

    private final DirectMessagePostedListener directMessagePostedListener = new DirectMessagePostedListener();

    @Test
    public void shouldName() throws Exception {
        SlackMessagePosted event = mock(SlackMessagePosted.class);
        SlackSession session = mock(SlackSession.class);

        directMessagePostedListener.onEvent(event, session);
    }
}