
public class Main {
    public static void main(String[] args) {
        Light light = new Light();
        Television tv = new Television();

        ICommand lightOn = new LightOnCommand(light);
        ICommand lightOff = new LightOffCommand(light);
        ICommand tvOn = new TelevisionOnCommand(tv);
        ICommand tvOff = new TelevisionOffCommand(tv);

        RemoteControl remote = new RemoteControl();

        System.out.println("=== Controlling Light ===");
        remote.setCommands(lightOn, lightOff);
        remote.pressOnButton();
        remote.pressOffButton();
        remote.pressUndoButton();

        System.out.println("\n=== Controlling Television ===");
        remote.setCommands(tvOn, tvOff);
        remote.pressOnButton();
        remote.pressOffButton();
    }
}
