package io.cloudbot.aws;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.amazonaws.PredefinedClientConfigurations.defaultConfig;
import static com.amazonaws.regions.Region.getRegion;

@Configuration
public class AWSConfiguration {

    @Bean
    public AmazonEC2 amazonEC2Client(AWSEnvironment awsEnvironment) {
        return getRegion(Regions.fromName(awsEnvironment.getAwsDefaultRegion()))
                .createClient(AmazonEC2Client.class, credentials(awsEnvironment), defaultConfig());
    }

    private static StaticCredentialsProvider credentials(AWSEnvironment awsEnvironment) {
        return new StaticCredentialsProvider(new BasicAWSCredentials(
                awsEnvironment.getAwsAccessKey(),
                awsEnvironment.getAwsSecretKey()));
    }
}
