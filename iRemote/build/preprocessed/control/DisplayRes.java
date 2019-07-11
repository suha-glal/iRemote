package control;

import javax.microedition.lcdui.*;


public class DisplayRes implements  CommandListener {
    // Exit command
    private Command exitCommand;

    // Back to examples list command
    private Command backCommand;
    private iRemote iRemoteScreen;
    private TextBox textBox;
    int sendExit=0;
//public String text="";
    public DisplayRes(iRemote m,String re) {
       iRemoteScreen=m;
        
        textBox = new TextBox("Result",re, 1024, TextField.ANY);
        createCommands();
       
         iRemoteScreen.display.setCurrent(textBox);
       textBox.setCommandListener(this);
    }//

   public void commandAction(Command c, Displayable d) {
       if (c == exitCommand) {
            // Exit. No need to call destroyApp
            // because it is empty.
            iRemoteScreen.notifyDestroyed();
        } else if (c == backCommand) {
            // Go back to iRemote selection list
            iRemoteScreen.display.setCurrent(iRemoteScreen.StartList);
        }
    }
    
private void createCommands() {
        exitCommand = new Command("Exit", Command.EXIT, 0);
        backCommand = new Command("Back", Command.BACK, 1);
        textBox.addCommand(exitCommand);
        textBox.addCommand(backCommand);
    }
}
