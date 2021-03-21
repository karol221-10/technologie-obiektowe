package pl.kompikownia.yaml2prog.parser;

import pl.kompikownia.yaml2prog.definition.ClassDefinition;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface Parser {
    ClassDefinition parseFile(String filename) throws IOException;
}
