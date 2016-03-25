package io.cloudbot.slack;

import com.ullink.slack.simpleslackapi.SlackSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.ullink.slack.simpleslackapi.impl.SlackSessionFactory.createWebSocketSlackSession;

@Configuration
public class SlackConfiguration {

    @Bean
    public SlackSession slackSession(SlackEnvironment slackEnvironment) {
        return createWebSocketSlackSession(slackEnvironment.getSlackAuthToken());
    }
}
