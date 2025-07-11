package com.upsxace.aces_springboot_code_gen.parser.code_generator;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GeneratedCodeDto {
    private final List<GeneratedCode> code;
}
