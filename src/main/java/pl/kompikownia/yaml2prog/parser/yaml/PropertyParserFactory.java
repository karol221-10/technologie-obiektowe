package pl.kompikownia.yaml2prog.parser.yaml;

import pl.kompikownia.yaml2prog.exception.FileParseException;

public class PropertyParserFactory {

    public PropertyParser getPropertyParserFactory(String propertyName) {
        switch (propertyName) {
            case "name":
                return new NameYamlParser();
            case "properties":
                return new PropertiesYamlParser();
            default:
                throw new FileParseException();
        }
    }
}
