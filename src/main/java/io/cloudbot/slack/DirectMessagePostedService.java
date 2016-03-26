package io.cloudbot.slack;

import com.ullink.slack.simpleslackapi.SlackMessageHandle;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;
import com.ullink.slack.simpleslackapi.replies.SlackMessageReply;
import io.cloudbot.aws.EC2InstanceCreationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DirectMessagePostedService implements SlackMessagePostedListener {

    private static final Logger logger = LoggerFactory.getLogger(DirectMessagePostedService.class);

    private final SlackAuthenticationService slackAuthenticationService;
    private final EC2InstanceCreationService ec2InstanceCreationService;

    @Autowired
    public DirectMessagePostedService(SlackAuthenticationService slackAuthenticationService,
                                      EC2InstanceCreationService ec2InstanceCreationService) {
        this.slackAuthenticationService = slackAuthenticationService;
        this.ec2InstanceCreationService = ec2InstanceCreationService;
    }

    @Override
    public void onEvent(SlackMessagePosted event, SlackSession session) {
        logger.debug(formattedLogMessage(event));

        if(slackAuthenticationService.userAuthenticated(event.getSender())) {
            String newInstancePrivateKeyURL = ec2InstanceCreationService.createInstance(event);
            slackMessageReply(event, session, "Instance creation in progress. Access your private key here...");
            slackMessageReply(event, session, newInstancePrivateKeyURL);
            slackMessageReply(event, session, "This link will expire in 5 minutes and cannot be recovered.");
        } else {
            slackMessageReply(event, session, "You're not allowed to do that...");
        }
    }

    private static SlackMessageHandle<SlackMessageReply> slackMessageReply(SlackMessagePosted event, SlackSession session, String message) {
        return session.sendMessageToUser(event.getSender(), message, null);
    }

    private static String formattedLogMessage(SlackMessagePosted event) {
        return String.format("Received message: %s - %s", event.getSender().getUserName(), event.getMessageContent());
    }
}
