package com.compass.ms.catalog.exceptions.ResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvalidOperationDTO {

    private String message;
    private String fieldName;
    private String value;

    public InvalidOperationDTO(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "{" +
                "\"message\": \"" + this.message + "\",\n" +
                "\""+this.fieldName+"\": \"" + this.value + "\"\n" +
                "}";
    }
}
