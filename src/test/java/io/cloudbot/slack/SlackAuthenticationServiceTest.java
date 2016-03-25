package io.cloudbot.slack;

import com.ullink.slack.simpleslackapi.SlackUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class SlackAuthenticationServiceTest {

    @Mock
    private SlackEnvironment slackEnvironment;

    @InjectMocks
    private SlackAuthenticationService slackAuthenticationService;

    @Test
    public void shouldBeAuthenticated() throws Exception {
        SlackUser slackUser = mock(SlackUser.class);

        given(slackUser.getUserName()).willReturn("popeye");
        given(slackEnvironment.getSlackAdmins()).willReturn("popeye, jamie, fred");

        boolean userAuthenticated = slackAuthenticationService.userAuthenticated(slackUser);

        assertThat(userAuthenticated, is(true));
    }

    @Test
    public void shouldNotBeAuthenticated() throws Exception {
        SlackUser slackUser = mock(SlackUser.class);

        given(slackUser.getUserName()).willReturn("bobby");
        given(slackEnvironment.getSlackAdmins()).willReturn("notbobby");

        boolean userAuthenticated = slackAuthenticationService.userAuthenticated(slackUser);

        assertThat(userAuthenticated, is(false));
    }

    @Test
    public void shouldTrimOutAllWhitespaceForUserList() throws Exception {
        SlackUser slackUser = mock(SlackUser.class);

        given(slackUser.getUserName()).willReturn("popeye");
        given(slackEnvironment.getSlackAdmins()).willReturn("   popeye,   george");

        boolean userAuthenticated = slackAuthenticationService.userAuthenticated(slackUser);

        assertThat(userAuthenticated, is(true));
    }
}