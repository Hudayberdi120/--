
public class RemoteControl {
    private ICommand onCommand;
    private ICommand offCommand;

    public void setCommands(ICommand onCommand, ICommand offCommand) {
        this.onCommand = onCommand;
        this.offCommand = offCommand;
    }

    public void pressOnButton() {
        if (onCommand != null)
            onCommand.execute();
        else
            System.out.println("No on command assigned");
    }

    public void pressOffButton() {
        if (offCommand != null)
            offCommand.execute();
        else
            System.out.println("No off command assigned");
    }

    public void pressUndoButton() {
        if (onCommand != null)
            onCommand.undo();
        else
            System.out.println("No command to undo");
    }
}
