package io.cloudbot;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static com.ullink.slack.simpleslackapi.impl.SlackSessionFactory.createWebSocketSlackSession;

@Component
public class SlackService {

    private final String slackAuthToken;

    @Autowired
    public SlackService(@Value("${slackAuthToken}") String slackAuthToken) {
        this.slackAuthToken = slackAuthToken;
    }

    @PostConstruct
    public void listenForNonsensicalCrap() {
        try {
            SlackSession webSocketSlackSession = createWebSocketSlackSession(slackAuthToken);
            webSocketSlackSession.connect();
            webSocketSlackSession.addMessagePostedListener(slackMessagePostedListener());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private SlackMessagePostedListener slackMessagePostedListener() {
        return (event, session) -> System.out.println(formattedMessage(event));
    }

    private String formattedMessage(SlackMessagePosted event) {
        return String.format("%s: %s", event.getSender().getUserName(), event.getMessageContent());
    }
}
