package pl.kompikownia.yaml2prog.generator;

import pl.kompikownia.yaml2prog.definition.ClassDefinition;

import java.util.Map;

public interface CodeGenerator {

    String generateFile(ClassDefinition classDefinition);
    void setMainPackageName(String packageName);
    void setParameters(Map<String, Object> parameters);
    String getFileExtension();
}
