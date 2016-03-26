package io.cloudbot.util;

import org.junit.Test;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class HostAddressResolverTest {

    private static final Pattern addressPattern = compile("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$");

    private final HostAddressResolver hostAddressResolver = new HostAddressResolver();

    @Test
    public void shouldReturnHostAddress() throws Exception {
        String hostAddress = hostAddressResolver.getHostAddress();

        assertThat(addressPattern.matcher(hostAddress).matches(), is(true));
    }
}