package pl.kompikownia.yaml2prog.yamlparser;

import lombok.val;
import org.junit.jupiter.api.Test;
import pl.kompikownia.yaml2prog.definition.ClassDefinition;
import pl.kompikownia.yaml2prog.definition.FieldDefinition;
import pl.kompikownia.yaml2prog.definition.FieldType;
import pl.kompikownia.yaml2prog.generator.cpp.CppCodeGenerator;

import java.io.IOException;

public class CppCodeGeneratorTest {

    @Test
    public void shouldGenerateCorrectCppFile() throws IOException {
        // given
        System.out.println(50 + 30 + "test");
        val codeGenerator = new CppCodeGenerator();
        codeGenerator.setMainPackageName("pl.kompikownia.yaml2prog.dto");
        // when
        val result = codeGenerator.generateFile(buildPersonClassDefinition());
        // then
        System.out.println(result);
    }

    @Test
    public void shouldGenerateCorrectCppFileWithGettersSetters() throws IOException {
        // given
        val codeGenerator = new CppCodeGenerator();
        codeGenerator.setMainPackageName("pl.kompikownia.yaml2prog.dto");
        codeGenerator.setGenerateGettersSetters(true);
        // when
        val result = codeGenerator.generateFile(buildPersonClassDefinition());
        // then
        System.out.println(result);
    }

    private ClassDefinition buildPersonClassDefinition() {
        return ClassDefinition.builder()
                .name("Person")
                .field(FieldDefinition.of("name", FieldType.STRING, false,null))
                .field(FieldDefinition.of("surname", FieldType.STRING, false, null))
                .field(FieldDefinition.of("age", FieldType.NUMBER, false,null))
                .build();
    }
}
