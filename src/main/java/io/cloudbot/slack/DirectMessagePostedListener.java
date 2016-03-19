package io.cloudbot.slack;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;
import org.springframework.stereotype.Component;

@Component
public class DirectMessagePostedListener implements SlackMessagePostedListener {

    @Override
    public void onEvent(SlackMessagePosted event, SlackSession session) {
    }
}
