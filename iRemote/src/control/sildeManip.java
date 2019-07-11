/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package control;
import javax.microedition.lcdui.*;
/**
 *
 * @author Suha
 */
public class sildeManip implements  CommandListener{
  private List slideList;
  private Command exitCommand;
  private Command backCommand;
  iRemote midlet;
 sildeManip(iRemote m)
 {
midlet=m;
createList();
midlet.display.setCurrent(slideList);
 }//constructor
 private void createList() {
         slideList = new List(midlet.mylang.slidelistname, List.IMPLICIT);

         //
        exitCommand=new Command(midlet.mylang.exitCommandName, Command.EXIT, 0);
        backCommand = new Command(midlet.mylang.backcommand, Command.BACK, 1);

        slideList.addCommand(exitCommand);
        slideList.addCommand(backCommand);

        for (int i = 0; i < midlet.mylang.Slidelistitem.length; i++) {
            slideList.append(midlet.mylang.Slidelistitem[i], null);
        }
         slideList.addCommand(exitCommand);
//         StartList.addCommand(deletDatabasecmd);
        slideList.setCommandListener(this);
    }//createList
public void commandAction(Command c, Displayable d) {
if (d == slideList) {
            // New example selected
            int index = slideList.getSelectedIndex();
if(index==0)
    {
    //Start Show
Render r=new Render(midlet,2,"0");
    }//if index
else if(index==1)
    {
    //"Next Slide"
Render r=new Render(midlet,2,"1");
    }//if index
else if(index==2)
    {
    //"Previous Slide"
    Render r=new Render(midlet,2,"2");
    }//if index
 else if(index==3)
    {
     //"First Slide"
Render r=new Render(midlet,2,"3");
    }//if index
else if(index==4)
    {
    //"Last Slide"
Render r=new Render(midlet,2,"4");
    }//if index
 else if(index==5)
    {
     //"Next Animation"
Render r=new Render(midlet,2,"5");
    }//if index
 else if(index==6)
    {
     //"End Show"
Render r=new Render(midlet,2,"6");
    }//if index

}//if d==slideList

}//commandAction
}//end of the class
