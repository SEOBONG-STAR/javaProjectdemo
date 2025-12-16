package com.crypto.util.CSVUtil;
import java.io.FileWriter;
import java.io.IOException;

		// TODO Auto-generated method stub
		public class CSVUpdateService {

		    private static final String FILE_PATH = "trades.csv";

		    // ADD new trade to CSV
		    public static void addTrade(String symbol, double quantity, double price) {

		        try (FileWriter fw = new FileWriter(FILE_PATH, true)) {
		            fw.append(symbol)
		              .append(",")
		              .append(String.valueOf(quantity))
		              .append(",")
		              .append(String.valueOf(price))
		              .append("\n");

		            System.out.println("✅ Trade added to CSV");

		        } catch (IOException e) {
		            System.out.println("❌ Error updating CSV");
		            e.printStackTrace();
		        }
		    }
		


	}


