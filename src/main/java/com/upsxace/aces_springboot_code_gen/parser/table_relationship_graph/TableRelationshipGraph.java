package com.upsxace.aces_springboot_code_gen.parser.table_relationship_graph;

import com.upsxace.aces_springboot_code_gen.parser.SqlToCodeRequest;
import com.upsxace.aces_springboot_code_gen.parser.table_relationship_graph.sql_parser.SqlParser;
import lombok.Getter;

import java.util.List;

@Getter
public class TableRelationshipGraph {
    private List<TableNode> tableNodes;

    public static TableRelationshipGraph fromSql(SqlToCodeRequest request){
        var graph = new TableRelationshipGraph();
        var parser = new SqlParser(request.getSql());
        graph.tableNodes = parser.parse();
        return graph;
    }
}
