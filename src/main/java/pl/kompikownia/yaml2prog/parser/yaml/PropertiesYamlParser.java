package pl.kompikownia.yaml2prog.parser.yaml;

import lombok.val;
import pl.kompikownia.yaml2prog.definition.ClassDefinition;
import pl.kompikownia.yaml2prog.definition.FieldDefinition;
import pl.kompikownia.yaml2prog.definition.FieldType;
import pl.kompikownia.yaml2prog.exception.FileParseException;
import pl.kompikownia.yaml2prog.parser.StringUtils;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class PropertiesYamlParser implements PropertyParser {
    @Override
    public void parseProperty(ClassDefinition.ClassDefinitionBuilder classDefinitionBuilder, List<String> nested) throws IOException {
        val properties = getObjectPropertiesWithDescription(nested);
        for (val property: properties) {
            classDefinitionBuilder.field(parseField(property));
        }
        System.out.println(properties);
    }

    private FieldDefinition parseField(String field) throws IOException {
        val tokenizer = new StreamTokenizer(new StringReader(field));
        val builder = FieldDefinition.builder();
        tokenizer.nextToken();
        builder.fieldName(tokenizer.sval);
        tokenizer.nextToken();
        if(tokenizer.ttype != ':') {
            throw new FileParseException();
        }
        tokenizer.nextToken();
        while(tokenizer.ttype != StreamTokenizer.TT_EOF) {
            val propertyName = tokenizer.sval;
            tokenizer.nextToken();
            if(tokenizer.ttype != ':') {
                throw new FileParseException();
            }
            tokenizer.nextToken();
            val propertyValue = tokenizer.sval;
            parseTypeProperty(builder, propertyName, propertyValue);
            tokenizer.nextToken();
        }
        return builder.build();
    }

    private void parseTypeProperty(FieldDefinition.FieldDefinitionBuilder builder, String propertyName, String propertyValue) {
        if (propertyName.equals("type")) {
           builder.type(FieldType.valueOf(propertyValue.toUpperCase()));
        }
        if (propertyName.equals("ref")) {
            builder.refName(propertyValue);
        }
    }

    private List<String> getObjectPropertiesWithDescription(List<String> lines) {
        List<String> objectProperties = new ArrayList<>();
        StringBuilder objectPropertiesDescription = new StringBuilder();
        int baseIndent = StringUtils.countIndent(lines.get(0));
        for (int i = 0; i < lines.size(); i++) {
            val indent = StringUtils.countIndent(lines.get(i));
            if (indent == baseIndent && i != 0) {
                objectProperties.add(objectPropertiesDescription.toString());
                objectPropertiesDescription = new StringBuilder();
                objectPropertiesDescription.append(lines.get(i));
            } else {
                objectPropertiesDescription.append(lines.get(i));
            }
        }
        objectProperties.add(objectPropertiesDescription.toString());
        return objectProperties;
    }
}
