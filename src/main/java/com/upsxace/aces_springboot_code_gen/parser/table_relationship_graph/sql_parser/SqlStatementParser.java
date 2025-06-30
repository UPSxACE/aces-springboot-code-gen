package com.upsxace.aces_springboot_code_gen.parser.table_relationship_graph.sql_parser;

import com.upsxace.aces_springboot_code_gen.parser.table_relationship_graph.ColumnNode;
import com.upsxace.aces_springboot_code_gen.parser.table_relationship_graph.ColumnType;
import com.upsxace.aces_springboot_code_gen.parser.table_relationship_graph.TableNode;
import com.upsxace.aces_springboot_code_gen.parser.table_relationship_graph.sql_parser.exceptions.InvalidStatementException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlStatementParser {
    public static List<TableNode> parseStatements(List<String> rawStatements){
        var tableNodes = new ArrayList<TableNode>();

        rawStatements.forEach(statement -> {
            if(statement.toLowerCase().startsWith("create table")){
                tableNodes.add(parseCreateTable(statement));
            }

            // FIXME: add more compatible statements
        });

        return tableNodes;
    }

    private static String cleanTableName(String tableName) {
        if (tableName == null || tableName.isEmpty()) {
            return tableName;
        }

        // Remove all backticks and double quotes
        String cleaned = tableName.replace("`", "").replace("\"", "");

        // Find last dot and keep everything after it
        int lastDot = cleaned.lastIndexOf('.');
        if (lastDot != -1) {
            cleaned = cleaned.substring(lastDot + 1);
        }

        return cleaned;
    }

    private static TableNode parseCreateTable(String statement){
        String pattern = "(?i)CREATE\\s+TABLE\\s+(`[^`]+`|\"[^\"]+\"|[a-zA-Z0-9_.]+)\\s*\\((.+)\\);?";

        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(statement);

        if(!matcher.matches()){
            throw new InvalidStatementException();
        }

        String tableName = cleanTableName(matcher.group(1).trim());
        String columnDefinitions = matcher.group(2).trim();

        return new TableNode(tableName, parseColumnDefinitions(columnDefinitions));
    }

    private static ColumnNode parseColumnDefinition(String columnDefinition){
        String pattern = "^\\s*([a-zA-Z0-9_]+)\\s+([a-zA-Z0-9_()]+)\\s*(.*)$";

        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(columnDefinition.trim());

        if(!matcher.matches()){
            throw new InvalidStatementException();
        }

        String columnName = matcher.group(1).trim();
        String columnType = matcher.group(2).trim();

        return new ColumnNode(columnName, ColumnType.fromString(columnType));
    }

    private static List<ColumnNode> parseColumnDefinitions(String columnDefinitions){
        return Arrays.stream(columnDefinitions.split(","))
                .map(columnText -> parseColumnDefinition(columnText.trim()))
                .toList();
    }
}
