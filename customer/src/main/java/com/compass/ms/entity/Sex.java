package com.compass.ms.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum Sex {
    Masculino, Feminino;

    @JsonCreator
    public static Sex setValue(String key) {
        return Arrays.stream(Sex.values())
                .filter(exampleEnum -> exampleEnum.toString().equalsIgnoreCase(key))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Invalid value '" + key + "'"));
    }
}
