package pl.kompikownia.yaml2prog.parser.yaml;

import pl.kompikownia.yaml2prog.definition.ClassDefinition;

import java.io.IOException;
import java.util.List;

public interface PropertyParser {
    void parseProperty(ClassDefinition.ClassDefinitionBuilder classDefinitionBuilder, List<String> nested) throws IOException;
}
