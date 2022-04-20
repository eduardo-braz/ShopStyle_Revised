package com.compass.fasttracker.history.exceptions.response;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ResponseError {

    private String field;
    private String error;

    @Override
    public String toString() {
        return "{" +
                "\"field\": \"" + field + "\",\n" +
                "\"error\": \"" + error + "\"\n" +
                "}";
    }
}