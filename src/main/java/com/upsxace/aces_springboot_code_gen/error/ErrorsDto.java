package com.upsxace.aces_springboot_code_gen.error;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class ErrorsDto {
    private final Map<String, String> errors = new HashMap<>();

    public void addError(String field, String message){
        errors.put(field, message);
    }
}
