package com.upsxace.aces_springboot_code_gen.parser.table_relationship_graph.sql_parser.exceptions;

public class InvalidAlterTableStatementException extends InvalidStatementException {
    public InvalidAlterTableStatementException() {
        super("Invalid alter table sql statement");
    }

    public InvalidAlterTableStatementException(String message) {
        super(message);
    }
}
