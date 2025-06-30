package com.upsxace.aces_springboot_code_gen.parser.table_relationship_graph;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ColumnNode {
    private final String columnName;
    private final ColumnType columnType;
}
