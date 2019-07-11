/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package control;
import java.io.IOException;
import javax.microedition.rms.*;
import java.util.*;
/**
 *
 * @author Suha
 */
public class GesRecordStore {
  private RecordStore rs = null;
  static final String REC_STORE = "Ges_1";
  public Vector gesvec;
  //int numOfGes=0;
 GesRecordStore ()
 {

     openRecStore();
     readRecords();
    
 }
private void openRecStore()
  {
    try
    {
      // The second parameter indicates that the record store
      // should be created if it does not exist
      rs = RecordStore.openRecordStore(REC_STORE, true );
    }
    catch (Exception e)
    {
      db(e.toString());
    }
  }

  public void closeRecStore()
  {
    try
    {
      rs.closeRecordStore();
    }
    catch (Exception e)
    {
      db(e.toString());
    }
  }

  public int deleteRecStore()
  {
    if (RecordStore.listRecordStores() != null)
    {
      try
      {
        RecordStore.deleteRecordStore(REC_STORE);
        //if deleted
        return 0;
      }
      catch (Exception e)
      {
        db(e.toString());

        return 1;
      }//catch
    }//if
    //if the database is empty
    else return 2;
  }

  public void writeUpdateRecord(Gesture ge)
  {
   

    try {
    byte[] data=null;
         try {
                data = ge.toByteArray();
                //if the gesture id do not exist before
                int recordId=gesexists(ge.gestureName);
                if(recordId==-1){
                rs.addRecord( data, 0, data.length );
                }
                //if the gesture id do not exist before
                else
                {

                rs.setRecord(recordId, data, 0,data.length);

                }//
            } catch (IOException ex) {
                ex.printStackTrace();
            }

}
catch( RecordStoreException e ){
    // handle the RMS error here
}

  }//writeRecord
 String listRecords()
  {
      String availabeges="";
    RecordEnumeration  enumrec;
        try {
            enumrec = rs.enumerateRecords(null, null, false);
            while( enumrec.hasNextElement() ){
                try {
                    byte[] data = enumrec.nextRecord();
                     Gesture ge= new  Gesture();
                    try {
                        ge.fromByteArray(data);
                        availabeges+=(ge.gestureName+"\n");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } catch (RecordStoreException ex) {
                    ex.printStackTrace();
                }


            }
            enumrec.destroy(); // always clean it up!
        } catch (RecordStoreNotOpenException ex) {
            ex.printStackTrace();
        }
    return availabeges;
  }
void DeletRecord(String gname)
  {
     int recID=0;

    RecordEnumeration  enumrec;
        try {
            enumrec = rs.enumerateRecords(null, null, false);
            while( enumrec.hasNextElement() ){
                try {
                    recID = enumrec.nextRecordId();
                    byte[] data = enumrec.nextRecord();
                     Gesture ge= new  Gesture();
                    try {
                        ge.fromByteArray(data);
                      if(ge.gestureName.equals(gname));
                         rs.deleteRecord(recID);
                         break;


                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } catch (RecordStoreException ex) {
                    ex.printStackTrace();
                }


            }
            enumrec.destroy(); // always clean it up!
        } catch (RecordStoreNotOpenException ex) {
            ex.printStackTrace();
        }

  }
private int gesexists(String gname)
{

  for (int i = 0; i < gesvec.size(); i++) {
      Gesture ge=(Gesture)gesvec.elementAt(i);
      if(ge.gestureName.equals(gname))
      {
          return(i+1);
      }

            }//i

      return -1;
}//gesexists
  public Vector readRecords()
  {
       
gesvec= new Vector();

Gesture temges = new Gesture();

try{
      for (int i = 1; i <= rs.getNumRecords(); i++)
      {
          try {
    byte[] data = rs.getRecord( 1 );
    temges.fromByteArray( data );
}
catch( RecordStoreException e ){
    // handle the RMS error here
}
catch( IOException e ){
    // handle the IO error here
}
           
           gesvec.addElement(temges);
        
          
      }//i

    }
    catch (Exception e)
    {
      db(e.toString());
    }
       return gesvec;
  }

  /*--------------------------------------------------
  * Simple message to console for debug/errors
  * When used with Exceptions we should handle the
  * error in a more appropriate manner.
  *-------------------------------------------------*/
  private void db(String str)
  {
    System.err.println("Msg: " + str);
  }
 
}
