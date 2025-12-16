package com.crypto.util.CSVUtil;

import java.io.*;
import java.util.*;

public class CSVUtil {

    public static List<String[]> readCSV(String filePath) throws IOException {
        List<String[]> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                rows.add(values);
            }
        }
        return rows;
    }

    public static void writeLine(PrintWriter pw, String... values) {
        pw.println(String.join(",", values));
    }
}