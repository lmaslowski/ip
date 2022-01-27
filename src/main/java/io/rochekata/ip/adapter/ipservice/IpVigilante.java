package io.rochekata.ip.adapter.ipservice;

import io.rochekata.ip.domain.model.Country;
import io.rochekata.ip.domain.model.IpAddress;
import io.rochekata.ip.domain.port.IpService;

import java.util.List;

public class IpVigilante implements IpService<List<IpAddress>, List<Country>> {
    @Override
    public List<Country> get(List<IpAddress> ipAddressList) {
        throw new UnsupportedOperationException();
    }
}
