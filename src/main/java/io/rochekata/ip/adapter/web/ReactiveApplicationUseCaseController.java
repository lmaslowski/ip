package io.rochekata.ip.adapter.web;

import io.rochekata.ip.application.ReactiveApplicationUseCase;
import io.rochekata.ip.domain.model.CountryReadModel;
import io.rochekata.ip.domain.model.IpAddress;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
class ReactiveApplicationUseCaseController {

    private final ReactiveApplicationUseCase applicationUseCase;

    private static final Logger logger = LogManager.getLogger();

    @GetMapping(value = "/northcountries")
    Flux<CountryReadModel> northcountries(@RequestParam("ip") IpAddress[] ids) {
        return applicationUseCase.getNorthCountries(Arrays.stream(ids).collect(Collectors.toList()));
    }
}
