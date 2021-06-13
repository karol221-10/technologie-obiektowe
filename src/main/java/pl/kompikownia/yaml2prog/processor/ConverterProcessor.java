package pl.kompikownia.yaml2prog.processor;

import lombok.SneakyThrows;
import lombok.val;
import pl.kompikownia.yaml2prog.definition.ClassDefinition;
import pl.kompikownia.yaml2prog.definition.FieldDefinition;
import pl.kompikownia.yaml2prog.definition.FieldType;
import pl.kompikownia.yaml2prog.factory.CodeGeneratorFactory;
import pl.kompikownia.yaml2prog.factory.DirectoryStructureGeneratorFactory;
import pl.kompikownia.yaml2prog.factory.ParserFactory;
import pl.kompikownia.yaml2prog.generator.CodeGenerator;
import pl.kompikownia.yaml2prog.lang.FileFormat;
import pl.kompikownia.yaml2prog.lang.SupportedLang;
import pl.kompikownia.yaml2prog.parser.Parser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConverterProcessor {

    @SneakyThrows
    public void convert(String filename, String packageName, SupportedLang destinationLang, FileFormat fileFormat, Map<String, Object> parameters) {
        val parser = ParserFactory.getParser(fileFormat);
        val codeGenerator = CodeGeneratorFactory.getGenerator(destinationLang);
        codeGenerator.setMainPackageName(packageName);
        codeGenerator.setParameters(parameters);
        val parsedFiles = parseFileAndRelatedFiles(parser, filename);
        val path = DirectoryStructureGeneratorFactory.getGenerator(destinationLang).generateDirectoryStructure(packageName);
        parsedFiles.forEach(parsedFile -> {
            try {
                saveFile(parsedFile, codeGenerator, path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    List<ClassDefinition> parseFileAndRelatedFiles(Parser parser, String filename) throws IOException {
        List<ClassDefinition> parsedFileList = new ArrayList<>();
        ClassDefinition parsedFile = parser.parseFile(filename);
        parsedFileList.add(parsedFile);
        parsedFileList.addAll(getObjectFields(parsedFile));
        while (parsedFile.getParentClass() != null) {
            parsedFile = parsedFile.getParentClass();
            parsedFileList.add(parsedFile);
            parsedFileList.addAll(getObjectFields(parsedFile));
        }
        return parsedFileList.stream().distinct().collect(Collectors.toList());
    }

    private List<ClassDefinition> getObjectFields(ClassDefinition classDefinition) {
        return classDefinition.getFields().stream()
                .filter(fieldDefinition -> fieldDefinition.getType().equals(FieldType.OBJECT))
                .map(FieldDefinition::getRefClass)
                .distinct()
                .collect(Collectors.toList());
    }

    private void saveFile(ClassDefinition classDefinition, CodeGenerator codeGenerator, String path) throws IOException {
        val fileContent = codeGenerator.generateFile(classDefinition);
        saveGeneratedFile(fileContent, classDefinition.getName() + codeGenerator.getFileExtension(), path);
    }

    private void saveGeneratedFile(String fileContent, String generatedFileName, String generatedFilePath) throws IOException {
        val fileOutputStream = new FileOutputStream(generatedFilePath+"\\" + generatedFileName);
        fileOutputStream.write(fileContent.getBytes(StandardCharsets.UTF_8));
        fileOutputStream.close();
    }
}
