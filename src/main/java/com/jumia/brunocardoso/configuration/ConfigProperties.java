package com.jumia.brunocardoso.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.regex.Pattern;

@Component
@ConfigurationProperties("application")
public class ConfigProperties {

    private Map<String, String> countrycodes;

    public Map<String, String> getCountrycodes() {
        return countrycodes;
    }

    public void setCountrycodes(Map<String, String> countrycodes) {
        this.countrycodes = countrycodes;
    }

}
