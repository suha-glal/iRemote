/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package control;

import java.io.IOException;
import javax.microedition.lcdui.*;
import java.util.*;

/**
 *
 * @author Suha
 */
public class Render extends Canvas implements  CommandListener {
 
 
 private String message="press start ";


 private Command backCommand;

 //Recognize commands
 private Command startCommand;

 //Add temp commands
 private Command DeletmotionCommand;
 private Command fillmotionCommand;
 private Command helpCommand;
Form test;
Image  startlogo;
 private MyThread mt;
 //"Start Show","Next Slide","Previous Slide","First Slide","Last Slide","Next Animation","End Show"};
 String []imgbath={"up.gif","right.gif","left.gif","next_anim.gif","5slide_right.gif","5slide_left.gif","down.gif"};

  public Vector accXYZ;
  public String request;
  public iRemote iRemoteSc;
  DeviceClientCOMM bluetooth;
  String gestempname="0";
boolean started=false;


int choose;
Render(iRemote m,int c,DeviceClientCOMM d)
{
choose=c;
bluetooth=d;
iRemoteSc=m;
 init();
}
Render(iRemote m,int c,String gn)
{
    test= new Form("TEST");
    try{
    startlogo = Image.createImage("/img/"+imgbath[Integer.parseInt(gestempname)]);
    }
    catch( IOException e ){
    }
    test.append(startlogo);
  iRemoteSc.display.setCurrent(test);
choose=c;
gestempname=gn;
iRemoteSc=m;
init();
}
void init(){
//iRemoteSc.display.setCurrent(this);
accXYZ=new Vector();
request="";

mt= new MyThread("MyThread",this);

backCommand= new Command(iRemoteSc.mylang.backcommand,Command.BACK,0);
helpCommand= new Command(iRemoteSc.mylang.helpCommand,Command.HELP,3) ;
this.addCommand(helpCommand);
this.addCommand(backCommand);


this.setCommandListener(this);
}
 public void paint(Graphics g) 
         {
      g.setColor(0xffffff);
      g.fillRect(0, 0, getWidth(), getHeight());
      g.setColor(0x000000);

      
      g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
      //g.drawString(message, 0, 30, g.LEFT | g.TOP);
   /*    try {
    startlogo = Image.createImage("/img/"+imgbath[Integer.parseInt(gestempname)]);
    }
    catch( IOException e ){
    }

    g.drawImage(startlogo, 0, 30,g.LEFT | g.TOP);
    */
         }

public void keyPressed(int keyCode)
{
    request="p";

    if(started==false)
    {
        mt.start();
        started=true;
    }
    else
        mt.resume();
    
   System.out.println("keyPressed " +((char)keyCode));
    
  
 }//keypressed

 public void keyReleased(int keyCode) {
            
             System.out.println("keyReleased " +((char)keyCode));
             mt.suspend();
             message="Finished Rendering";
             repaint ();


GestureProcessing pro=new GestureProcessing();
if(choose==1)
{
    Recognizer Reco= new Recognizer(iRemoteSc,pro.PreprocessV(accXYZ),Render.this);
}
else if(choose==2)
{
    // AddTemp addtemp = new AddTemp(iRemoteSc,pro.PreprocessV(accXYZ),gestempname);
     iRemoteSc.newtemp=1;

}
   
        
         }//keyreleased
 void RecognizeComm()
 {
 startCommand=new Command(iRemoteSc.mylang.startCommand,Command.SCREEN, 1);
  this.addCommand(startCommand);
 }
 void AddTempComm()
 {

 DeletmotionCommand= new  Command(iRemoteSc.mylang.DeletmotionCommand,Command.SCREEN,1) ;
 fillmotionCommand= new Command (iRemoteSc.mylang.fillmotionCommand,Command.SCREEN,2);

 this.addCommand(DeletmotionCommand);
 this.addCommand(fillmotionCommand);

 }//AddTempComm
 void Recognized(int ges)
 {
 bluetooth.WriteRead(ges);
 accXYZ.removeAllElements();
 message="Press 1 and keep pressing it to render the gesture";
repaint ();
 }//Recognized
 public void commandAction(Command c, Displayable s) {
if (c ==startCommand)
{

request="p";
if(mt.getConnection()==true){
message="Press 1 and keep pressing it to render the gesture";
repaint ();
}//c=startCommand
else if(c==backCommand)
{

}//c==backCommand
else if(c==DeletmotionCommand)
{
    iRemoteSc.mygesRecordStore.DeletRecord(gestempname);
}//DeletmotionCommand
else if(c==fillmotionCommand)
{
    iRemoteSc.mygesRecordStore.writeUpdateRecord(null);
}//fillmotionCommand
else if(c==helpCommand)
{
}//helpCommand

}//if

    }//commandaction


}
