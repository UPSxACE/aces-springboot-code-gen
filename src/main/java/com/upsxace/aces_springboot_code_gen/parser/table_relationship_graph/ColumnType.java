package com.upsxace.aces_springboot_code_gen.parser.table_relationship_graph;

import com.upsxace.aces_springboot_code_gen.lib.utils.StringUtils;
import com.upsxace.aces_springboot_code_gen.lib.utils.UnexpectedSwitchDefaultCaseException;
import com.upsxace.aces_springboot_code_gen.parser.table_relationship_graph.sql_parser.exceptions.InvalidColumnTypeException;

public enum ColumnType {
    BIGINT,
    BIGSERIAL,
    BIT,
    VARBIT,
    BOOL,
    CHAR,
    VARCHAR,
    DATE,
    DOUBLE,
    INTEGER,
    MONEY,
    NUMERIC,
    REAL,
    SMALLINT,
    SMALLSERIAL,
    SERIAL,
    TEXT,
    TIME,
    TIMETZ,
    TIMESTAMP,
    TIMESTAMPTZ,
    UUID;

    public static ColumnType fromString(String columnType){
        var type = StringUtils.removeAllAfter(columnType, "(");

        return switch (type) {
            case "BIGINT", "INT8" -> ColumnType.BIGINT;
            case "BIGSERIAL", "SERIAL8" -> ColumnType.BIGSERIAL;
            case "BIT" -> ColumnType.BIT;
            case "VARBIT" -> ColumnType.VARBIT;
            case "BOOL", "BOOLEAN" -> ColumnType.BOOL;
            case "CHAR", "CHARACTER" -> ColumnType.CHAR;
            case "VARCHAR" -> ColumnType.VARCHAR;
            case "DATE" -> ColumnType.DATE;
            case "DOUBLE", "FLOAT8" -> ColumnType.DOUBLE;
            case "INTEGER", "INT", "INT4" -> ColumnType.INTEGER;
            case "MONEY" -> ColumnType.MONEY;
            case "NUMERIC" -> ColumnType.NUMERIC;
            case "REAL", "FLOAT4" -> ColumnType.REAL;
            case "SMALLINT", "INT2" -> ColumnType.SMALLINT;
            case "SMALLSERIAL", "SERIAL2" -> ColumnType.SMALLSERIAL;
            case "SERIAL", "SERIAL4" -> ColumnType.SERIAL;
            case "TEXT" -> ColumnType.TEXT;
            case "TIME" -> ColumnType.TIME;
            case "TIMETZ" -> ColumnType.TIMETZ;
            case "TIMESTAMP" -> ColumnType.TIMESTAMP;
            case "TIMESTAMPTZ" -> ColumnType.TIMESTAMPTZ;
            case "UUID" -> ColumnType.UUID;
            default -> throw new InvalidColumnTypeException(type);
        };
    }

    public String evaluateToCode(){
        return switch (this.toString()) {
            case "BIGINT", "BIGSERIAL" -> "Long";
            case "SMALLINT", "SMALLSERIAL" -> "Short";
            case "INTEGER", "SERIAL" -> "Integer";
            case "DOUBLE", "REAL" -> "Double";
            case "NUMERIC", "MONEY" -> "BigDecimal";
            case "BIT", "VARBIT", "BOOL" -> "Boolean";
            case "CHAR", "VARCHAR", "TEXT" -> "String";
            case "DATE" -> "LocalDate";
            case "TIME", "TIMETZ" -> "LocalTime";
            case "TIMESTAMP", "TIMESTAMPTZ" -> "LocalDateTime";
            case "UUID" -> "UUID";
            default -> throw new UnexpectedSwitchDefaultCaseException();
        };
    }
}
