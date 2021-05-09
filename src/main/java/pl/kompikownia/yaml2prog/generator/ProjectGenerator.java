package pl.kompikownia.yaml2prog.generator;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ProjectGenerator {
    String generateProjectFileContent(String templateName, Map<String, String> placeholders) throws IOException;
}
