package pl.kompikownia.yaml2prog;

import lombok.val;
import pl.kompikownia.yaml2prog.lang.FileFormat;
import pl.kompikownia.yaml2prog.lang.SupportedLang;
import pl.kompikownia.yaml2prog.processor.ConverterProcessor;

import java.util.Optional;

public class MainConverter {

    public static void main(String[] args) {
        val fileToConvert = resolveParam("--file",args);
        val destinationLang = resolveParamOrDefault("--destinationLanguage", "JAVA", args);
        val packageName = resolveParam("--packageName", args);
        val sourceFileFormat = getExtensionByStringHandling(fileToConvert).orElseThrow();
        val converterProcessor = new ConverterProcessor();
        converterProcessor.convert(fileToConvert, packageName, SupportedLang.valueOf(destinationLang.toUpperCase()), FileFormat.valueOf(sourceFileFormat.toUpperCase()));
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

    private static Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }
}
