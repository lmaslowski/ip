package io.rochekata.ip;

import io.rochekata.ip.adapter.ipservice.ReactiveIpVigilante;
import io.rochekata.ip.application.ReactiveApplicationUseCase;
import io.rochekata.ip.domain.model.Country;
import io.rochekata.ip.domain.model.CountryReadModel;
import io.rochekata.ip.domain.model.IpAddress;
import io.rochekata.ip.domain.port.IpService;
import io.rochekata.ip.domain.port.NorthCountriesService;
import io.rochekata.ip.domain.service.NonReactiveNorthCountriesService;
import io.rochekata.ip.domain.service.ReactiveNorthCountriesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.List;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class IpConfig {

    @Bean
    public ReactiveApplicationUseCase reactiveApplicationUseCase(NorthCountriesService<List<IpAddress>, Flux<CountryReadModel>> northCountriesService) {
        return new ReactiveApplicationUseCase(northCountriesService);
    }

    @Bean
    public NorthCountriesService<List<IpAddress>, Flux<CountryReadModel>> reactiveNorthCountriesService(IpService<Flux<IpAddress>, Flux<Country>> reactiveIpService) {
        return new ReactiveNorthCountriesService(reactiveIpService);
    }

    @Bean
    public IpService<Flux<IpAddress>, Flux<Country>> reactiveIpService(@Value("${ipservice.host}") String serviceHost) {
        return new ReactiveIpVigilante(serviceHost);
    }

    @Bean
    public NorthCountriesService<List<IpAddress>, List<CountryReadModel>> northCountriesService(IpService<List<IpAddress>, List<Country>> ipService) {
        return new NonReactiveNorthCountriesService(ipService);
    }

    @Bean
    public IpService<List<IpAddress>, List<Country>> ipService() {
        return ipAddressList -> null;
    }
}
