package io.rochekata.ip.domain.model;

import lombok.ToString;
import lombok.Value;

@Value
@ToString
public class Country {

    String name;
    String ipAddress;
    double latitude;

    public CountryReadModel getSnapshot() {
        return new CountryReadModel(name);
    }

    public boolean isNorthern() {
        return latitude > 0;
    }
}
