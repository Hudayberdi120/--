import java.util.*;

interface ICommand {
    void execute();
    void undo();
}

class Light {
    void on() { System.out.println("Light is ON"); }
    void off() { System.out.println("Light is OFF"); }
}

class TV {
    void on() { System.out.println("TV is ON"); }
    void off() { System.out.println("TV is OFF"); }
}

class AirConditioner {
    void on() { System.out.println("AC is ON"); }
    void off() { System.out.println("AC is OFF"); }
}

class LightOnCommand implements ICommand {
    private Light light;
    public LightOnCommand(Light light) { this.light = light; }
    public void execute() { light.on(); }
    public void undo() { light.off(); }
}

class LightOffCommand implements ICommand {
    private Light light;
    public LightOffCommand(Light light) { this.light = light; }
    public void execute() { light.off(); }
    public void undo() { light.on(); }
}

class TVOnCommand implements ICommand {
    private TV tv;
    public TVOnCommand(TV tv) { this.tv = tv; }
    public void execute() { tv.on(); }
    public void undo() { tv.off(); }
}

class TVOffCommand implements ICommand {
    private TV tv;
    public TVOffCommand(TV tv) { this.tv = tv; }
    public void execute() { tv.off(); }
    public void undo() { tv.on(); }
}

class ACOnCommand implements ICommand {
    private AirConditioner ac;
    public ACOnCommand(AirConditioner ac) { this.ac = ac; }
    public void execute() { ac.on(); }
    public void undo() { ac.off(); }
}

class ACOffCommand implements ICommand {
    private AirConditioner ac;
    public ACOffCommand(AirConditioner ac) { this.ac = ac; }
    public void execute() { ac.off(); }
    public void undo() { ac.on(); }
}

class MacroCommand implements ICommand {
    private List<ICommand> commands;
    public MacroCommand(List<ICommand> commands) { this.commands = commands; }
    public void execute() { for (ICommand cmd : commands) cmd.execute(); }
    public void undo() { for (ICommand cmd : commands) cmd.undo(); }
}

class RemoteControl {
    private Map<String, ICommand> commandSlots = new HashMap<>();
    private Stack<ICommand> undoStack = new Stack<>();
    private Stack<ICommand> redoStack = new Stack<>();

    public void setCommand(String slot, ICommand command) {
        commandSlots.put(slot, command);
    }

    public void pressButton(String slot) {
        ICommand command = commandSlots.get(slot);
        if (command == null) {
            System.out.println("No command assigned to slot: " + slot);
            return;
        }
        command.execute();
        undoStack.push(command);
        redoStack.clear();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            ICommand command = undoStack.pop();
            command.undo();
            redoStack.push(command);
        } else System.out.println("Nothing to undo");
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            ICommand command = redoStack.pop();
            command.execute();
            undoStack.push(command);
        } else System.out.println("Nothing to redo");
    }
}

abstract class ReportGenerator {
    public final void generateReport() {
        collectData();
        formatData();
        createDocument();
        if (customerWantsSave()) saveReport();
        if (customerWantsSendEmail()) sendEmail();
    }

    abstract void collectData();
    abstract void formatData();
    abstract void createDocument();
    abstract void saveReport();

    boolean customerWantsSave() { return true; }
    boolean customerWantsSendEmail() { return false; }
    void sendEmail() { System.out.println("Sending report by email..."); }
}

class PdfReport extends ReportGenerator {
    void collectData() { System.out.println("Collecting data for PDF..."); }
    void formatData() { System.out.println("Formatting PDF data..."); }
    void createDocument() { System.out.println("Creating PDF document..."); }
    void saveReport() { System.out.println("Saving PDF report..."); }
}

class ExcelReport extends ReportGenerator {
    void collectData() { System.out.println("Collecting data for Excel..."); }
    void formatData() { System.out.println("Formatting Excel cells..."); }
    void createDocument() { System.out.println("Creating Excel file..."); }
    void saveReport() { System.out.println("Saving Excel report..."); }
}

class HtmlReport extends ReportGenerator {
    void collectData() { System.out.println("Collecting data for HTML..."); }
    void formatData() { System.out.println("Formatting HTML tags..."); }
    void createDocument() { System.out.println("Creating HTML page..."); }
    void saveReport() { System.out.println("Saving HTML file..."); }
    boolean customerWantsSendEmail() { return true; }
}

interface IMediator {
    void sendMessage(String message, User user, String channelName);
    void addUser(User user, String channelName);
    void removeUser(User user, String channelName);
}

class ChannelMediator implements IMediator {
    private Map<String, List<User>> channels = new HashMap<>();

    public void sendMessage(String message, User user, String channelName) {
        if (!channels.containsKey(channelName)) {
            System.out.println("Channel " + channelName + " does not exist.");
            return;
        }
        for (User u : channels.get(channelName)) {
            if (u != user) u.receive(message, user, channelName);
        }
    }

    public void addUser(User user, String channelName) {
        channels.putIfAbsent(channelName, new ArrayList<>());
        channels.get(channelName).add(user);
        System.out.println(user.getName() + " joined channel " + channelName);
    }

    public void removeUser(User user, String channelName) {
        if (channels.containsKey(channelName)) {
            channels.get(channelName).remove(user);
            System.out.println(user.getName() + " left channel " + channelName);
        }
    }
}

abstract class User {
    protected IMediator mediator;
    protected String name;

    public User(IMediator mediator, String name) {
        this.mediator = mediator;
        this.name = name;
    }

    public String getName() { return name; }

    public abstract void send(String message, String channelName);
    public abstract void receive(String message, User sender, String channelName);
}

class ChatUser extends User {
    public ChatUser(IMediator mediator, String name) {
        super(mediator, name);
    }

    public void send(String message, String channelName) {
        System.out.println(name + " sends message in " + channelName + ": " + message);
        mediator.sendMessage(message, this, channelName);
    }

    public void receive(String message, User sender, String channelName) {
        System.out.println(name + " receives from " + sender.getName() + " in " + channelName + ": " + message);
    }
}

public class Main {
    public static void main(String[] args) {
        // -------- COMMAND ----------
        System.out.println("\n=== COMMAND PATTERN ===");
        Light light = new Light();
        TV tv = new TV();
        AirConditioner ac = new AirConditioner();

        RemoteControl remote = new RemoteControl();
        remote.setCommand("lightOn", new LightOnCommand(light));
        remote.setCommand("lightOff", new LightOffCommand(light));
        remote.setCommand("tvOn", new TVOnCommand(tv));
        remote.setCommand("tvOff", new TVOffCommand(tv));
        remote.setCommand("acOn", new ACOnCommand(ac));
        remote.setCommand("acOff", new ACOffCommand(ac));

        remote.pressButton("lightOn");
        remote.pressButton("tvOn");
        remote.undo();
        remote.redo();

        List<ICommand> party = Arrays.asList(
                new LightOnCommand(light),
                new TVOnCommand(tv),
                new ACOnCommand(ac)
        );
        remote.setCommand("partyMode", new MacroCommand(party));
        remote.pressButton("partyMode");
        remote.undo();

        System.out.println("\n=== TEMPLATE METHOD PATTERN ===");
        ReportGenerator pdf = new PdfReport();
        ReportGenerator excel = new ExcelReport();
        ReportGenerator html = new HtmlReport();
        pdf.generateReport();
        excel.generateReport();
        html.generateReport();


        System.out.println("\n=== MEDIATOR PATTERN ===");
        ChannelMediator chat = new ChannelMediator();
        User alice = new ChatUser(chat, "Alice");
        User bob = new ChatUser(chat, "Bob");
        User charlie = new ChatUser(chat, "Charlie");

        chat.addUser(alice, "general");
        chat.addUser(bob, "general");
        chat.addUser(charlie, "music");

        alice.send("Hello everyone!", "general");
        bob.send("Hi Alice!", "general");
        charlie.send("Anyone here loves music?", "music");

        chat.removeUser(bob, "general");
        alice.send("Bye Bob!", "general");
    }
}
