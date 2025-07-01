package com.upsxace.aces_springboot_code_gen.parser.code_generator;

import com.upsxace.aces_springboot_code_gen.lib.utils.StringUtils;
import com.upsxace.aces_springboot_code_gen.parser.table_relationship_graph.ColumnNode;
import com.upsxace.aces_springboot_code_gen.parser.table_relationship_graph.TableRelationshipGraph;

import java.util.ArrayList;
import java.util.List;

public class CodeGenerator {
    public static List<GeneratedCode> generateCodeFromGraph(TableRelationshipGraph graph){
        var code = new ArrayList<GeneratedCode>();

        graph.getTableNodes().forEach(t -> {
            var entity = new StringBuilder();
            var entityName = StringUtils.toPascalCase(t.getTableName());
            var entityIdType = "Long";

            entity.append(
                    String.format(
                            """
                            import jakarta.persistence.Column;
                            import jakarta.persistence.Entity;
                            import jakarta.persistence.GeneratedValue;
                            import jakarta.persistence.GenerationType;
                            import jakarta.persistence.Id;
                            import jakarta.persistence.Table;
                            import lombok.Getter;
                            import lombok.Setter;
                            
                            @Getter
                            @Setter
                            @Table(name = "%s")
                            @Entity
                            public class %s {
                            """, t.getTableName(), entityName
                    )
            );

            var i = 0;
            for(ColumnNode c : t.getColumnNodes()){
                if(i == 0) {
                    // assume first column is the ID column // FIXME: accurately detect primary key type
                    entityIdType = c.getColumnType().evaluateToCode();
                    entity.append(
                            String.format("""
                                          \t@Id
                                          \t@GeneratedValue(strategy = %s)
                                          """, entityIdType.equals("UUID") ? "GenerationType.UUID" : "GenerationType.IDENTITY"
                            )
                    );
                }

                if(i > 0)
                    entity.append("\n");

                entity.append(
                        String.format(
                                """
                                \t@Column(name = "%s")
                                \tprivate %s %s;
                                """,
                                c.getColumnName(), c.getColumnType().evaluateToCode(), StringUtils.toCamelCase(c.getColumnName())
                        )
                );

                i++;
            }

            entity.append("}");

            var repository = String.format(
                    """
                    import org.springframework.data.jpa.repository.JpaRepository;
                    
                    public interface %sRepository extends JpaRepository<%s, %s> {}""",
                    entityName, entityName, entityIdType
            );

            code.add(new GeneratedCode(entity.toString(), repository));
        });

        return code;
    }
}
