package com.upsxace.aces_springboot_code_gen.parser.table_relationship_graph;

import com.upsxace.aces_springboot_code_gen.lib.utils.StringUtils;
import com.upsxace.aces_springboot_code_gen.lib.utils.UnexpectedSwitchDefaultCaseException;
import com.upsxace.aces_springboot_code_gen.parser.table_relationship_graph.sql_parser.exceptions.InvalidColumnTypeException;

public enum ColumnType {
    // FIXME: add more types
    BIGINT,
    VARCHAR;

    public static ColumnType fromString(String columnType){
        var type = StringUtils.removeAllAfter(columnType, "(");

        return switch (type){
            case "BIGINT" -> ColumnType.BIGINT;
            case "VARCHAR" -> ColumnType.VARCHAR;
            default -> throw new InvalidColumnTypeException(type);
        };
    }

    public String evaluateToCode(){
        return switch (this.toString()){
            case "BIGINT" -> "Long";
            case "VARCHAR" -> "String";
            default -> throw new UnexpectedSwitchDefaultCaseException();
        };
    }
}
