package io.cloudbot.aws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AWSEnvironment {

    private final String awsAccessKey;
    private final String awsSecretKey;
    private final String awsDefaultImageId;
    private final String awsDefaultSecurityGroup;
    private final String awsDefaultPlacement;
    private final String awsDefaultRegion;

    @Autowired
    public AWSEnvironment(@Value("${awsAccessKey}") String awsAccessKey,
                          @Value("${awsSecretKey}") String awsSecretKey,
                          @Value("${awsDefaultImageId}") String awsDefaultImageId,
                          @Value("${awsDefaultSecurityGroup}") String awsDefaultSecurityGroup,
                          @Value("${awsDefaultPlacement}") String awsDefaultPlacement,
                          @Value("${awsDefaultRegion}") String awsDefaultRegion) {
        this.awsAccessKey = awsAccessKey;
        this.awsSecretKey = awsSecretKey;
        this.awsDefaultImageId = awsDefaultImageId;
        this.awsDefaultSecurityGroup = awsDefaultSecurityGroup;
        this.awsDefaultPlacement = awsDefaultPlacement;
        this.awsDefaultRegion = awsDefaultRegion;
    }

    public String getAwsAccessKey() {
        return awsAccessKey;
    }

    public String getAwsSecretKey() {
        return awsSecretKey;
    }

    public String getAwsDefaultImageId() {
        return awsDefaultImageId;
    }

    public String getAwsDefaultSecurityGroup() {
        return awsDefaultSecurityGroup;
    }

    public String getAwsDefaultPlacement() {
        return awsDefaultPlacement;
    }

    public String getAwsDefaultRegion() {
        return awsDefaultRegion;
    }
}
