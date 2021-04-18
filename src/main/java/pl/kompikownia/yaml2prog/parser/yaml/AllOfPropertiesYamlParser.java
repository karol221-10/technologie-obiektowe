package pl.kompikownia.yaml2prog.parser.yaml;

import lombok.val;
import pl.kompikownia.yaml2prog.definition.ClassDefinition;
import pl.kompikownia.yaml2prog.exception.FileParseException;
import pl.kompikownia.yaml2prog.parser.StringUtils;
import pl.kompikownia.yaml2prog.parser.YamlParser;
import pl.kompikownia.yaml2prog.parser.yaml.allOf.PropertyType;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.List;

public class AllOfPropertiesYamlParser implements PropertyParser {
    @Override
    public void parseProperty(ClassDefinition.ClassDefinitionBuilder classDefinitionBuilder, List<String> nested) throws IOException {
        val halfBuildedClassDefinition = classDefinitionBuilder.build();
        for (int i = 0; i <= nested.size(); i++) {
            val line = nested.get(i);
            val parseResult = parseLine(line);
            if (parseResult[0] == PropertyType.REF) {
               processRefResult(classDefinitionBuilder, halfBuildedClassDefinition.getPath(), (String) parseResult[1]);
            }
            if (parseResult[0] == PropertyType.PROPERTIES) {
                val nestedProperties = StringUtils.readAllNestedFields(nested, i + 1);
                i += nested.size();
                processPropertiesResult(classDefinitionBuilder, nestedProperties);
            }
        }
    }

    private Object[] parseLine(String line) throws IOException {
        val tokenizer = new StreamTokenizer(new StringReader(line));
        tokenizer.nextToken();
        if (tokenizer.ttype == 36) {
            tokenizer.nextToken();
        }
        if ("ref".equals(tokenizer.sval)) {
            tokenizer.nextToken();
            if(tokenizer.ttype != ':') {
                throw new FileParseException();
            }
            tokenizer.nextToken();
            return new Object[] {PropertyType.REF, tokenizer.sval};
        }
        if ("properties".equals(tokenizer.sval)) {
            return new Object[]{PropertyType.PROPERTIES};
        }
        throw new FileParseException();
    }

    private void processRefResult(ClassDefinition.ClassDefinitionBuilder builder, String homePath, String refClassName) throws IOException {
        val yamlParser = new YamlParser();
        val classDefinition = yamlParser.parseFile(homePath + refClassName);
        builder.parentClass(classDefinition);
    }

    private void processPropertiesResult(ClassDefinition.ClassDefinitionBuilder builder, List<String> properties) throws IOException {
        val yamlPropertyParser = new PropertiesYamlParser();
        yamlPropertyParser.parseProperty(builder, properties);
    }
}