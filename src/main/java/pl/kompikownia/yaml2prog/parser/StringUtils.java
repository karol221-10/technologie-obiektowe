package pl.kompikownia.yaml2prog.parser;

import lombok.val;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class StringUtils {

    public static int countIndent(String readString) {
        int indent = 0;
        val bytes = readString.getBytes(StandardCharsets.UTF_8);
        for (val readByte : bytes) {
            if (readByte == 32) {
                indent ++;
            }
            else {
                return indent;
            }
        }
        return Integer.MAX_VALUE;
    }

    public static List<String> readAllNestedFields(List<String> lines, int linePointer) {
        List<String> fileLines = new ArrayList<>();
        boolean endOfWork = false;
        while(!endOfWork) {
            val line = lines.get(linePointer++);
            val indent = StringUtils.countIndent(line);
            if(indent > 0) {
                fileLines.add(line);
            }
            if(lines.size() == linePointer || indent == 0) {
                endOfWork = true;
            }
        }
        return fileLines;
    }

}
