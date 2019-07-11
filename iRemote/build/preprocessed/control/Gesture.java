/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package control;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Suha
 */
public class Gesture {
    String gestureName;
    int accX[];
    int accY[];
    int accZ[];
    int arrlen;
    int distance;
    String ActiveAcc;

Gesture()
{

}//Gesture
     Gesture(String n,int[]x,int[]y,int[]z,String c )
    {

      ActiveAcc=c;
       gestureName=n;
       accX=x;
       accY=y;
       accZ=z;
     arrlen=accX.length;
      }//Gesture
void setScore(int dis)
{
        distance=dis;

}// setScores
void Clone(Gesture a)
{
    gestureName=a.gestureName;
     distance=a.distance;
     
    
}//Clone
public void fromByteArray( byte[] data ) throws IOException {
    ByteArrayInputStream bin = new ByteArrayInputStream(data);
    DataInputStream din = new DataInputStream( bin );

   gestureName = din.readUTF();
   arrlen=din.readInt();
   accX = StringToArr(din.readUTF(), arrlen);
   accY = StringToArr(din.readUTF(), arrlen);
   accZ = StringToArr(din.readUTF(), arrlen);

    din.close();
}

public byte[] toByteArray() throws IOException {
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    DataOutputStream dout = new DataOutputStream( bout );

    dout.writeUTF(gestureName );
    dout.writeInt(arrlen);
    dout.writeUTF(arrtoStr(accX));
    dout.writeUTF(arrtoStr(accY));
    dout.writeUTF(arrtoStr(accZ));

    dout.close();

    return bout.toByteArray();
}
   int[] StringToArr(String buf,int arrlen1)
  {
      String charstr,numstr;
    int numChar=buf.length();
   int x[]=new int[arrlen1];
   char[] charArray=new char[numChar];
    for(int d=0;d<numChar;d++)
    {
        charArray[d]=buf.charAt(d);

    }


             int begin=0,end=-1;
             int s=0;
               for(s=0;s<arrlen1;s++)
               {

                  begin= end;

                 charstr=new String(charArray);
                 System.out.println(charstr);
                  end=charstr.indexOf(',');
                  charArray[end]='#';
                  numstr=buf.substring(begin+1, end);
                   x[s]=Integer.parseInt(numstr);

               }//s


             return x;
  }//StringToArray
   private String arrtoStr(int arr[])
{
    String arrstr="";
    for(int i=0;i<arr.length;i++)
    {
        arrstr+=arr[i]+",";
    }//for i
    return arrstr;
}//ArrayToString
}//end of class gesture
