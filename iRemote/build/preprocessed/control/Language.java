/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package control;

/**
 *
 * @author Suha
 */
public class Language {

 int mylang;
 String[] listitem;
 String exitCommandName;
 String startCommand;
 String backcommand;
 String DeletmotionCommand;
 String fillmotionCommand;
 String helpCommand;
 String MenuName;
 String[] Slidelistitem;
String slidelistname;

  Language(int l)
 {
 mylang=l;
 initalizeElements();
 }
    private void initalizeElements()
    {
       if(mylang==0)
       English();
       else
       Arabic();
    }//initalizeElements
void Arabic()
{
    //the iRemote list
 String[] arabiclistitem ={"\uFE87\uFE91\uFEAA\uFE83","\uFE83\uFEA9\uFEA7\uFEDE \uFE8D\uFEDF\uFEA4\uFEAE\uFEDB\uFE8E\uFE95","\uFE8D\uFEDF\uFEC0\uFE92\uFEC4"};
 listitem =arabiclistitem;
 //exit 
 exitCommandName="\uFEA7\uFEAE\uFEEE\uFE9D";

 //The iRemote menu name
     MenuName="Menu";//need to be change
   //Slide menu items
  String[] sAitem =
            // start                 //show
  {        "\uFE87\uFE91\uFEAA\uFE83 \uFE8D\uFEDF\uFECC\uFEAE\uFEBD"
             //slide                                    //next
           ,"\uFE8D\uFEDF\uFEB8\uFEAE\uFEF3\uFEA4\uFE94 \uFE8D\uFEDF\uFE98\uFE8E\uFEDF\uFEF4\uFE94"
            //slide                                     //Previous
           ,"\uFE8D\uFEDF\uFEB8\uFEAE\uFEF3\uFEA4\uFE94 \uFE8D\uFEDF\uFEB4\uFE8E\uFE91\uFED8\uFE94"
            //Slide                                     //First
           ,"\uFE8D\uFEDF\uFEB8\uFEAE\uFEF3\uFEA4\uFE94 \uFE8D\uFEF7\uFEED\uFEDF\uFEF0 "
            //Slide                                     //Last
           ,"\uFE8D\uFEDF\uFEB8\uFEAE\uFEF3\uFEA4\uFE94 \uFE8D\uFEF7\uFEA7\uFEF4\uFEAE\uFE93 "
           //Animation                            //Next
           ,"\uFE8D\uFEDF\uFEA3\uFEAE\uFEDB\uFE94 \uFE8D\uFEDF\uFE98\uFE8E\uFEDF\uFEF4\uFE94"
           //End                            //Show
           ,"\uFEE7\uFEEC\uFE8E\uFEF3\uFE94 \uFE8D\uFEDF\uFECC\uFEAE\uFEBD"
  };
  Slidelistitem=sAitem;

  //slide list name
  slidelistname="\uFE83\uFEED\uFE8D\uFEE3\uFEAE";

  //back command Name
  backcommand="\uFEAD\uFE9F\uFEEE\uFEC9";
}//Arabic
void English()
{
    ////the iRemote list
    String[] Englistitem = {"Start","Fill Motions","Settings"};
    listitem =Englistitem;

    
    //exit 
    exitCommandName="Exit";
    //The iRemote menu name
     MenuName="Menu";

     //Slide menu items
    String[] sitem = {"Start Show","Next Slide","Previous Slide","First Slide","Last Slide","Next Animation","End Show"};
Slidelistitem=sitem;
//slide list name
slidelistname="Commands:";
//back command name
backcommand="Back";

}//English
}//language
