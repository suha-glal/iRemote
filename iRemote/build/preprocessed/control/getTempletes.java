/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Suha
 */
package control;
import java.util.*;

public class getTempletes {



private GesRecordStore  tempdata;


// private TemplateData  tempdata;
 public Vector templetes;
 public Vector Readytempletes;
private GestureProcessing  pro;
  iRemote m1;
  getTempletes(iRemote m)
{
m1=m;
    tempdata=new GesRecordStore();
    templetes=new Vector();
    Readytempletes=new Vector();
    pro=new GestureProcessing();
   
    
     

 
}//getTempletes
 Vector SendRealTemp()
 {
     m1.set=0;

    Readytempletes=tempdata.readRecords();

    m1.set=1;
    
    return  Readytempletes;
 }
 int DeleteTempletes()
 {
  CloseDB();
 return tempdata.deleteRecStore();
 }
 void CloseDB()
 {
     tempdata.closeRecStore();
 }

/*
void getRawData()
{
 templetes.addElement(new Gesture("1",tempdata.g1X,
            tempdata.g1Y,tempdata.g1Z,""));
templetes.addElement(new Gesture("2",tempdata.g2X,
            tempdata.g2Y,tempdata.g2Z,""));
templetes.addElement(new Gesture("3",tempdata.g3X,
            tempdata.g3Y,tempdata.g3Z,""));
templetes.addElement(new Gesture("4",tempdata.g4X,
            tempdata.g4Y,tempdata.g4Z,""));
templetes.addElement(new Gesture("5",tempdata.g5X,
            tempdata.g5Y,tempdata.g5Z,""));
templetes.addElement(new Gesture("6",tempdata.g6X,
            tempdata.g6Y,tempdata.g6Z,""));
templetes.addElement(new Gesture("7",tempdata.g7X,
            tempdata.g7Y,tempdata.g7Z,""));
templetes.addElement(new Gesture("8",tempdata.g8X,
            tempdata.g8Y,tempdata.g8Z,""));
    
}*/
void print(Gesture g)
{
    int i=0;
for(i=0;i<g.accX.length;i++)
{
    System.out.print(g.accX[i]);
    System.out.print(",");
}//for
    System.out.print("\n");
    for(i=0;i<g.accY.length;i++)
{
    System.out.print(g.accY[i]);
    System.out.print(",");
}//for
    System.out.print("\n");
    for(i=0;i<g.accZ.length;i++)
{
    System.out.print(g.accZ[i]);
    System.out.print(",");
}//for
    System.out.print("\n");

}//print

}
