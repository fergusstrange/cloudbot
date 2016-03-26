package io.cloudbot.util;

import org.springframework.stereotype.Component;

import java.net.UnknownHostException;

import static java.net.InetAddress.getLocalHost;

@Component
public class HostAddressResolver {

    public String getHostAddress() throws UnknownHostException {
        return getLocalHost().getHostAddress();
    }
}
