package com.crypto.app;

import java.io.PrintWriter;
import java.util.*;

import com.crypto.model.Trade;
import com.crypto.service.PortfolioService;
import com.crypto.strategy.WeightedAverageValuation;

import com.crypto.util.CSVUtil.CSVUtil;


public class App {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        PortfolioService service =
                new PortfolioService(new WeightedAverageValuation());

        System.out.println("Crypto Portfolio Tracker (Offline)");

        while (true) {

            System.out.println("\nCommands: AddTrade, ShowHoldings, Export, Exit");
            System.out.print("> ");
            String cmd = sc.nextLine();

            try {

                switch (cmd) {

                    case "AddTrade":

                        List<String[]> rows = CSVUtil.readCSV(
                                "E:\\java\\eclipse\\Crypto Portfolio Tracker\\src\\com\\crypto\\app\\Trades.csv"
                        );

                        for (int i = 1; i < rows.size(); i++) {
                            String[] r = rows.get(i);

                            Trade t = new Trade(
                                    r[0],
                                    Double.parseDouble(r[1]),
                                    Double.parseDouble(r[2])
                            );

                            service.addTrade(t);
                        }

                        System.out.println("Trades added.");
                        break;

                    case "ShowHoldings":

                        List<String[]> priceRows = CSVUtil.readCSV(
                                "E:\\java\\eclipse\\Crypto Portfolio Tracker\\src\\com\\crypto\\app\\Prices.csv"  //
                        );

                        System.out.println("\nSymbol | PnL | Status");

                        for (int i = 1; i < priceRows.size(); i++) {

                            String[] pr = priceRows.get(i);
                            String sym = pr[0];
                            double cp = Double.parseDouble(pr[1]);

                            double pnl = service.computePnL(sym, cp);

                            String status;
                            if (pnl < 0) status = "LOSS";
                            else if (pnl > 0) status = "PROFIT";
                            else status = "NO CHANGE";

                            System.out.println(
                                    sym + " | " + pnl + " | " + status
                            );
                        }
                        break;

                    case "Export":

                        List<String[]> priceData = CSVUtil.readCSV(
                                "E:\\java\\eclipse\\Crypto Portfolio Tracker\\src\\com\\crypto\\app\\Prices.csv"
                        );

                        Map<String, Double> priceMap = new HashMap<>();

                        for (int i = 1; i < priceData.size(); i++) {
                            priceMap.put(
                                    priceData.get(i)[0],
                                    Double.parseDouble(priceData.get(i)[1])
                            );
                        }

                        PrintWriter pw = new PrintWriter("pnl_report.csv");
                        CSVUtil.writeLine(pw, "Symbol", "PnL");

                        for (String sym : service.getTradeMap().keySet()) {

                            if (!priceMap.containsKey(sym)) continue;

                            double latestPrice = priceMap.get(sym);
                            double pnl = service.computePnL(sym, latestPrice);

                            CSVUtil.writeLine(pw, sym, String.valueOf(pnl));
                        }

                        pw.close();
                        System.out.println("Exported to pnl_report.csv");
                        break;

                    case "Exit":
                        System.out.println("Thank you for visiting. Have a good day!");
                        sc.close();
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
