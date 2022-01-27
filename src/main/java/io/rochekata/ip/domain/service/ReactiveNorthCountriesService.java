package io.rochekata.ip.domain.service;

import io.rochekata.ip.domain.model.Country;
import io.rochekata.ip.domain.model.CountryReadModel;
import io.rochekata.ip.domain.model.IpAddress;
import io.rochekata.ip.domain.port.IpService;
import io.rochekata.ip.domain.port.NorthCountriesService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.Comparator;
import java.util.List;

public class ReactiveNorthCountriesService implements NorthCountriesService<List<IpAddress>, Flux<CountryReadModel>> {

    private final IpService<Flux<IpAddress>, Flux<Country>> ipService;
    private static final Logger logger = LogManager.getLogger();

    public ReactiveNorthCountriesService(IpService<Flux<IpAddress>, Flux<Country>> ipService) {
        this.ipService = ipService;
    }

    @Override
    public Flux<CountryReadModel> getNorthCountries(List<IpAddress> ips) {
        throwExceptionIfInputNotValid(ips);
        return ipService
                .get(Flux.fromStream(ips.stream()))
                .subscribeOn(Schedulers.boundedElastic())
                .filter(Country::isNorthern)
                .map(Country::getSnapshot)
                .distinct()
                .sort(Comparator.comparing(CountryReadModel::getName))
                .doOnError(logger::info);
    }

    private void throwExceptionIfInputNotValid(List<IpAddress> ips) {
        if (ips.isEmpty() || ips.size() > 5)
            throw new NorthCountriesServiceValidationException();
    }
}
