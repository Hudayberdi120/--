
public class TelevisionOffCommand implements ICommand {
    private Television tv;
    public TelevisionOffCommand(Television tv) {
        this.tv = tv;
    }
    public void execute() {
        tv.off();
    }
    public void undo() {
        tv.on();
    }
}
