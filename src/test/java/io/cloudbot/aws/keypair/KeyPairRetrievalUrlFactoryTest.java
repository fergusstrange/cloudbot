package io.cloudbot.aws.keypair;

import com.amazonaws.services.ec2.model.KeyPair;
import io.cloudbot.util.HostAddressResolver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;

import java.net.UnknownHostException;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class KeyPairRetrievalUrlFactoryTest {

    private static final Pattern urlPattern = compile("^http://(?:[0-9]{1,3}\\.){3}[0-9]{1,3}:8080/keyPair/abcdefg123456$");

    @Mock
    private EmbeddedWebApplicationContext embeddedWebApplicationContext;
    @Mock
    private HostAddressResolver hostAddressResolver;

    @InjectMocks
    private KeyPairRetrievalUrlFactory keyPairRetrievalUrlFactory;

    @Test
    public void shouldReturnUrlWithKeyPairName() throws Exception {
        EmbeddedServletContainer embeddedServletContainer = mock(EmbeddedServletContainer.class);

        given(hostAddressResolver.getHostAddress()).willReturn("127.0.0.1");
        given(embeddedWebApplicationContext.getEmbeddedServletContainer()).willReturn(embeddedServletContainer);
        given(embeddedServletContainer.getPort()).willReturn(8080);

        String keyPairURL = keyPairRetrievalUrlFactory.create(new KeyPair().withKeyName("abcdefg123456"));

        assertThat(urlPattern.matcher(keyPairURL).matches(), is(true));
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionWhenUnableToResolveHostname() throws Exception {
        given(hostAddressResolver.getHostAddress()).willThrow(new UnknownHostException());

        keyPairRetrievalUrlFactory.create(new KeyPair().withKeyName("abcdefg123456"));
    }
}