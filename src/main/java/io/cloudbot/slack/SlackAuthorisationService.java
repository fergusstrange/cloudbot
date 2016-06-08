package io.cloudbot.slack;

import com.ullink.slack.simpleslackapi.SlackUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class SlackAuthorisationService {

    private final SlackEnvironment slackEnvironment;

    @Autowired
    public SlackAuthorisationService(SlackEnvironment slackEnvironment) {
        this.slackEnvironment = slackEnvironment;
    }

    public boolean userAuthorised(SlackUser user) {
        return Stream.of(slackEnvironment.getSlackAdmins().trim().split(","))
                .anyMatch(authUser -> authUser.equals(user.getUserName()));
    }
}
