package pl.kompikownia.yaml2prog.definition;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class FieldDefinition {
    private String fieldName;
    private FieldType type;
}
