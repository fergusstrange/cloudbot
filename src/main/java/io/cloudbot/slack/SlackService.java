package io.cloudbot.slack;

import com.ullink.slack.simpleslackapi.SlackSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Component
public class SlackService {

    private static final Logger logger = LoggerFactory.getLogger(SlackService.class);

    private final SlackSession webSocketSlackSession;
    private final DirectMessagePostedListener directMessagePostedListener;

    @Autowired
    public SlackService(SlackSession webSocketSlackSession,
                        DirectMessagePostedListener directMessagePostedListener) {
        this.webSocketSlackSession = webSocketSlackSession;
        this.directMessagePostedListener = directMessagePostedListener;
    }

    @PostConstruct
    public void listenForDirectMessage() {
        webSocketSlackSession.addMessagePostedListener(directMessagePostedListener);
    }

    @PostConstruct
    public void connect() {
        try {
            webSocketSlackSession.connect();
        } catch (IOException e) {
            logger.error("Unable to connect to slack with provided auth token");
            throw new IllegalStateException();
        }
    }

    @PreDestroy
    public void disconnect() {
        try {
            webSocketSlackSession.disconnect();
        } catch (IOException e) {
            logger.error("Unable to disconnect from slack");
        }
    }
}
