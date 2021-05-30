package pl.kompikownia.yaml2prog.generator.cpp;

import lombok.val;
import pl.kompikownia.yaml2prog.generator.DirectoryStructureGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CppDirectoryStructureGenerator implements DirectoryStructureGenerator {
    @Override
    public String generateDirectoryStructure(String packageName) throws IOException {
        val endPath = Paths.get("target");
        Files.createDirectories(endPath);
        return endPath.toString();
    }
}
