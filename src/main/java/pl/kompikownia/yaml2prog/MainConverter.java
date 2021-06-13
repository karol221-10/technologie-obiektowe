package pl.kompikownia.yaml2prog;

import lombok.val;
import pl.kompikownia.yaml2prog.generator.java.JavaProjectGenerator;
import pl.kompikownia.yaml2prog.lang.FileFormat;
import pl.kompikownia.yaml2prog.lang.SupportedLang;
import pl.kompikownia.yaml2prog.parameter.ParameterNames;
import pl.kompikownia.yaml2prog.processor.ConverterProcessor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MainConverter {

    public static void main(String[] args) {
        Map<String, Object> parameters = new HashMap<>();
        val fileToConvert = resolveParam("--file",args);
        val destinationLang = resolveParamOrDefault("--destinationLanguage", "JAVA", args);
        val packageName = resolveParam("--packageName", args);
        val sourceFileFormat = getExtensionByStringHandling(fileToConvert).orElseThrow();
        val converterProcessor = new ConverterProcessor();
        val projectType = resolveParamOptional("--projectType", args);
        val generateGetters = resolveParamOptional("--generateGettersSetters", args);
        if (generateGetters.isPresent()) {
            parameters.put(ParameterNames.GENERATE_GETTERS_SETTERS, true);
        }
        converterProcessor.convert(fileToConvert, packageName, SupportedLang.valueOf(destinationLang.toUpperCase()), FileFormat.valueOf(sourceFileFormat.toUpperCase()), parameters);
        if(projectType.isPresent() && "maven".equals(projectType.get())) {
            generateMavenProject(args);
        }
    }

    private static void generateMavenProject(String[] args) {
        Map<String, String> placeholderMap = new HashMap<>();
        val projectGenerator = new JavaProjectGenerator();
        placeholderMap.put("GROUP_ID", resolveParam("--projectGroupId", args));
        placeholderMap.put("ARTIFACT_ID", resolveParam("--projectArtifactId", args));
        placeholderMap.put("PROJECT_VERSION", resolveParam("--projectVersion", args));
        try {
            val content = projectGenerator.generateProjectFileContent("pom.xml", placeholderMap);
            FileOutputStream file = new FileOutputStream("target/pom.xml");
            file.write(content.getBytes(StandardCharsets.UTF_8));
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String resolveParam(String paramType, String[] args) {
        for (int i = 0;i<args.length;i++) {
            if (paramType.equals(args[i]) && i + 1 < args.length) {
                return args[i + 1];
            }
        }
        throw new IllegalArgumentException("Parameter " + paramType + " cannot be resolved");
    }

    private static String resolveParamOrDefault(String paramType, String defaultValue, String[] args) {
        try {
            return resolveParam(paramType, args);
        }
        catch(IllegalArgumentException ex) {
            return defaultValue;
        }
    }

    private static Optional<String> resolveParamOptional(String paramType, String[] args) {
        try {
            return Optional.of(resolveParam(paramType, args));
        }
        catch(IllegalArgumentException ex) {
            return Optional.empty();
        }
    }

    private static Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }
}
