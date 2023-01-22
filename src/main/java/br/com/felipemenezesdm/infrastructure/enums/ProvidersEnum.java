package br.com.felipemenezesdm.infrastructure.enums;

import java.util.Arrays;

public enum ProvidersEnum {
    GCP,
    AWS,
    ENVIRONMENT,
    APPLICATION;

    /**
     * Search provider by name
     * @param providerName Value to search
     * @return return provider
     */
    public static ProvidersEnum getByValue(String providerName) {
        return Arrays
                .stream(values())
                .filter(provider -> provider.name().equalsIgnoreCase(providerName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("\"%s\" is an invalid provider.", providerName)));
    }
}
