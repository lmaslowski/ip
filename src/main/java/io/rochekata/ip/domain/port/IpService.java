package io.rochekata.ip.domain.port;

public interface IpService<I, O> {

    O get(I ipAddressList);
}
