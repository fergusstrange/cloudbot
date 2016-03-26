package io.cloudbot.aws.keypair;

import com.amazonaws.services.ec2.model.KeyPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import static java.net.InetAddress.getLocalHost;
import static org.springframework.web.util.UriComponentsBuilder.fromUri;

@Component
public class KeyPairRetrievalUrlFactory {

    private final int serverPort;

    @Autowired
    public KeyPairRetrievalUrlFactory(@Value("${server.port}") int serverPort) {
        this.serverPort = serverPort;
    }

    public String create(KeyPair keyPair) {

        return fromUri(uri())
                .pathSegment("keyPair")
                .pathSegment(keyPair.getKeyName())
                .build()
                .toUriString();
    }

    private URI uri() {
        try {
            return new URI("http", null, getLocalHost().getHostAddress(), serverPort, null, null, null);
        } catch (UnknownHostException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
