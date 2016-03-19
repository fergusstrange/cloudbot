package io.cloudbot.slack;

import com.ullink.slack.simpleslackapi.SlackSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SlackConfigurationTest {

    @Mock
    private SlackSession slackSession;

    @InjectMocks
    private SlackConfiguration slackConfiguration;

    @Test
    public void shouldConnectToSlack() throws Exception {
        slackConfiguration.connect();

        verify(slackSession).connect();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateExceptionIfCantConnect() throws Exception {
        doThrow(new IOException()).when(slackSession).connect();

        slackConfiguration.connect();
    }

    @Test
    public void shouldDisconnectFromSlack() throws Exception {
        slackConfiguration.disconnect();

        verify(slackSession).disconnect();
    }

    @Test
    public void shouldReturnNoExceptionShouldDisconnectFail() throws Exception {
        doThrow(new IOException()).when(slackSession).disconnect();

        slackConfiguration.disconnect();
    }
}