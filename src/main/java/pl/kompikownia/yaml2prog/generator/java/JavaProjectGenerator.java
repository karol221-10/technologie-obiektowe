package pl.kompikownia.yaml2prog.generator.java;

import lombok.val;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringSubstitutor;
import pl.kompikownia.yaml2prog.generator.ProjectGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class JavaProjectGenerator implements ProjectGenerator {

    @Override
    public String generateProjectFileContent(String templateName, Map<String, String> placeholders) throws IOException {
        val template = readFile(templateName);
        StringSubstitutor stringSubstitutor = new StringSubstitutor(placeholders);
        return stringSubstitutor.replace(template);
    }

    private String readFile(String filename) throws IOException {
        InputStream fis = getClass().getClassLoader().getResourceAsStream("templates/pom.xml");
        val result = IOUtils.toString(fis, StandardCharsets.UTF_8);
        fis.close();
        return result;
    }
}
