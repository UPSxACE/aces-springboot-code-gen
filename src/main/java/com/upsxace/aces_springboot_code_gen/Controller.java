package com.upsxace.aces_springboot_code_gen;

import com.upsxace.aces_springboot_code_gen.parser.code_generator.GeneratedCodeDto;
import com.upsxace.aces_springboot_code_gen.parser.ParserService;
import com.upsxace.aces_springboot_code_gen.parser.SqlToCodeRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class Controller {
    private final ParserService parserService;

    @PostMapping("/sql-to-code")
    GeneratedCodeDto sqlToCode(@Valid @RequestBody SqlToCodeRequest request){
        return parserService.parseSql(request);
    }
}
