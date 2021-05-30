package pl.kompikownia.yaml2prog.factory;

import pl.kompikownia.yaml2prog.generator.DirectoryStructureGenerator;
import pl.kompikownia.yaml2prog.generator.cpp.CppDirectoryStructureGenerator;
import pl.kompikownia.yaml2prog.generator.java.JavaDirectoryStructureGenerator;
import pl.kompikownia.yaml2prog.lang.SupportedLang;

public class DirectoryStructureGeneratorFactory {
    public static DirectoryStructureGenerator getGenerator(SupportedLang lang) {
        if(SupportedLang.JAVA.equals(lang)) {
            return new JavaDirectoryStructureGenerator();
        }
        if(SupportedLang.CPP.equals(lang)) {
            return new CppDirectoryStructureGenerator();
        }
        throw new IllegalArgumentException("Lang " + lang.name() + " is not supported");
    }
}
