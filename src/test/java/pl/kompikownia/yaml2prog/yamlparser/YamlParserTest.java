package pl.kompikownia.yaml2prog.yamlparser;

import lombok.val;
import org.junit.jupiter.api.Test;
import pl.kompikownia.yaml2prog.definition.ClassDefinition;
import pl.kompikownia.yaml2prog.definition.FieldDefinition;
import pl.kompikownia.yaml2prog.definition.FieldType;
import pl.kompikownia.yaml2prog.parser.YamlParser;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


public class YamlParserTest {

    @Test
    public void shouldProperlyParseFile() throws IOException {
        // given
        val yamlParser = new YamlParser();

        // when
        val result = yamlParser.parseFile("src/test/resources/TestFile.yaml");

        // then
        assertThat(result.getName()).isEqualTo("TestFile");
        assertThat(result.getFields().size()).isEqualTo(3);
        assertThat(result.getFields()).contains(FieldDefinition.of("integerType", FieldType.NUMBER, false,null));
        assertThat(result.getFields()).contains(FieldDefinition.of("stringType", FieldType.STRING, false,null));
        assertThat(result.getFields()).contains(FieldDefinition.of("objectType", FieldType.OBJECT, false,
                ClassDefinition.builder()
                .name("TestRefFile")
                .path("src/test/resources/")
                .field(FieldDefinition.of("field1", FieldType.STRING, false, null))
                .field(FieldDefinition.of("field2", FieldType.NUMBER, false,null))
                .build()));
    }

    @Test
    public void shouldProperlyParseFileWithArray() throws IOException {
        // given
        val yamlParser = new YamlParser();

        // when
        val result = yamlParser.parseFile("src/test/resources/Magazine.yaml");

        // then
        assertThat(result.getFields().get(0).isArray()).isEqualTo(true);
        assertThat(result.getFields().get(0).getType()).isEqualTo(FieldType.NUMBER);
        assertThat(result.getFields().get(1).isArray()).isEqualTo(true);
        assertThat(result.getFields().get(1).getType()).isEqualTo(FieldType.OBJECT);
        assertThat(result.getFields().get(1).getRefClass()).isNotNull();
    }

    @Test
    public void shouldProperlyParseInheritanceFile() throws IOException {
        // given
        val yamlParser = new YamlParser();

        // when
        val result = yamlParser.parseFile("src/test/resources/TestInheritanceFile.yaml");
        // then

        assertThat(result.getParentClass()).isNotNull();
    }
}
