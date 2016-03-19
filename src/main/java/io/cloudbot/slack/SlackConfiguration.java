package io.cloudbot.slack;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class SlackConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(SlackConfiguration.class);

    @Bean
    public SlackSession slackSession(@Value("${slackAuthToken}") String slackAuthToken) {
        SlackSession slackSession = SlackSessionFactory.createWebSocketSlackSession(slackAuthToken);
        try {
            slackSession.connect();
        }
        catch (IOException e) {
            logger.error("Unable to connect to slack with provided Auth Token.");
            System.exit(1);
        }
        return slackSession;
    }

}
