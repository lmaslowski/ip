package io.rochekata.ip.application;

import io.rochekata.ip.domain.model.CountryReadModel;
import io.rochekata.ip.domain.model.IpAddress;
import io.rochekata.ip.domain.port.NorthCountriesService;
import reactor.core.publisher.Flux;

import java.util.List;

public class ReactiveApplicationUseCase {

    private final NorthCountriesService<List<IpAddress>, Flux<CountryReadModel>> northCountriesService;

    public ReactiveApplicationUseCase(NorthCountriesService<List<IpAddress>, Flux<CountryReadModel>> northCountriesService) {
        this.northCountriesService = northCountriesService;
    }

    public Flux<CountryReadModel> getNorthCountries(List<IpAddress> ips) {
        return northCountriesService.getNorthCountries(ips);
    }
}