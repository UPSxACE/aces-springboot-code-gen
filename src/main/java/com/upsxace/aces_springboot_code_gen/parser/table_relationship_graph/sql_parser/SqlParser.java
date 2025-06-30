package com.upsxace.aces_springboot_code_gen.parser.table_relationship_graph.sql_parser;


import com.upsxace.aces_springboot_code_gen.lib.utils.StringUtils;
import com.upsxace.aces_springboot_code_gen.parser.table_relationship_graph.TableNode;
import com.upsxace.aces_springboot_code_gen.parser.table_relationship_graph.sql_parser.exceptions.UnexpectedEndOfInputException;

import java.util.ArrayList;
import java.util.List;

public class SqlParser {
    private final List<String> rawStatements;

    public SqlParser(String sqlText){
        this.rawStatements = splitSqlStatements(sqlText);
    }

    private List<String> splitSqlStatements(String sqlText) {
        // FIXME: strip away comments first!

        List<String> statements = new ArrayList<>();

        StringBuilder current = new StringBuilder();
        for (char c : sqlText.toCharArray()) {
            current.append(c);

            if (c == ';') {
                // Inline statement and clear unnecessary white spaces
                String statement = StringUtils.inline(current.toString());
                if (!statement.isEmpty()) {
                    statements.add(statement);
                }
                current.setLength(0); // Reset the StringBuilder
            }
        }

        // Handle leftover text without a semicolon
        String leftover = current.toString().trim();
        if (!leftover.isEmpty()) {
            throw new UnexpectedEndOfInputException();
        }

        return statements;
    }

    public List<TableNode> parse(){
        return SqlStatementParser.parseStatements(rawStatements);
    }
}
