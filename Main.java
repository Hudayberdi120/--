import java.util.*;
import java.util.concurrent.*;

interface ICostCalculationStrategy {
    double calculateCost(double distance, String serviceClass, int passengers, boolean hasDiscount, boolean hasLuggage);
}

class PlaneCostStrategy implements ICostCalculationStrategy {
    public double calculateCost(double distance, String serviceClass, int passengers, boolean hasDiscount, boolean hasLuggage) {
        double base = distance * 0.5;
        if (serviceClass.equalsIgnoreCase("business")) base *= 1.8;
        if (hasLuggage) base += 50;
        if (hasDiscount) base *= 0.9;
        return base * passengers;
    }
}

class TrainCostStrategy implements ICostCalculationStrategy {
    public double calculateCost(double distance, String serviceClass, int passengers, boolean hasDiscount, boolean hasLuggage) {
        double base = distance * 0.2;
        if (serviceClass.equalsIgnoreCase("business")) base *= 1.3;
        if (hasLuggage) base += 10;
        if (hasDiscount) base *= 0.85;
        return base * passengers;
    }
}

class BusCostStrategy implements ICostCalculationStrategy {
    public double calculateCost(double distance, String serviceClass, int passengers, boolean hasDiscount, boolean hasLuggage) {
        double base = distance * 0.1;
        if (serviceClass.equalsIgnoreCase("business")) base *= 1.2;
        if (hasLuggage) base += 5;
        if (hasDiscount) base *= 0.8;
        return base * passengers;
    }
}

class TravelBookingContext {
    private ICostCalculationStrategy strategy;

    public void setStrategy(ICostCalculationStrategy strategy) {
        this.strategy = strategy;
    }

    public double calculate(double distance, String serviceClass, int passengers, boolean hasDiscount, boolean hasLuggage) {
        if (strategy == null) throw new IllegalStateException("Strategy not selected!");
        return strategy.calculateCost(distance, serviceClass, passengers, hasDiscount, hasLuggage);
    }
}

interface IObserver {
    void update(String stockName, double newPrice);
    String getName();
}

interface ISubject {
    void addObserver(String stockName, IObserver observer);
    void removeObserver(String stockName, IObserver observer);
    void notifyObservers(String stockName, double newPrice);
}

class StockExchange implements ISubject {
    private Map<String, Double> stocks = new HashMap<>();
    private Map<String, List<IObserver>> observers = new HashMap<>();
    private ExecutorService executor = Executors.newCachedThreadPool();

    public void addStock(String name, double price) {
        stocks.put(name, price);
        observers.putIfAbsent(name, new ArrayList<>());
        System.out.println("Stock added: " + name + " - " + price);
    }

    public void updateStock(String name, double newPrice) {
        if (!stocks.containsKey(name)) {
            System.out.println("Stock not found: " + name);
            return;
        }
        stocks.put(name, newPrice);
        System.out.println("\nStock updated: " + name + " -> " + newPrice);
        notifyObservers(name, newPrice);
    }

    public void addObserver(String stockName, IObserver observer) {
        observers.putIfAbsent(stockName, new ArrayList<>());
        observers.get(stockName).add(observer);
        System.out.println(observer.getName() + " subscribed to " + stockName);
    }

    public void removeObserver(String stockName, IObserver observer) {
        if (observers.containsKey(stockName)) {
            observers.get(stockName).remove(observer);
            System.out.println(observer.getName() + " unsubscribed from " + stockName);
        }
    }

    public void notifyObservers(String stockName, double newPrice) {
        List<IObserver> list = observers.get(stockName);
        if (list == null) return;
        for (IObserver obs : list) {
            executor.submit(() -> obs.update(stockName, newPrice));
        }
    }
}

class Trader implements IObserver {
    private String name;
    public Trader(String name) { this.name = name; }
    public String getName() { return name; }

    public void update(String stockName, double newPrice) {
        System.out.println(name + " received update: " + stockName + " new price = " + newPrice);
    }
}

class TradingRobot implements IObserver {
    private String name;
    private double buyThreshold, sellThreshold;

    public TradingRobot(String name, double buyThreshold, double sellThreshold) {
        this.name = name;
        this.buyThreshold = buyThreshold;
        this.sellThreshold = sellThreshold;
    }

    public String getName() { return name; }

    public void update(String stockName, double newPrice) {
        System.out.println(name + " robot analyzing " + stockName + " price = " + newPrice);
        if (newPrice <= buyThreshold) System.out.println(name + " decides to BUY " + stockName);
        else if (newPrice >= sellThreshold) System.out.println(name + " decides to SELL " + stockName);
        else System.out.println(name + " decides to HOLD " + stockName);
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("=== STRATEGY PATTERN ===");
        TravelBookingContext context = new TravelBookingContext();

        context.setStrategy(new PlaneCostStrategy());
        double cost1 = context.calculate(1500, "business", 2, true, true);
        System.out.println("Plane cost: " + cost1);

        context.setStrategy(new TrainCostStrategy());
        double cost2 = context.calculate(800, "econom", 3, false, true);
        System.out.println("Train cost: " + cost2);

        context.setStrategy(new BusCostStrategy());
        double cost3 = context.calculate(500, "econom", 4, true, false);
        System.out.println("Bus cost: " + cost3);

        System.out.println("\n=== OBSERVER PATTERN ===");
        StockExchange exchange = new StockExchange();
        exchange.addStock("AAPL", 150);
        exchange.addStock("TSLA", 250);

        Trader alice = new Trader("Alice");
        Trader bob = new Trader("Bob");
        TradingRobot robo1 = new TradingRobot("AutoBot", 140, 260);

        exchange.addObserver("AAPL", alice);
        exchange.addObserver("AAPL", robo1);
        exchange.addObserver("TSLA", bob);
        exchange.addObserver("TSLA", robo1);

        exchange.updateStock("AAPL", 145);
        exchange.updateStock("AAPL", 135);
        exchange.updateStock("TSLA", 265);

        exchange.removeObserver("TSLA", bob);
        exchange.updateStock("TSLA", 240);

        exchange.updateStock("GOOG", 3000);

        try { Thread.sleep(1000); } catch (Exception ignored) {}
    }
}
