package com.crypto.app;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.crypto.model.Trade;
import com.crypto.service.PortfolioService;
import com.crypto.strategy.WeightedAverageValuation;
import com.crypto.util.CSVUtil.CSVUtil;

/** Command-line entry point for the offline crypto portfolio tracker. */
public class App {
    private static final Path DATA_DIR = Path.of("src", "com", "crypto", "app");
    private static final Path TRADES_FILE = DATA_DIR.resolve("Trades.csv");
    private static final Path PRICES_FILE = DATA_DIR.resolve("Prices.csv");
    private static final Path REPORT_FILE = Path.of("pnl_report.csv");

    public static void main(String[] args) {
        PortfolioService service = new PortfolioService(new WeightedAverageValuation());
        boolean tradesLoaded = false;

        System.out.println("Crypto Portfolio Tracker");
        System.out.println("Offline portfolio analysis · sample CSV data");

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\nCommands: AddTrade | ShowHoldings | Export | Exit");
                System.out.print("> ");
                if (!scanner.hasNextLine()) break;

                String command = scanner.nextLine().trim().toLowerCase();
                try {
                    switch (command) {
                        case "addtrade":
                            if (tradesLoaded) {
                                System.out.println("Trades are already loaded for this session.");
                                break;
                            }
                            List<String[]> trades = CSVUtil.readCSV(TRADES_FILE.toString());
                            int added = 0;
                            for (int i = 1; i < trades.size(); i++) {
                                String[] row = trades.get(i);
                                if (row.length < 3) {
                                    System.out.println("Skipping malformed trade row " + (i + 1));
                                    continue;
                                }
                                service.addTrade(new Trade(row[0].trim(),
                                        Double.parseDouble(row[1].trim()),
                                        Double.parseDouble(row[2].trim())));
                                added++;
                            }
                            tradesLoaded = true;
                            System.out.println("Loaded " + added + " trade(s).");
                            break;

                        case "showholdings":
                            requireTrades(tradesLoaded);
                            showHoldings(service);
                            break;

                        case "export":
                            requireTrades(tradesLoaded);
                            exportReport(service);
                            break;

                        case "exit":
                            System.out.println("Portfolio session closed.");
                            return;

                        default:
                            System.out.println("Unknown command. Choose AddTrade, ShowHoldings, Export, or Exit.");
                    }
                } catch (Exception exception) {
                    System.out.println("Could not complete command: " + exception.getMessage());
                }
            }
        }
    }

    private static void requireTrades(boolean loaded) {
        if (!loaded) throw new IllegalStateException("Run AddTrade before viewing or exporting holdings.");
    }

    private static Map<String, Double> loadPrices() throws Exception {
        List<String[]> rows = CSVUtil.readCSV(PRICES_FILE.toString());
        Map<String, Double> prices = new HashMap<>();
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row.length < 2) continue;
            double price = Double.parseDouble(row[1].trim());
            if (price < 0) throw new IllegalArgumentException("Price cannot be negative for " + row[0]);
            prices.put(row[0].trim(), price);
        }
        return prices;
    }

    private static void showHoldings(PortfolioService service) throws Exception {
        Map<String, Double> prices = loadPrices();
        System.out.printf("%-12s %16s %12s%n", "SYMBOL", "UNREALIZED P&L", "STATUS");
        System.out.println("------------------------------------------");
        for (Map.Entry<String, Double> entry : prices.entrySet()) {
            double pnl = service.computePnL(entry.getKey(), entry.getValue());
            String status = pnl > 0 ? "PROFIT" : pnl < 0 ? "LOSS" : "BREAK-EVEN";
            System.out.printf("%-12s %16.2f %12s%n", entry.getKey(), pnl, status);
        }
    }

    private static void exportReport(PortfolioService service) throws Exception {
        Map<String, Double> prices = loadPrices();
        Files.createDirectories(REPORT_FILE.toAbsolutePath().getParent());
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(REPORT_FILE))) {
            CSVUtil.writeLine(writer, "Symbol", "CurrentPrice", "UnrealizedPnL", "Status");
            for (Map.Entry<String, Double> entry : prices.entrySet()) {
                double pnl = service.computePnL(entry.getKey(), entry.getValue());
                String status = pnl > 0 ? "PROFIT" : pnl < 0 ? "LOSS" : "BREAK-EVEN";
                CSVUtil.writeLine(writer, entry.getKey(), String.valueOf(entry.getValue()),
                        String.format(java.util.Locale.US, "%.2f", pnl), status);
            }
        }
        System.out.println("Report exported to " + REPORT_FILE.toAbsolutePath());
    }
}
