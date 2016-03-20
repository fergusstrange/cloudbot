package io.cloudbot.slack;

import com.ullink.slack.simpleslackapi.SlackUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class SlackAuthenticationService {

    private final String slackAdmins;

    @Autowired
    public SlackAuthenticationService(@Value("${slackAdmins}") String slackAdmins) {
        this.slackAdmins = slackAdmins;
    }

    public boolean userAuthenticated(SlackUser user) {
        return Stream.of(slackAdmins.trim().split(","))
                .anyMatch(authUser -> authUser.equals(user.getUserName()));
    }
}
