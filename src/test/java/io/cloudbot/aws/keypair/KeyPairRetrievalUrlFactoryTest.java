package io.cloudbot.aws.keypair;

import com.amazonaws.services.ec2.model.KeyPair;
import org.junit.Test;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class KeyPairRetrievalUrlFactoryTest {

    private static final Pattern urlPattern = compile("^http://(?:[0-9]{1,3}\\.){3}[0-9]{1,3}:8080/keyPair/abcdefg123456$");

    private final KeyPairRetrievalUrlFactory keyPairRetrievalUrlFactory = new KeyPairRetrievalUrlFactory(8080);

    @Test
    public void shouldReturnUrlWithKeyPairName() throws Exception {
        String keyPairURL = keyPairRetrievalUrlFactory.create(new KeyPair().withKeyName("abcdefg123456"));

        assertThat(urlPattern.matcher(keyPairURL).matches(), is(true));
    }
}