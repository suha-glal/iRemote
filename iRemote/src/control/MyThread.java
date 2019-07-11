package control;
import javax.microedition.lcdui.*;
import javax.microedition.io.*;
import java.io.*;
class MyThread implements Runnable {
  Thread thrd;
  boolean suspended;
  boolean stopped;

  StreamConnection conn;
 private static String url = "socket://127.0.0.1:8777";
  int in1=0,in2=0,accX=0,accY=0,accZ=0;
  String num="";
Render can;
  MyThread(String name,Render c) {
    thrd = new Thread(this, name);
    suspended = false;
    stopped = false;
    can=c;
  //  getConnection();
   
  }
  public void start()
  {
    
 thrd.start();
  }
  public void run() {
    
    try {
     
         
            while(!stopped||!suspended){
            try{

    StreamConnection conn = (StreamConnection)Connector.open(url);

            OutputStream out = conn.openOutputStream();
          byte[] buf = "p".getBytes();
            out.write(buf, 0, buf.length);
            out.flush();
            out.close();

            byte[] data = new byte[256];
            InputStream in = conn.openInputStream();
            int actualLength = in.read(data);
            String response = new String(data, 0, actualLength);
            in.close();
            conn.close();

           in1=response.indexOf('!');
           num=response.substring(0, in1);
           accX= Integer.parseInt(num);
           in2=response.indexOf('@');
           num=response.substring(in1+1, in2);
           accY= Integer.parseInt(num);
           in1=response.indexOf('#');
           num=response.substring(in2+1, in1);
           accZ= Integer.parseInt(num);

          Point p=new Point(accX,accY,accZ);
          can.accXYZ.addElement(p);


        
             }//try
           catch(IOException ioe)
           {
            ioe.printStackTrace();
          Alert a=  new Alert("New Alert", "Unable to connect!\nTry again later.",null,AlertType.WARNING);

          can.iRemoteSc.display.setCurrent(a, can);
          
            }//catch

      //}
        synchronized (this) {
          while (suspended)
            wait();
          if (stopped)
            break;
        }
      }//while
    } catch (InterruptedException exc) {
      System.out.println(thrd.getName() + " interrupted.");
    }
    System.out.println("\n" + thrd.getName() + " exiting.");
    
  }//run

  synchronized void stop() {
    stopped = true;
    suspended = false;
    notify();
  }

  synchronized void suspend() {
    suspended = true;
  }

  synchronized void resume() {
    suspended = false;
    notify();
  }
  synchronized boolean  getConnection()
  {
      try{

    StreamConnection conn = (StreamConnection)Connector.open(url);

            OutputStream out = conn.openOutputStream();
          byte[] buf = can.request.getBytes();
            out.write(buf, 0, buf.length);
            out.flush();
            out.close();

            byte[] data = new byte[256];
            InputStream in = conn.openInputStream();
            int actualLength = in.read(data);
            String response = new String(data, 0, actualLength);
            in.close();
            conn.close();

         return true;
             }//try
           catch(IOException ioe)
           {
            ioe.printStackTrace();
         Alert a=  new Alert("New Alert", "Unable to connect!\nTry again later.",null,AlertType.WARNING);
          can.iRemoteSc.display.setCurrent(a, can);
          return false;
               }//catch


}
}
