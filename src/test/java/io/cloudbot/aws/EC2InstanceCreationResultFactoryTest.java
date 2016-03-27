package io.cloudbot.aws;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.Instance;
import io.cloudbot.aws.keypair.KeyPairRetrievalUrlFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class EC2InstanceCreationResultFactoryTest {

    @Mock
    private KeyPairRetrievalUrlFactory keyPairRetrievalUrlFactory;
    @Mock
    private AmazonEC2 amazonEC2;

    @InjectMocks
    private EC2InstanceCreationResultFactory ec2InstanceCreationResultFactory;

    @Test
    @Ignore
    public void shouldReturnPopulatedEC2InstanceCreationResult() throws Exception {
        Instance instance = mock(Instance.class);

        given(keyPairRetrievalUrlFactory.create("aKey")).willReturn("http://127.0.0.1:8080/keyPair/aKey");
        given(instance.getPublicIpAddress()).willReturn("127.0.0.1");

        EC2InstanceCreationResult ec2InstanceCreationResult = ec2InstanceCreationResultFactory.create("aKey", singletonList(instance));

        assertThat(ec2InstanceCreationResult, is(new EC2InstanceCreationResult("http://127.0.0.1:8080/keyPair/aKey", singletonList("127.0.0.1"))));
    }
}