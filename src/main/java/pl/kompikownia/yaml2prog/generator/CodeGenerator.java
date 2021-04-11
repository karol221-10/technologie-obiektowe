package pl.kompikownia.yaml2prog.generator;

import pl.kompikownia.yaml2prog.definition.ClassDefinition;

public interface CodeGenerator {

    String generateFile(ClassDefinition classDefinition);
    void setMainPackageName(String packageName);
    String getFileExtension();
}
