package pl.kompikownia.yaml2prog.definition;

import lombok.*;

import java.util.List;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class ClassDefinition {
    private String name;
    private String path;
    private ClassDefinition parentClass;
    @Singular
    private List<FieldDefinition> fields;
}
