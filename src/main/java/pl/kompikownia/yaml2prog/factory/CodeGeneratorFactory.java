package pl.kompikownia.yaml2prog.factory;

import pl.kompikownia.yaml2prog.generator.CodeGenerator;
import pl.kompikownia.yaml2prog.generator.java.JavaCodeGenerator;
import pl.kompikownia.yaml2prog.lang.SupportedLang;

public class CodeGeneratorFactory {

    public static CodeGenerator getGenerator(SupportedLang lang) {
        if(SupportedLang.JAVA.equals(lang)) {
            return new JavaCodeGenerator();
        }
        throw new IllegalArgumentException("Lang " + lang.name() + " is not supported");
    }
}
