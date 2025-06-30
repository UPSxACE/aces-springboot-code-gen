package com.upsxace.aces_springboot_code_gen.parser;

import com.upsxace.aces_springboot_code_gen.parser.table_relationship_graph.TableRelationshipGraph;
import org.springframework.stereotype.Service;

@Service
public class ParserService {
    public GeneratedCodeDto parseSql(SqlToCodeRequest request){
        var graph = TableRelationshipGraph.fromSql(request);
        return new GeneratedCodeDto(CodeGenerator.generateCodeFromGraph(graph));
    }
}
