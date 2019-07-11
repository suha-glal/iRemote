/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.util.*;

public class iRemote extends MIDlet implements  CommandListener {
   // Exit command
    private Command exitCommand;
   // private Command deletDatabasecmd;
    private getTempletes gt;
    Language mylang;
    Vector gTemplete;
    GesRecordStore mygesRecordStore;
    public Display display;
    int set;
    int newtemp;
   // The example selection list
    public List StartList;
   // The example names. Used to populate the list.

//adkel alhrakat
    public iRemote() {
        display = Display.getDisplay(this);
         mylang= new Language(1);
        mygesRecordStore= new GesRecordStore();

      }//iRemote

    public void startApp() {
exitCommand = new Command(mylang.exitCommandName, Command.EXIT, 0);
//deletDatabasecmd=new Command("DeletDB",Command.SCREEN,0);

            // Create the list of examples
            createList();

            // Start with the List
            display.setCurrent(StartList);
    }//startApp

    public void commandAction(Command c, Displayable d) {
     //  if(c==deletDatabasecmd)
        if (c == exitCommand) {
            // Exit. No need to call destroyApp
            // because it is empty.
            notifyDestroyed();

        } else if (d == StartList) {
            // New example selected
            int index = StartList.getSelectedIndex();
if(index==0)
    {

   if(newtemp==1){

         gt=new getTempletes(this);
         gTemplete=new Vector();
         gTemplete=gt.SendRealTemp();

   }//newtemp
    DeviceClientCOMM blue= new DeviceClientCOMM(iRemote.this);
    newtemp=0;



      }//if
     else if(index==1)
    {

    new sildeManip(iRemote.this);


    }//else if
     else if(index==2)
     {
          int con= gt.DeleteTempletes();
          if(con==0)
          {
         Alert a=  new Alert("Delete", "Record Store was deleted sucessfuly",null,AlertType.CONFIRMATION);
         a.setTimeout( Alert.FOREVER );
        this.display.setCurrent(a,this.StartList);
          }//con==1
          else if(con==1)
          {
         Alert a=  new Alert("Delete", "Record Store is empty",null,AlertType.CONFIRMATION);
         a.setTimeout( Alert.FOREVER );
        this.display.setCurrent(a,this.StartList);
          }
          else
          {
               Alert a=  new Alert("Delete", "Record Store is empty",null,AlertType.CONFIRMATION);
         a.setTimeout( Alert.FOREVER );
        this.display.setCurrent(a,this.StartList);
          }
     }
    }//d=StartList
    }//commandAction
    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        gt.CloseDB();
    }

    public void showAlert(String message){

    }

 private void createList() {
        StartList = new List(mylang.MenuName, List.IMPLICIT);

        for (int i = 0; i < mylang.listitem.length; i++) {

            StartList.append(mylang.listitem[i], null);

        }
         StartList.addCommand(exitCommand);
//         StartList.addCommand(deletDatabasecmd);
        StartList.setCommandListener(this);
    }

}
