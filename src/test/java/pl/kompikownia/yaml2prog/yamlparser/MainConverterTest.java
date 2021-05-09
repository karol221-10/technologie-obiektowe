package pl.kompikownia.yaml2prog.yamlparser;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.kompikownia.yaml2prog.MainConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLOutput;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MainConverterTest {

    @BeforeEach
    private void prepareEnvironment() throws IOException {
        if (Files.exists(Path.of("target\\pl"))) {
            FileUtils.cleanDirectory(new File("target\\pl"));
            FileUtils.deleteDirectory(new File("target\\pl"));
        }
    }

    @Test
    public void shouldCorrectlyConvertAndCreateFile() throws IOException {
        //given
        String[] args = List.of("--file","src/test/resources/Person.yaml", "--packageName","pl.kompikownia.yaml2prog.test.dtos")
                .toArray(String[]::new);
        MainConverter.main(args);
        File tmpDir = new File("target/pl/kompikownia/yaml2prog/test/dtos/Person.java");
        assertThat(tmpDir.exists()).isEqualTo(true);
    }

    @Test
    public void shouldCorrectlyConvertAndCreateFilesWithInheritance() throws IOException {
        String[] args = List.of("--file","src/test/resources/Student.yaml", "--packageName","pl.kompikownia.yaml2prog.test.dtos")
                .toArray(String[]::new);
        MainConverter.main(args);
        File studentFile = new File("target/src/main/java/pl/kompikownia/yaml2prog/test/dtos/Student.java");
        File personFile = new File("target/src/main/java/pl/kompikownia/yaml2prog/test/dtos/Person.java");
        assertThat(personFile.exists()).isEqualTo(true);
        assertThat(studentFile.exists()).isEqualTo(true);
    }

    @Test
    public void shouldCorrectlyConvertAndCreateFileWithProject() throws IOException {
        //given
        String[] args = List.of("--file","src/test/resources/Person.yaml", "--packageName","pl.kompikownia.yaml2prog.test.dtos",
                "--projectType","maven","--projectGroupId","pl.kompikownia.yaml2prog","--projectArtifactId","yaml2progdtos",
                "--projectVersion","1.0.0-SNAPSHOT")
                .toArray(String[]::new);
        MainConverter.main(args);
        File mavenFile = new File("target/pom.xml");
        assertThat(mavenFile.exists()).isEqualTo(true);
    }
}
