package pl.kompikownia.yaml2prog.yamlparser;

import lombok.val;
import org.junit.jupiter.api.Test;
import pl.kompikownia.yaml2prog.definition.ClassDefinition;
import pl.kompikownia.yaml2prog.definition.FieldDefinition;
import pl.kompikownia.yaml2prog.definition.FieldType;
import pl.kompikownia.yaml2prog.generator.java.JavaCodeGenerator;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaCodeGeneratorTest {

    @Test
    public void shouldGenerateCorrectJavaFile() throws IOException {
        // given
        val codeGenerator = new JavaCodeGenerator();
        codeGenerator.setMainPackageName("pl.kompikownia.yaml2prog.dto");
        // when
        val result = codeGenerator.generateFile(buildPersonClassDefinition());

        // then
       assertThat(result).isEqualTo(readFile("Person.java"));
    }

    @Test
    public void shouldGenerateCorrectJavaFileWithInheritance() throws IOException {
        // given
        val codeGenerator = new JavaCodeGenerator();
        codeGenerator.setMainPackageName("pl.kompikownia.yaml2prog.dto");
        // when
        val result = codeGenerator.generateFile(buildStudentClassDefinition());

        // then
        assertThat(result).isEqualTo(readFile("Student.java"));
    }

    private String readFile(String filename) throws IOException {
        File file = new File("src/test/resources/"+filename);
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        return new String(data, StandardCharsets.UTF_8);
    }

    private ClassDefinition buildPersonClassDefinition() {
        return ClassDefinition.builder()
                .name("Person")
                .field(FieldDefinition.of("name", FieldType.STRING, null))
                .field(FieldDefinition.of("surname", FieldType.STRING, null))
                .field(FieldDefinition.of("age", FieldType.NUMBER, null))
                .build();
    }

    private ClassDefinition buildStudentClassDefinition() {
        return ClassDefinition.builder()
                .name("Student")
                .parentClass(buildPersonClassDefinition())
                .field(FieldDefinition.of("studentIdNumber", FieldType.STRING, null))
                .field(FieldDefinition.of("yearOfStudy", FieldType.NUMBER, null))
                .build();
    }
}
