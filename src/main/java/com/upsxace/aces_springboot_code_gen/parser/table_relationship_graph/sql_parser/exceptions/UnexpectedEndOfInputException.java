package com.upsxace.aces_springboot_code_gen.parser.table_relationship_graph.sql_parser.exceptions;

import com.upsxace.aces_springboot_code_gen.error.BadRequestException;

public class UnexpectedEndOfInputException extends BadRequestException {
    public UnexpectedEndOfInputException() {
        super("Unexpected end of input");
    }
}
