package io.cloudbot.aws;

import com.amazonaws.services.ec2.model.Tag;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

public class EC2TagFactoryTest {

    private final EC2TagFactory ec2TagFactory = new EC2TagFactory();

    @Test
    public void shouldReturnTags() throws Exception {
        List<Tag> tags = ec2TagFactory.create("aUser", "randomKeyName");

        assertThat(tags, IsIterableContainingInOrder.contains(
                new Tag("Name", "CloudbotInstance"),
                new Tag("CreatedBy", "aUser"),
                new Tag("key", "randomKeyName")
        ));
    }
}