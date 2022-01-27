package io.rochekata.ip.adapter.ipservice;

import com.tngtech.archunit.thirdparty.com.google.common.net.HttpHeaders;
import io.rochekata.ip.domain.model.Country;
import io.rochekata.ip.domain.model.IpAddress;
import io.rochekata.ip.domain.port.IpService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.util.function.Function;

public class ReactiveIpVigilante implements IpService<Flux<IpAddress>, Flux<Country>> {

    private final String serviceHost;
    private final String URI_PATTERN = "/json/";

    private static final Logger logger = LogManager.getLogger();

    public ReactiveIpVigilante(String serviceHost) {
        this.serviceHost = serviceHost;
    }

    @Override
    public Flux<Country> get(Flux<IpAddress> ipAddressList) {
        return ipAddressList
                .doOnNext(logger::info)
                .publishOn(Schedulers.boundedElastic())
                .flatMap(toResponse())
                .doOnNext(logger::info)
                .publishOn(Schedulers.parallel())
                .map(toCountry())
                .publishOn(Schedulers.parallel())
                .doOnNext(logger::info)
                .subscribeOn(Schedulers.parallel())
                .onErrorContinue((throwable, o) -> logger.error(throwable));
    }

    @NotNull
    private Function<IpAddress, Publisher<? extends IpVigilanteResult>> toResponse() {
        return ipAddress -> buildWebClient()
                .get()
                .uri(URI.create(serviceHost + URI_PATTERN + ipAddress.getIpAddress()))
                .retrieve()
                .bodyToMono(IpVigilanteResult.class);
    }

    @NotNull
    private Function<IpVigilanteResult, Country> toCountry() {
        return ipVigilanteResult -> new Country(
                ipVigilanteResult.data.country_name,
                ipVigilanteResult.data.ipv4,
                Double.parseDouble(ipVigilanteResult.data.latitude));
    }

    private WebClient buildWebClient() {
        return WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
