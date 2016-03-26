package io.cloudbot.aws.keypair;

import com.amazonaws.services.ec2.model.KeyPair;
import io.cloudbot.util.HostAddressResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import static org.springframework.web.util.UriComponentsBuilder.fromUri;

@Component
public class KeyPairRetrievalUrlFactory {

    private final static Logger logger = LoggerFactory.getLogger(KeyPairRetrievalUrlFactory.class);

    private final EmbeddedWebApplicationContext embeddedWebApplicationContext;
    private final HostAddressResolver hostAddressResolver;

    @Autowired
    public KeyPairRetrievalUrlFactory(EmbeddedWebApplicationContext embeddedWebApplicationContext,
                                      HostAddressResolver hostAddressResolver) {
        this.embeddedWebApplicationContext = embeddedWebApplicationContext;
        this.hostAddressResolver = hostAddressResolver;
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
            return new URI("http", null,
                    hostAddressResolver.getHostAddress(),
                    embeddedWebApplicationContext.getEmbeddedServletContainer().getPort(), null, null, null);
        } catch (UnknownHostException | URISyntaxException e) {
            logger.error("Unable to resolve host address", e);
            throw new RuntimeException(e);
        }
    }
}
