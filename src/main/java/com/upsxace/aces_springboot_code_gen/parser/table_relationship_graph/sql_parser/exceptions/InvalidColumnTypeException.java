package com.upsxace.aces_springboot_code_gen.parser.table_relationship_graph.sql_parser.exceptions;

import com.upsxace.aces_springboot_code_gen.error.BadRequestException;

public class InvalidColumnTypeException extends BadRequestException {
    public InvalidColumnTypeException(String typeName) {
        super("Unknown column type: " + typeName);
    }
}
