import java.io.*;
import java.util.*;

class Stock {
    String symbol;
    double price;

    Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    void updatePrice() {
        double change = (Math.random() * 4) - 2; // Random change between -2% and +2%
        price += price * (change / 100);
    }

    public String toString() {
        return symbol + ": $" + String.format("%.2f", price);
    }
}

class Market {
    Map<String, Stock> stocks = new HashMap<>();

    Market() {
        stocks.put("AAPL", new Stock("AAPL", 150.0));
        stocks.put("GOOGL", new Stock("GOOGL", 2800.0));
        stocks.put("MSFT", new Stock("MSFT", 300.0));
        stocks.put("TSLA", new Stock("TSLA", 750.0));
    }

    void updatePrices() {
        for (Stock stock : stocks.values()) {
            stock.updatePrice();
        }
    }

    void displayMarket() {
        System.out.println("\n Current Market Prices:");
        for (Stock stock : stocks.values()) {
            System.out.println(stock);
        }
    }

    Stock getStock(String symbol) {
        return stocks.get(symbol.toUpperCase());
    }
}

class PortfolioEntry {
    int quantity;
    double avgPrice;

    PortfolioEntry(int quantity, double avgPrice) {
        this.quantity = quantity;
        this.avgPrice = avgPrice;
    }
}

class User {
    String name;
    double balance;
    Map<String, PortfolioEntry> portfolio = new HashMap<>();

    User(String name) {
        this.name = name;
        this.balance = 10000.0;
    }

    void buyStock(String symbol, int quantity, Market market) {
        Stock stock = market.getStock(symbol);
        if (stock == null) {
            System.out.println("Stock not found.");
            return;
        }

        double cost = stock.price * quantity;
        if (balance >= cost) {
            balance -= cost;
            if (portfolio.containsKey(symbol)) {
                PortfolioEntry entry = portfolio.get(symbol);
                int newQty = entry.quantity + quantity;
                double newAvg = ((entry.avgPrice * entry.quantity) + (stock.price * quantity)) / newQty;
                portfolio.put(symbol, new PortfolioEntry(newQty, newAvg));
            } else {
                portfolio.put(symbol, new PortfolioEntry(quantity, stock.price));
            }
            System.out.printf(" Bought %d shares of %s at $%.2f\n", quantity, symbol, stock.price);
        } else {
            System.out.println(" Insufficient balance.");
        }
    }

    void sellStock(String symbol, int quantity, Market market) {
        if (!portfolio.containsKey(symbol)) {
            System.out.println(" You don't own this stock.");
            return;
        }

        PortfolioEntry entry = portfolio.get(symbol);
        if (entry.quantity < quantity) {
            System.out.println("Not enough shares to sell.");
            return;
        }

        Stock stock = market.getStock(symbol);
        double revenue = stock.price * quantity;
        balance += revenue;

        entry.quantity -= quantity;
        if (entry.quantity == 0) {
            portfolio.remove(symbol);
        }

        System.out.printf(" Sold %d shares of %s at $%.2f\n", quantity, symbol, stock.price);
    }

    void viewPortfolio(Market market) {
        System.out.println("\n Portfolio Overview:");
        double totalValue = 0;
        for (String symbol : portfolio.keySet()) {
            PortfolioEntry entry = portfolio.get(symbol);
            Stock stock = market.getStock(symbol);
            double currentValue = entry.quantity * stock.price;
            totalValue += currentValue;
            System.out.printf("%s: %d shares | Avg Buy: $%.2f | Current: $%.2f | Value: $%.2f\n",
symbol, entry.quantity, entry.avgPrice, stock.price, currentValue);
        }
        System.out.printf("\n Cash Balance: $%.2f\n", balance);
        System.out.printf(" Total Portfolio Value: $%.2f\n", totalValue + balance);
    }

    void savePortfolio() {
        try (PrintWriter writer = new PrintWriter(name + "_portfolio.txt")) {
            writer.println(balance);
            for (String symbol : portfolio.keySet()) {
                PortfolioEntry e = portfolio.get(symbol);
                writer.printf("%s,%d,%.2f\n", symbol, e.quantity, e.avgPrice);
            }
            System.out.println("Portfolio saved.");
        } catch (IOException e) {
            System.out.println(" Error saving portfolio.");
        }
    }

    void loadPortfolio() {
        try (Scanner sc = new Scanner(new File(name + "_portfolio.txt"))) {
            balance = Double.parseDouble(sc.nextLine());
            while (sc.hasNextLine()) {
                String[] parts = sc.nextLine().split(",");
                String symbol = parts[0];
                int quantity = Integer.parseInt(parts[1]);
                double avgPrice = Double.parseDouble(parts[2]);
                portfolio.put(symbol, new PortfolioEntry(quantity, avgPrice));
            }
            System.out.println(" Portfolio loaded.");
        } catch (IOException e) {
            System.out.println("No saved portfolio found.");
        }
    }
}

public class StockTradingPlatform {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Market market = new Market();

        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        User user = new User(name);
        user.loadPortfolio();

        while (true) {
            market.updatePrices();
            market.displayMarket();

            System.out.println("\nMenu:");
            System.out.println("1. Buy Stock");
            System.out.println("2. Sell Stock");
            System.out.println("3. View Portfolio");
            System.out.println("4. Save & Exit");
            System.out.print("Choose an option: ");

            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter stock symbol to buy: ");
                    String buySymbol = sc.nextLine().toUpperCase();
                    System.out.print("Enter quantity: ");
                    int buyQty = sc.nextInt(); sc.nextLine();
                    user.buyStock(buySymbol, buyQty, market);
                    break;
                case "2":
                    System.out.print("Enter stock symbol to sell: ");
                    String sellSymbol = sc.nextLine().toUpperCase();
                    System.out.print("Enter quantity: ");
                    int sellQty = sc.nextInt(); sc.nextLine();
                    user.sellStock(sellSymbol, sellQty, market);
                    break;
                case "3":
                    user.viewPortfolio(market);
                    break;
                case "4":
                    user.savePortfolio();
                    System.out.println("👋 Exiting... Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option.");
            }

            System.out.println("\nPress Enter to continue...");
            sc.nextLine();
        }
    }
}
