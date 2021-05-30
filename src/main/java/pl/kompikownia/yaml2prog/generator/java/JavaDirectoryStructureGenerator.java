package pl.kompikownia.yaml2prog.generator.java;

import lombok.val;
import pl.kompikownia.yaml2prog.generator.DirectoryStructureGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JavaDirectoryStructureGenerator implements DirectoryStructureGenerator {
    @Override
    public String generateDirectoryStructure(String packageName) throws IOException {
        val folders = packageName.replace(".", "\\");
        val endPath = Paths.get("target\\src\\main\\java\\"+folders);
        Files.createDirectories(endPath);
        return endPath.toString();
    }
}
