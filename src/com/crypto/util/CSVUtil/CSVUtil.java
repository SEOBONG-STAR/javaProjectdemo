package com.crypto.util.CSVUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class CSVUtil {
    private CSVUtil() { }

    public static List<String[]> readCSV(String filePath) throws IOException {
        List<String[]> rows = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Path.of(filePath), StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                rows.add(parseLine(line));
            }
        }
        return rows;
    }

    private static String[] parseLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder value = new StringBuilder();
        boolean quoted = false;
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch == '"') {
                if (quoted && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    value.append('"');
                    i++;
                } else {
                    quoted = !quoted;
                }
            } else if (ch == ',' && !quoted) {
                values.add(value.toString());
                value.setLength(0);
            } else {
                value.append(ch);
            }
        }
        if (quoted) throw new IllegalArgumentException("Malformed CSV row: unclosed quote.");
        values.add(value.toString());
        return values.toArray(new String[0]);
    }

    public static void writeLine(PrintWriter writer, String... values) {
        StringBuilder row = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i > 0) row.append(',');
            String value = values[i] == null ? "" : values[i];
            if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
                row.append('"').append(value.replace("\"", "\"\"")).append('"');
            } else {
                row.append(value);
            }
        }
        writer.println(row);
    }
}
