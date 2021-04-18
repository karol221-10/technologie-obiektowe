package pl.kompikownia.yaml2prog.parser;

import lombok.val;
import pl.kompikownia.yaml2prog.definition.ClassDefinition;
import pl.kompikownia.yaml2prog.exception.FileParseException;
import pl.kompikownia.yaml2prog.parser.yaml.PropertyParserFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class YamlParser implements Parser {

    private List<String> readAllFileLinesToList(String filename) throws FileNotFoundException {
        List<String> fileLines = new ArrayList<>();
        val fileReader = new FileReader(filename);
        val scanner = new Scanner(fileReader);
        while(scanner.hasNext()) {
            fileLines.add(scanner.nextLine());
        }
        return fileLines;
    }

    @Override
    public ClassDefinition parseFile(String filename) throws IOException {
        val classDefinitionBuilder = ClassDefinition.builder();
        classDefinitionBuilder.path(getPathWithoutFilename(filename));
        val lines = readAllFileLinesToList(filename);
        int linePointer = 0;
        val propertyParserFactory = new PropertyParserFactory();
        while (linePointer != lines.size()) {
            val readString = lines.get(linePointer++);
            val indent = StringUtils.countIndent(readString);
            if(indent != 0) {
                throw new FileParseException();
            }
            val streamTokenizer = new StreamTokenizer(new StringReader(readString));
            streamTokenizer.nextToken();
            val propertyName = streamTokenizer.sval;
            streamTokenizer.nextToken();
            if(streamTokenizer.ttype != ':') {
                throw new FileParseException();
            }
            val propertyParser = propertyParserFactory.getPropertyParserFactory(propertyName);
            List<String> nested = StringUtils.readAllNestedFields(lines, linePointer);
            linePointer += nested.size();
            if(nested.isEmpty()) {
                streamTokenizer.nextToken();
                nested = List.of(streamTokenizer.sval);
            }
            propertyParser.parseProperty(classDefinitionBuilder, nested);
        }
        return classDefinitionBuilder.build();
    }


    private String getPathWithoutFilename(String fullPath) {
        val splittedPath = fullPath.split("/");
        StringBuilder pathAfterDivide = new StringBuilder();
        for(int i = 0;i < splittedPath.length - 1; i++) {
            pathAfterDivide.append(splittedPath[i]).append("/");
        }
        return pathAfterDivide.toString();
    }
}
