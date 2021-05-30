package pl.kompikownia.yaml2prog.generator;

import java.io.IOException;

public interface DirectoryStructureGenerator {
    String generateDirectoryStructure(String packageName) throws IOException;
}
