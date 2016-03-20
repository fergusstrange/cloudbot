package io.cloudbot.slack;

import com.ullink.slack.simpleslackapi.SlackUser;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class SlackAuthenticationServiceTest {

    private final SlackAuthenticationService slackAuthenticationService = new SlackAuthenticationService("popeye, james");

    @Test
    public void shouldBeAuthenticated() throws Exception {
        SlackUser slackUser = mock(SlackUser.class);
        given(slackUser.getUserName()).willReturn("popeye");

        boolean userAuthenticated = slackAuthenticationService.userAuthenticated(slackUser);

        assertThat(userAuthenticated, is(true));
    }

    @Test
    public void shouldNotBeAuthenticated() throws Exception {
        SlackUser slackUser = mock(SlackUser.class);
        given(slackUser.getUserName()).willReturn("bobby");

        boolean userAuthenticated = slackAuthenticationService.userAuthenticated(slackUser);

        assertThat(userAuthenticated, is(false));
    }

    @Test
    public void shouldTrimOutAllWhitespaceForUserList() throws Exception {
        SlackUser slackUser = mock(SlackUser.class);
        given(slackUser.getUserName()).willReturn("popeye");

        boolean userAuthenticated = new SlackAuthenticationService("          popeye,              james").userAuthenticated(slackUser);

        assertThat(userAuthenticated, is(true));
    }
}