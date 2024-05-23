package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import java.util.ArrayList;
import java.util.List;

public abstract class DataGenerator {
    List<String> parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean inQuotes = false;
        char[] chars = line.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];

            if (ch == '"' && (i == 0 || chars[i - 1] != '\\')) {
                inQuotes = !inQuotes;
            } else if (ch == ',' && !inQuotes) {
                fields.add(field.toString());
                field.setLength(0);
            } else {
                field.append(ch);
            }
        }
        fields.add(field.toString());
        return fields;
    }
}
