
public class TelevisionOnCommand implements ICommand {
    private Television tv;
    public TelevisionOnCommand(Television tv) {
        this.tv = tv;
    }
    public void execute() {
        tv.on();
    }
    public void undo() {
        tv.off();
    }
}
