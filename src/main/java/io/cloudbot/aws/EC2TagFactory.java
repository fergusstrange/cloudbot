package io.cloudbot.aws;

import com.amazonaws.services.ec2.model.Tag;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Arrays.asList;

@Component
public class EC2TagFactory {

    public List<Tag> create(String userName, String keyName) {
        return asList(
                tag("Name", "CloudbotInstance"),
                tag("CreatedBy", userName),
                tag("key", keyName));
    }

    private Tag tag(String key, String value) {
        return new Tag(key, value);
    }
}
