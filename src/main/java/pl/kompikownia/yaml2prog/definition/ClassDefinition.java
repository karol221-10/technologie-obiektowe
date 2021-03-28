package pl.kompikownia.yaml2prog.definition;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Getter
@Builder
public class ClassDefinition {
    private String name;
    private String inheritFileName;
    @Singular
    private List<FieldDefinition> fields;
}
