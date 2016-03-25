package io.cloudbot.slack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SlackEnvironment {

    private final String slackAdmins;
    private final String slackAuthToken;

    @Autowired
    public SlackEnvironment(@Value("${slackAdmins}") String slackAdmins,
                            @Value("${slackAuthToken}") String slackAuthToken) {
        this.slackAdmins = slackAdmins;
        this.slackAuthToken = slackAuthToken;
    }

    public String getSlackAdmins() {
        return slackAdmins;
    }

    public String getSlackAuthToken() {
        return slackAuthToken;
    }
}
