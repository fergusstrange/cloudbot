package io.cloudbot.slack;

import com.ullink.slack.simpleslackapi.SlackSession;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class SlackConfigurationTest {

    private final SlackConfiguration slackConfiguration = new SlackConfiguration();

    @Test
    public void shouldReturnSlackSession() throws Exception {
        SlackEnvironment slackEnvironment = mock(SlackEnvironment.class);

        SlackSession slackSession = slackConfiguration.slackSession(slackEnvironment);

        assertThat(slackSession, not(nullValue()));
    }
}