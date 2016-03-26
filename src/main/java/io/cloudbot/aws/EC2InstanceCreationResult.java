package io.cloudbot.aws;

import java.util.List;
import java.util.Objects;

public class EC2InstanceCreationResult {

    private final String keyRetrievalUrl;
    private final List<String> publicIPs;

    public EC2InstanceCreationResult(String keyRetrievalUrl, List<String> publicIPs) {
        this.keyRetrievalUrl = keyRetrievalUrl;
        this.publicIPs = publicIPs;
    }

    public String getKeyRetrievalUrl() {
        return keyRetrievalUrl;
    }

    public List<String> getPublicIPs() {
        return publicIPs;
    }

    @Override
    public String toString() {
        return "EC2InstanceCreationResult{" +
                "keyRetrievalUrl='" + keyRetrievalUrl + '\'' +
                ", publicIPs=" + publicIPs +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EC2InstanceCreationResult that = (EC2InstanceCreationResult) o;
        return Objects.equals(keyRetrievalUrl, that.keyRetrievalUrl) &&
                Objects.equals(publicIPs, that.publicIPs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyRetrievalUrl, publicIPs);
    }
}
