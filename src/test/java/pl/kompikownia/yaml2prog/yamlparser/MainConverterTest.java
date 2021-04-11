package pl.kompikownia.yaml2prog.yamlparser;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import pl.kompikownia.yaml2prog.MainConverter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MainConverterTest {

    @Test
    public void shouldCorrectlyConvertAndCreateFile() throws IOException {
        //given
        FileUtils.cleanDirectory(new File("target\\pl"));
        FileUtils.deleteDirectory(new File("target\\pl"));
        String[] args = List.of("--file","src/test/resources/Person.yaml", "--packageName","pl.kompikownia.yaml2prog.test.dtos")
                .toArray(String[]::new);
        MainConverter.main(args);
        File tmpDir = new File("target/pl/kompikownia/yaml2prog/test/dtos/Person.java");
        assertThat(tmpDir.exists()).isEqualTo(true);
    }
}
