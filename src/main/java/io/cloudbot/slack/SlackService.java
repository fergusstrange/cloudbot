package io.cloudbot.slack;

import com.ullink.slack.simpleslackapi.SlackSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SlackService {

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
}
