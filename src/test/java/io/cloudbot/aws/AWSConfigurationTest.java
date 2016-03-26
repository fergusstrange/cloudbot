package io.cloudbot.aws;

import com.amazonaws.services.ec2.AmazonEC2;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class AWSConfigurationTest {

    private final AWSConfiguration awsConfiguration = new AWSConfiguration();

    @Test
    public void shouldReturnEC2Client() throws Exception {
        AWSEnvironment awsEnvironment = mock(AWSEnvironment.class);

        given(awsEnvironment.getAwsDefaultRegion()).willReturn("eu-west-1");
        given(awsEnvironment.getAwsAccessKey()).willReturn("abcdef");
        given(awsEnvironment.getAwsSecretKey()).willReturn("123556");

        AmazonEC2 amazonEC2 = awsConfiguration.amazonEC2Client(awsEnvironment);

        assertThat(amazonEC2, notNullValue());
    }
}