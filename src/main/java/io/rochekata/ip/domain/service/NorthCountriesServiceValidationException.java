package io.rochekata.ip.domain.service;

public class NorthCountriesServiceValidationException extends RuntimeException {

    private static final String GET_NORTH_COUNTRIES_ERROR_MESSAGE_AT_LEAST_1_AND_MAXIMUM_5_IPS =
            "getNorthCountries service accept at least 1 and maximum 5 ip addresses";

    public NorthCountriesServiceValidationException() {
        super(GET_NORTH_COUNTRIES_ERROR_MESSAGE_AT_LEAST_1_AND_MAXIMUM_5_IPS);
    }
}