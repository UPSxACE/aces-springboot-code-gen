package com.upsxace.aces_springboot_code_gen.parser.table_relationship_graph;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TableNode {
    private final String tableName;
    private final List<ColumnNode> columnNodes;
}
