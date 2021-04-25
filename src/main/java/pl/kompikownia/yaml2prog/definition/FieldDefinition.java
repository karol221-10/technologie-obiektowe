package pl.kompikownia.yaml2prog.definition;

import lombok.*;

@Getter
@AllArgsConstructor(staticName = "of")
@Builder
@ToString
@EqualsAndHashCode
public class FieldDefinition {
    private String fieldName;
    private FieldType type;
    private ClassDefinition refClass;
}
