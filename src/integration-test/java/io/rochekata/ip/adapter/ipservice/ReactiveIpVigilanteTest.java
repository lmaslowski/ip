package io.rochekata.ip.adapter.ipservice;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.rochekata.ip.domain.model.Country;
import io.rochekata.ip.domain.model.IpAddress;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.io.IOException;

public class ReactiveIpVigilanteTest {

    private WireMockServer wireMockServer;
    private ReactiveIpVigilante reactiveIpVigilante;

    @BeforeEach
    void before() {
        reactiveIpVigilante = new ReactiveIpVigilante("http://localhost:9999");
        wireMockServer = new WireMockServer(9999);
        wireMockServer.start();
    }

    @Test
    void given4IPS_Returns4Country() throws IOException {
        String ip = "8.8.8.8";
        byte[] body = this.getClass()
                .getClassLoader()
                .getResourceAsStream("success-response.json")
                .readAllBytes();

        wireMockServer
                .stubFor(WireMock.get(WireMock.urlEqualTo("/json/" + ip)).willReturn(
                        WireMock.aResponse().withStatus(HttpStatus.OK.ordinal())
                                .withHeader("content-type", ContentType.APPLICATION_JSON.toString())
                                .withBody(body)
                ));


        final Flux<Country> countryFlux = reactiveIpVigilante
                .get(Flux.just(
                        new IpAddress(ip),
                        new IpAddress(ip),
                        new IpAddress(ip),
                        new IpAddress(ip)
                ));

        StepVerifier.create(countryFlux)
                .expectNextCount(4)
                .verifyComplete();
    }
}
