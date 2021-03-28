package pl.kompikownia.yaml2prog.parser;

import lombok.val;

import java.nio.charset.StandardCharsets;

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
}
