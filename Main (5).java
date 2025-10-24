import java.util.Scanner;

interface IShippingStrategy {
    double calculateShippingCost(double weight, double distance);
}

class StandardShippingStrategy implements IShippingStrategy {
    public double calculateShippingCost(double weight, double distance) {
        return weight * 0.5 + distance * 0.1;
    }
}

class ExpressShippingStrategy implements IShippingStrategy {
    public double calculateShippingCost(double weight, double distance) {
        return (weight * 0.75 + distance * 0.2) + 10;
    }
}

class InternationalShippingStrategy implements IShippingStrategy {
    public double calculateShippingCost(double weight, double distance) {
        return weight * 1.0 + distance * 0.5 + 15;
    }
}

class NightShippingStrategy implements IShippingStrategy {
    public double calculateShippingCost(double weight, double distance) {
        return (weight * 0.8 + distance * 0.25) + 20;
    }
}

class DeliveryContext {
    private IShippingStrategy shippingStrategy;

    public void setShippingStrategy(IShippingStrategy strategy) {
        this.shippingStrategy = strategy;
    }

    public double calculateCost(double weight, double distance) {
        if (shippingStrategy == null) {
            throw new IllegalStateException("No shipping strategy selected.");
        }
        if (weight < 0 || distance < 0) {
            throw new IllegalArgumentException("Weight and distance must be non-negative.");
        }
        return shippingStrategy.calculateShippingCost(weight, distance);
    }
}

public class Main{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DeliveryContext deliveryContext = new DeliveryContext();

        System.out.println("Choose delivery type: 1 - Standard, 2 - Express, 3 - International, 4 - Night");
        String choice = sc.nextLine();

        switch (choice) {
            case "1":
                deliveryContext.setShippingStrategy(new StandardShippingStrategy());
                break;
            case "2":
                deliveryContext.setShippingStrategy(new ExpressShippingStrategy());
                break;
            case "3":
                deliveryContext.setShippingStrategy(new InternationalShippingStrategy());
                break;
            case "4":
                deliveryContext.setShippingStrategy(new NightShippingStrategy());
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        System.out.print("Enter package weight (kg): ");
        double weight = sc.nextDouble();

        System.out.print("Enter delivery distance (km): ");
        double distance = sc.nextDouble();

        try {
            double cost = deliveryContext.calculateCost(weight, distance);
            System.out.printf("Shipping cost: %.2f\n", cost);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        sc.close();
    }
}
