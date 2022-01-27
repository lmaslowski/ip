package io.rochekata.ip.adapter.ipservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
@NoArgsConstructor
class IpVigilanteResult {
    public String status;
    public IpVigilanteData data;
}

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
@NoArgsConstructor
class IpVigilanteData {
    public String ipv4;
    public String country_name;
    public String latitude;
}

