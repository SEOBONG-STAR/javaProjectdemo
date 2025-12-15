package com.crypto.util.CSVUtil;

import java.io.*;
import java.util.*;

public class CSVUtil {

    // Read CSV file into list of string arrays
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

    // Write a line to CSV
    public static void writeLine(PrintWriter pw, String... values) {
        pw.println(String.join(",", values));
    }
}