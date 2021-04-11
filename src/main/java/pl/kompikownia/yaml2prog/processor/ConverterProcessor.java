package pl.kompikownia.yaml2prog.processor;

import lombok.SneakyThrows;
import lombok.val;
import pl.kompikownia.yaml2prog.factory.CodeGeneratorFactory;
import pl.kompikownia.yaml2prog.factory.ParserFactory;
import pl.kompikownia.yaml2prog.lang.FileFormat;
import pl.kompikownia.yaml2prog.lang.SupportedLang;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConverterProcessor {

    @SneakyThrows
    public void convert(String filename, String packageName, SupportedLang destinationLang, FileFormat fileFormat) {
        val parser = ParserFactory.getParser(fileFormat);
        val codeGenerator = CodeGeneratorFactory.getGenerator(destinationLang);
        codeGenerator.setMainPackageName(packageName);
        val parsedFile = parser.parseFile(filename);
        val fileContent = codeGenerator.generateFile(parsedFile);
        val path = generateDirectoryStructure(packageName);
        saveGeneratedFile(fileContent, parsedFile.getName() + codeGenerator.getFileExtension(), path);
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
