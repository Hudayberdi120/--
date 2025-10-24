import java.util.ArrayList;
import java.util.List;

interface IObserver {
    void update(float temperature);
}

interface ISubject {
    void registerObserver(IObserver observer);
    void removeObserver(IObserver observer);
    void notifyObservers();
}

class WeatherStation implements ISubject {
    private List<IObserver> observers = new ArrayList<>();
    private float temperature;

    public void registerObserver(IObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(IObserver observer) {
        if (!observers.remove(observer)) {
            System.out.println("Observer not found.");
        }
    }

    public void notifyObservers() {
        for (IObserver observer : observers) {
            observer.update(temperature);
        }
    }

    public void setTemperature(float newTemperature) {
        if (Float.isNaN(newTemperature)) {
            System.out.println("Invalid temperature value.");
            return;
        }
        System.out.println("Temperature changed: " + newTemperature + "°C");
        this.temperature = newTemperature;
        notifyObservers();
    }
}

class WeatherDisplay implements IObserver {
    private String name;

    public WeatherDisplay(String name) {
        this.name = name;
    }

    public void update(float temperature) {
        System.out.println(name + " shows new temperature: " + temperature + "°C");
    }
}

class EmailNotifier implements IObserver {
    private String email;

    public EmailNotifier(String email) {
        this.email = email;
    }

    public void update(float temperature) {
        System.out.println("Email sent to " + email + ": Temperature updated to " + temperature + "°C");
    }
}

public class Main2 {
    public static void main(String[] args) {
        WeatherStation station = new WeatherStation();

        WeatherDisplay mobileApp = new WeatherDisplay("Mobile App");
        WeatherDisplay billboard = new WeatherDisplay("Digital Billboard");
        EmailNotifier emailAlert = new EmailNotifier("marka@weather.com");

        station.registerObserver(mobileApp);
        station.registerObserver(billboard);
        station.registerObserver(emailAlert);

        station.setTemperature(25.0f);
        station.setTemperature(30.0f);

        station.removeObserver(billboard);
        station.setTemperature(28.0f);

        station.removeObserver(billboard);
    }
}
