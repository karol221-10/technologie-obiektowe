package pl.kompikownia.yaml2prog.yamlparser;

import lombok.val;
import org.junit.jupiter.api.Test;
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
        assertThat(result.getFields()).contains(FieldDefinition.of("integerType", FieldType.NUMBER, null));
        assertThat(result.getFields()).contains(FieldDefinition.of("stringType", FieldType.STRING, null));
        assertThat(result.getFields()).contains(FieldDefinition.of("objectType", FieldType.OBJECT, "TestRefFile.yaml"));
    }
}
