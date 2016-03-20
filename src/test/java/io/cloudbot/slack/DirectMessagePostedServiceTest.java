package io.cloudbot.slack;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import org.junit.Ignore;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class DirectMessagePostedServiceTest {

    private final DirectMessagePostedService directMessagePostedService = new DirectMessagePostedService();

    @Test
    @Ignore
    public void shouldName() throws Exception {
        SlackMessagePosted event = mock(SlackMessagePosted.class);
        SlackSession session = mock(SlackSession.class);

        directMessagePostedService.onEvent(event, session);
    }
}