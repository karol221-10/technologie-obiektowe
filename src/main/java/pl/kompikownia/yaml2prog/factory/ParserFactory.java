package pl.kompikownia.yaml2prog.factory;

import pl.kompikownia.yaml2prog.lang.FileFormat;
import pl.kompikownia.yaml2prog.parser.Parser;
import pl.kompikownia.yaml2prog.parser.YamlParser;

public class ParserFactory {
    public static Parser getParser(FileFormat fileFormat) {
        if(FileFormat.YAML.equals(fileFormat)) {
            return new YamlParser();
        }
        throw new IllegalArgumentException("Unsupported format: "+ fileFormat.name());
    }
}
