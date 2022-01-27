package io.rochekata.ip.adapter.web;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.rochekata.ip.IpConfig;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings("ConstantConditions")
@RunWith(SpringRunner.class)
@WebFluxTest(controllers = ReactiveApplicationUseCaseController.class)
@ContextConfiguration(classes = IpConfig.class)
class ReactiveApplicationServiceControllerTest {

    private WireMockServer wireMockServer;

    @Autowired
    private WebTestClient testClient;

    @Test
    void contextLoad() {
        assertNotNull(testClient);
    }

    @BeforeEach
    void before() {
        wireMockServer = new WireMockServer(7890);
        wireMockServer.start();
    }

    @Test
    void whenRequest2IpsFromTheSameCountry_thenReturnDistinctCountryNameInResult() throws Exception {
        String ip1 = "8.8.8.8";
        String ip2 = "9.9.9.9";
        byte[] body = this.getClass()
                .getClassLoader()
                .getResourceAsStream("success-response.json")
                .readAllBytes();

        wireMockServer
                .stubFor(WireMock.get(WireMock.urlEqualTo("/json/" + ip1)).willReturn(
                        WireMock.aResponse().withStatus(HttpStatus.OK.ordinal())
                                .withHeader("content-type", ContentType.APPLICATION_JSON.toString())
                                .withBody(body)
                ));

        wireMockServer
                .stubFor(WireMock.get(WireMock.urlEqualTo("/json/" + ip2)).willReturn(
                        WireMock.aResponse().withStatus(HttpStatus.OK.ordinal())
                                .withHeader("content-type", ContentType.APPLICATION_JSON.toString())
                                .withBody(body)
                ));


        testClient.get().uri("/northcountries?ip=" + ip1 + "&ip=" + ip2)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[0].name").isEqualTo("United States")
                .jsonPath("$[1].name").doesNotHaveJsonPath();
    }

    @Test
    void whenRequest2ips_oneReturnErrorFromExternalIp_thenErrorRequestIsSkipped() throws Exception {
        String ip1 = "8.8.8.8";
        String ip2 = "9.9.9.9";
        byte[] body = this.getClass()
                .getClassLoader()
                .getResourceAsStream("success-response.json")
                .readAllBytes();

        wireMockServer
                .stubFor(WireMock.get(WireMock.urlEqualTo("/json/" + ip1)).willReturn(
                        WireMock.aResponse().withStatus(HttpStatus.OK.ordinal())
                                .withHeader("content-type", ContentType.APPLICATION_JSON.toString())
                                .withBody(body)
                ));


        testClient.get().uri("/northcountries?ip=" + ip1 + "&ip=" + ip2)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[0].name").isEqualTo("United States")
                .jsonPath("$[1].name").doesNotHaveJsonPath();
    }

    @Test
    void whenRequest6Ips_thenShouldReturnError() throws Exception {
        String ip1 = "8.8.8.8";
        String ip2 = "9.9.9.9";

        testClient.get().uri("/northcountries?ip=" + ip1 + "&ip=" + ip2 + "&ip=" + ip1 + "&ip=" + ip2 + "&ip=" + ip1 + "&ip=" + ip2)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void whenRequest0Ips_thenShouldReturnError() throws Exception {
        testClient.get().uri("/northcountries")
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @AfterEach
    void after() {
        wireMockServer.stop();
    }
}
