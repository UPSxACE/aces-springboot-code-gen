package com.upsxace.aces_springboot_code_gen.parser.table_relationship_graph.sql_parser;

import com.upsxace.aces_springboot_code_gen.lib.utils.StringUtils;
import com.upsxace.aces_springboot_code_gen.parser.table_relationship_graph.ColumnNode;
import com.upsxace.aces_springboot_code_gen.parser.table_relationship_graph.ColumnType;
import com.upsxace.aces_springboot_code_gen.parser.table_relationship_graph.TableNode;
import com.upsxace.aces_springboot_code_gen.parser.table_relationship_graph.sql_parser.exceptions.InvalidAlterTableStatementException;
import com.upsxace.aces_springboot_code_gen.parser.table_relationship_graph.sql_parser.exceptions.InvalidStatementException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SqlStatementParser {
    public static List<TableNode> parseStatements(List<String> rawStatements){
        var tableNodes = new ArrayList<TableNode>();

        rawStatements.forEach(statement -> {
            if(statement.toLowerCase().startsWith("create table"))
                tableNodes.add(parseCreateTable(statement));
            if(statement.toLowerCase().startsWith("alter table"))
                parseAlterTable(statement, tableNodes);
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

    private static String cleanColumnName(String columnName) {
        if (columnName == null || columnName.isEmpty()) {
            return columnName;
        }

        // Remove all backticks and double quotes
        return columnName.replace("`", "").replace("\"", "");
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

        String columnName = cleanColumnName(matcher.group(1).trim());
        String columnType = matcher.group(2).trim().toUpperCase();

        return new ColumnNode(columnName, ColumnType.fromString(columnType));
    }

    private static List<ColumnNode> parseColumnDefinitions(String columnDefinitions){
        return StringUtils.splitIgnoringParentheses(columnDefinitions)
                .stream()
                .filter(columnText -> {
                    if(columnText.toLowerCase().startsWith("constraint"))
                        return false;
                    if(columnText.toLowerCase().startsWith("primary"))
                        return false;
                    if(columnText.toLowerCase().startsWith("foreign"))
                        return false;
                    if(columnText.toLowerCase().startsWith("unique"))
                        return false;
                    if(columnText.toLowerCase().startsWith("check"))
                        return false;
                    return !columnText.toLowerCase().startsWith("exclude");
                })
                .map(columnText -> parseColumnDefinition(columnText.trim()))
                .collect(Collectors.toList());
    }

    private static void parseAlterTable(String statement, List<TableNode> tableNodes){
        String pattern = "(?i)^\\s*alter\\s+table\\s+(.+?)\\s+(\\w+)(?:\\s+column)?\\s+(.+)\\s*$"; // "alter table {table} {verb} COLUMN {anything}", COLUMN is optional
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(statement);

        if(!matcher.matches())
            return;

        String tableName = cleanTableName(matcher.group(1).trim());
        String verb = matcher.group(2).trim().toLowerCase();
        String update = matcher.group(3).trim(); // everything after "COLUMN"

        var tableToUpdate = tableNodes
                .stream()
                .filter(n -> n.getTableName().equals(tableName))
                .findFirst()
                .orElseThrow(() -> new InvalidAlterTableStatementException("Invalid alter table sql statement: target table not found"));

        switch (verb) {
            case "add" -> {
                if(update.toLowerCase().startsWith("constraint"))
                    return; // not an add COLUMN statement

                String add_pattern = "^\\s*([`\"a-zA-Z0-9_]+)\\s+([a-zA-Z0-9]+)\\s*(.*)?$"; // "{column} {type}{anything/;}"
                Pattern add_regex = Pattern.compile(add_pattern);
                Matcher add_matcher = add_regex.matcher(update);
                if(!add_matcher.matches())
                    throw new InvalidStatementException();

                String columnName = cleanColumnName(add_matcher.group(1).trim());
                String columnType = add_matcher.group(2).trim().toUpperCase();

                tableToUpdate.getColumnNodes().add(new ColumnNode(columnName, ColumnType.fromString(columnType)));
            }
            case "drop" -> {
                String drop_pattern = "^\\s*([`\"a-zA-Z0-9_]+)\\s*(.*)?$"; // "{column};"
                Pattern drop_regex = Pattern.compile(drop_pattern);
                Matcher drop_matcher = drop_regex.matcher(update);
                if(!drop_matcher.matches())
                    throw new InvalidStatementException();

                String columnName = cleanColumnName(drop_matcher.group(1).trim());
                var columnToUpdate = tableToUpdate
                        .getColumnNodes()
                        .stream()
                        .filter(c -> c.getColumnName().equals(columnName))
                        .findFirst()
                        .orElseThrow(() -> new InvalidAlterTableStatementException("Invalid alter table sql statement: target column not found"));

                tableToUpdate.getColumnNodes().remove(columnToUpdate);
            }
            case "rename" -> {
                String rename_pattern = "(?i)^\\s*([`\"a-zA-Z0-9_]+)\\s+to\\s+([`\"a-zA-Z0-9_]+)\\s*;?\\s*$"; // "{column} TO {column};"
                Pattern rename_regex = Pattern.compile(rename_pattern);
                Matcher rename_matcher = rename_regex.matcher(update);
                if(!rename_matcher.matches())
                    throw new InvalidStatementException();

                String oldColumnName = cleanColumnName(rename_matcher.group(1).trim());
                String newColumnName = cleanColumnName(rename_matcher.group(2).trim());
                var columnToUpdate = tableToUpdate
                        .getColumnNodes()
                        .stream()
                        .filter(c -> c.getColumnName().equals(oldColumnName))
                        .findFirst()
                        .orElseThrow(() -> new InvalidAlterTableStatementException("Invalid alter table sql statement: target column not found"));

                columnToUpdate.setColumnName(newColumnName);
            }
            case "alter" -> {
                String alter_type_pattern = "(?i)^\\s*([`\"a-zA-Z0-9_]+)\\s+type\\s+([a-zA-Z0-9]+)\\s*(.*)?$"; // "{column} TYPE {type} {anything/;}"
                Pattern alter_type_regex = Pattern.compile(alter_type_pattern);
                Matcher alter_type_matcher = alter_type_regex.matcher(update);
                if(!alter_type_matcher.matches())
                    return; // not an alter TYPE statement

                String columnName = cleanColumnName(alter_type_matcher.group(1).trim());
                String columnType = alter_type_matcher.group(2).trim().toUpperCase();
                var columnToUpdate = tableToUpdate
                        .getColumnNodes()
                        .stream()
                        .filter(c -> c.getColumnName().equals(columnName))
                        .findFirst()
                        .orElseThrow(() -> new InvalidAlterTableStatementException("Invalid alter table sql statement: target column not found"));

                columnToUpdate.setColumnType(ColumnType.fromString(columnType));
            }
        }
    }
}
