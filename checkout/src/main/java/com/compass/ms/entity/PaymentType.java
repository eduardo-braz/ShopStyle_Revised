package com.compass.ms.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;

public enum PaymentType {
    CREDIT_CARD,
    DEBIT,
    BANK_TRANSFER,
    TICKET,
    PIX;

    @JsonCreator
    public static PaymentType setValue(String key) {
        String typeKey = key.replaceFirst(" ", "_");
        return Arrays.stream(PaymentType.values())
                .filter(exampleEnum -> exampleEnum.toString().equals(typeKey.toUpperCase()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Invalid value '" + key + "'"));
    }

}
