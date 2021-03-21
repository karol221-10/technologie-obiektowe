package pl.kompikownia.yaml2prog.definition;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ClassDefinition {
    private String name;
    private List<FieldDefinition> fields;
}
