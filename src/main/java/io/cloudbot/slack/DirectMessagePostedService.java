package io.cloudbot.slack;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public class DirectMessagePostedService implements SlackMessagePostedListener {

    private static final Logger logger = LoggerFactory.getLogger(DirectMessagePostedService.class);

    @Override
    public void onEvent(SlackMessagePosted event, SlackSession session) {
        logger.info(formattedMessage(event));
        //Is authenticated?
        //
    }

    private String formattedMessage(SlackMessagePosted event) {
        return format("%s: %s", event.getSender().getUserName(), event.getMessageContent());
    }
}
