package pl.kompikownia.yaml2prog.yamlparser;

import lombok.val;
import org.junit.jupiter.api.Test;
import pl.kompikownia.yaml2prog.parser.YamlParser;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class YamlParserTest {

    @Test
    public void shouldProperlyGetClassName() throws IOException {
        // given
        val yamlParser = new YamlParser();

        // when
        val result = yamlParser.parseFile("src/test/resources/TestFile.yaml");

        // then
        assertThat(result.getName()).isEqualTo("TestClass");
    }
}
