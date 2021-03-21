package pl.kompikownia.yaml2prog.parser;

import lombok.val;
import pl.kompikownia.yaml2prog.definition.ClassDefinition;
import pl.kompikownia.yaml2prog.exception.FileParseException;

import java.io.*;

public class YamlParser implements Parser{

    @Override
    public ClassDefinition parseFile(String filename) throws IOException {
        val classDefinitionBuilder = ClassDefinition.builder();
        val fileReader = new FileReader(filename);
        val streamTokenizer = new StreamTokenizer(fileReader);
        classDefinitionBuilder.name(getClassName(streamTokenizer));
        return classDefinitionBuilder.build();
    }

    private String getClassName(StreamTokenizer tokenizer) throws IOException {
        return getProperty("name", tokenizer);
    }

    private String getProperty(String propertyName, StreamTokenizer tokenizer) throws IOException {
        tokenizer.nextToken();
        if(!tokenizer.sval.equals(propertyName)) {
            throw new FileParseException();
        }
        tokenizer.nextToken();
        if (tokenizer.ttype != ':') {
           throw new FileParseException();
        }
        tokenizer.nextToken();
        return tokenizer.sval;
    }
}
