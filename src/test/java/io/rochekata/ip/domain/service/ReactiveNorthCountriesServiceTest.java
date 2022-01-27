package io.rochekata.ip.domain.service;

import io.rochekata.ip.domain.model.Country;
import io.rochekata.ip.domain.model.CountryReadModel;
import io.rochekata.ip.domain.model.IpAddress;
import io.rochekata.ip.domain.port.IpService;
import io.rochekata.ip.domain.port.NorthCountriesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReactiveNorthCountriesServiceTest {

    private NorthCountriesService<List<IpAddress>, Flux<CountryReadModel>> service;
    private IpService<Flux<IpAddress>, Flux<Country>> ipService;

    @BeforeEach
    void before() {
        ipService = mock(IpService.class);
        service = new ReactiveNorthCountriesService(ipService);
    }

    @Test
    void whenRequest0Ips_returnsError(){
        assertThrows(NorthCountriesServiceValidationException.class,
                () -> service.getNorthCountries(Collections.emptyList()));
    }

    @Test
    void whenRequestMoreThen5Ips_returnsError(){
        assertThrows(NorthCountriesServiceValidationException.class,
                () -> service.getNorthCountries(Arrays.asList(
                        new IpAddress("0.0.0.1"),
                        new IpAddress("0.0.0.1"),
                        new IpAddress("0.0.0.1"),
                        new IpAddress("0.0.0.1"),
                        new IpAddress("0.0.0.1"),
                        new IpAddress("0.0.0.1")
                )));
    }

    @Test
    void whenRequestIp_returnsOnlyNorthernDistinctCountryNameSortedAlphabetically() {
        //given
        final List<IpAddress> ipAddresses = Arrays.asList(
                new IpAddress("0.0.0.0"),
                new IpAddress("0.0.0.0"),
                new IpAddress("0.0.0.1")
        );

        final List<Country> countries = Arrays.asList(
                new Country("Poland", "0.0.0.0", 1),
                new Country("Poland", "0.0.0.0", 1),
                new Country("England", "0.0.0.1", 1),
                new Country("RPA", "0.0.0.2", -1)
        );

        when(ipService.get(any(Flux.class))).thenReturn(Flux.fromStream(countries.stream()));

        //when then
        StepVerifier.create(service.getNorthCountries(ipAddresses))
                .expectNext(new CountryReadModel("England"))
                .expectNext(new CountryReadModel("Poland"))
                .expectComplete()
                .verify();
    }

}
