package com.upsxace.aces_springboot_code_gen.parser;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SqlToCodeRequest {
    @NotBlank(message = "The SQL field is required")
    private final String sql;
}
