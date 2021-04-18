package pl.kompikownia.yaml2prog.definition;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Getter
@Builder
@EqualsAndHashCode
public class ClassDefinition {
    private String name;
    private String path;
    private ClassDefinition parentClass;
    @Singular
    private List<FieldDefinition> fields;
}
