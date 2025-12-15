package com.crypto.app;

import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import com.crypto.model.Trade;
import com.crypto.service.PortfolioService;
import com.crypto.strategy.WeightedAverageValuation;
import com.crypto.util.CSVUtil.*;

public class App {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PortfolioService service = new PortfolioService(new WeightedAverageValuation());

        System.out.println("Crypto Portfolio Tracker (Offline)");
        
        while (true) {
            System.out.println("\nCommands: AddTrade, ShowHoldings, Export, Exit");
            System.out.print("> ");
            String cmd = sc.nextLine();

            try {

                switch (cmd) {

                case "AddTrade":
                    System.out.print("CSV File (trades.csv): ");
                    String f = sc.nextLine();

                    List<String[]> rows = CSVUtil.readCSV("E:\\java\\eclipse\\Crypto Portfolio Tracker\\src\\com\\crypto\\app\\Trades.csv");
                    for (int i = 1; i < rows.size(); i++) {
                        String[] r = rows.get(i);
                        Trade t = new Trade(r[0], Double.parseDouble(r[1]), Double.parseDouble(r[2]));
                        service.addTrade(t);
                    }
                    System.out.println("Trades added.");
                    break;

                case "ShowHoldings":
                    System.out.print("Prices File (prices.csv): ");
                    String pfile = sc.nextLine();

                    List<String[]> priceRows = CSVUtil.readCSV("E:\\java\\eclipse\\Crypto Portfolio Tracker\\src\\com\\crypto\\app\\Prices.csv");

                    System.out.println("\nSymbol | PnL");
                    for (int i = 1; i < priceRows.size(); i++) {
                        String[] pr = priceRows.get(i);
                        String sym = pr[0];
                        double cp = Double.parseDouble(pr[1]);


                        double pnl = service.computePnL(sym, cp);
                        System.out.println(sym + " | " + pnl);
                    }
                    break;

                case "Export":
                    PrintWriter pw = new PrintWriter("pnl_report.csv");
                    CSVUtil.writeLine(pw, "Symbol", "PnL");

                    for (String sym : service.getTradeMap().keySet()) {
                        double latestPrice = 100; // placeholder
                        double pnl = service.computePnL(sym, latestPrice);
                        CSVUtil.writeLine(pw, sym, pnl + "");
                    }

                    pw.close();
                    System.out.println("Exported to pnl_report.csv");
                    break;

                case "Exit":
                    System.out.println("Goodbye.");
                    return;

                default:
                    System.out.println("Invalid command.");
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}