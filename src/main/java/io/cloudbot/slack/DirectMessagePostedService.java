package io.cloudbot.slack;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;
import io.cloudbot.aws.EC2InstanceCreationResult;
import io.cloudbot.aws.EC2InstanceCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.fromCallable;
import static reactor.core.publisher.SchedulerGroup.async;

@Component
public class DirectMessagePostedService implements SlackMessagePostedListener {

    private final SlackAuthorisationService slackAuthorisationService;
    private final EC2InstanceCreationService ec2InstanceCreationService;

    @Autowired
    public DirectMessagePostedService(SlackAuthorisationService slackAuthorisationService,
                                      EC2InstanceCreationService ec2InstanceCreationService) {
        this.slackAuthorisationService = slackAuthorisationService;
        this.ec2InstanceCreationService = ec2InstanceCreationService;
    }

    @Override
    public void onEvent(SlackMessagePosted event, SlackSession session) {
        fromCallable(() -> slackAuthorisationService.userAuthorised(event.getSender()))
                .publishOn(async())
                .flatMap(userAuthorised -> userAuthorised ? authorised(event, session) : unauthorised(event, session))
                .log()
                .consume(aVoid -> {});
    }

    private Mono<Void> unauthorised(SlackMessagePosted event, SlackSession session) {
        return Mono.fromRunnable(() -> {
            if (!event.getSender().isBot()) {
                slackMessageReply(event, session, "You're not allowed to do that...");
            }
        });
    }

    private Mono<Void> authorised(SlackMessagePosted event, SlackSession session) {
        return Mono.fromRunnable(() -> {
            EC2InstanceCreationResult creationResult = ec2InstanceCreationService.createInstance(event);
            slackMessageReply(event, session, "Instance creation in progress. Access your private key here...");
            slackMessageReply(event, session, creationResult.getKeyRetrievalUrl());
            slackMessageReply(event, session, String.format("Your new instance(s) are at %s", creationResult.getPublicIPs()));
            slackMessageReply(event, session, "This link will expire in 5 minutes and cannot be recovered.");
        });
    }

    private void slackMessageReply(SlackMessagePosted event, SlackSession session, String message) {
        session.sendMessageToUser(event.getSender(), message, null);
    }
}
