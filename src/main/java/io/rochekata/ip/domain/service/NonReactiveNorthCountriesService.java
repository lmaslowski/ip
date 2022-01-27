package io.rochekata.ip.domain.service;

import io.rochekata.ip.domain.model.Country;
import io.rochekata.ip.domain.model.CountryReadModel;
import io.rochekata.ip.domain.model.IpAddress;
import io.rochekata.ip.domain.port.IpService;
import io.rochekata.ip.domain.port.NorthCountriesService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class NonReactiveNorthCountriesService implements NorthCountriesService<List<IpAddress>, List<CountryReadModel>> {

    private final IpService<List<IpAddress>, List<Country>> ipService;

    public NonReactiveNorthCountriesService(IpService<List<IpAddress>, List<Country>> ipService) {
        this.ipService = ipService;
    }

    @Override
    public List<CountryReadModel> getNorthCountries(List<IpAddress> ips) {
        throwExceptionIfInputNotValid(ips);
        return ipService
                .get(ips)
                .stream()
                .filter(Country::isNorthern)
                .map(Country::getSnapshot)
                .sorted(Comparator.comparing(CountryReadModel::getName))
                .distinct()
                .collect(Collectors.toList());
    }

    private void throwExceptionIfInputNotValid(List<IpAddress> ips) {
        if (ips.isEmpty() || ips.size() > 5)
            throw new NorthCountriesServiceValidationException();
    }
}
