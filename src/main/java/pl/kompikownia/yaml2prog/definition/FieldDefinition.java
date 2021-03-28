package pl.kompikownia.yaml2prog.definition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
@Builder
@EqualsAndHashCode
public class FieldDefinition {
    private String fieldName;
    private FieldType type;
    private String refName;
}
