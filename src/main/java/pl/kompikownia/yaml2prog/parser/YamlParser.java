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
            List<String> nested = readAllNestedFields(lines, linePointer);
            linePointer += nested.size();
            if(nested.isEmpty()) {
                streamTokenizer.nextToken();
                nested = List.of(streamTokenizer.sval);
            }
            propertyParser.parseProperty(classDefinitionBuilder, nested);
        }
        return classDefinitionBuilder.build();
    }

    private List<String> readAllNestedFields(List<String> lines, int linePointer) {
        List<String> fileLines = new ArrayList<>();
        boolean endOfWork = false;
        while(!endOfWork) {
            val line = lines.get(linePointer++);
            val indent = StringUtils.countIndent(line);
            if(indent > 0) {
                fileLines.add(line);
            }
            if(lines.size() == linePointer || indent == 0) {
                endOfWork = true;
            }
        }
        return fileLines;
    }
}
