package com.compass.ms.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class TokenDTO implements Serializable {

    private String token;
    private String type;
}
