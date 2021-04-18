package pl.kompikownia.yaml2prog.processor;

import lombok.SneakyThrows;
import lombok.val;
import pl.kompikownia.yaml2prog.definition.ClassDefinition;
import pl.kompikownia.yaml2prog.exception.FileParseException;
import pl.kompikownia.yaml2prog.factory.CodeGeneratorFactory;
import pl.kompikownia.yaml2prog.factory.ParserFactory;
import pl.kompikownia.yaml2prog.generator.CodeGenerator;
import pl.kompikownia.yaml2prog.lang.FileFormat;
import pl.kompikownia.yaml2prog.lang.SupportedLang;
import pl.kompikownia.yaml2prog.parser.Parser;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConverterProcessor {

    @SneakyThrows
    public void convert(String filename, String packageName, SupportedLang destinationLang, FileFormat fileFormat) {
        val parser = ParserFactory.getParser(fileFormat);
        val codeGenerator = CodeGeneratorFactory.getGenerator(destinationLang);
        codeGenerator.setMainPackageName(packageName);
        val parsedFiles = parseFileAndRelatedFiles(parser, filename);
        val path = generateDirectoryStructure(packageName);
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
        while (parsedFile.getParentClass() != null) {
            parsedFile = parsedFile.getParentClass();
            parsedFileList.add(parsedFile);
        }
        return parsedFileList.stream().distinct().collect(Collectors.toList());
    }

    private void saveFile(ClassDefinition classDefinition, CodeGenerator codeGenerator, String path) throws IOException {
        val fileContent = codeGenerator.generateFile(classDefinition);
        saveGeneratedFile(fileContent, classDefinition.getName() + codeGenerator.getFileExtension(), path);
    }

    private String generateDirectoryStructure(String packageName) throws IOException {
        val folders = packageName.replace(".", "\\");
        val endPath = Paths.get("target\\"+folders);
        Files.createDirectories(endPath);
        return endPath.toString();
    }

    private void saveGeneratedFile(String fileContent, String generatedFileName, String generatedFilePath) throws IOException {
        val fileOutputStream = new FileOutputStream(generatedFilePath+"\\" + generatedFileName);
        fileOutputStream.write(fileContent.getBytes(StandardCharsets.UTF_8));
        fileOutputStream.close();
    }
}
