package pl.kompikownia.yaml2prog.parser.yaml;

import pl.kompikownia.yaml2prog.definition.ClassDefinition;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.List;

public class NameYamlParser implements PropertyParser {

    @Override
    public void parseProperty(ClassDefinition.ClassDefinitionBuilder classDefinitionBuilder, List<String> nested) throws IOException {
        classDefinitionBuilder.name(nested.get(0));
    }
}
