/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package control;
import java.util.*;
/*
 *@author Suha
 */
public class GestureProcessing {
    private Vector xyzAcc;
    private Gesture g;
    private Gesture rawg;
    private Recognizer re;
    private int smpX[];
    private int smpY[];
    private int smpZ[];
    String gname="";
   
 GestureProcessing( )
{

}//GestureProcessing

Gesture PreprocessV(Vector v)
{
    xyzAcc=v;
    smpX=new int [xyzAcc.size()];
    smpY=new int [xyzAcc.size()];
    smpZ=new int [xyzAcc.size()];


          for (int i=0;i<xyzAcc.size();i++)
          {
              Point p;
             p=(Point)xyzAcc.elementAt(i);
             smpX[i]=p.X;
             smpY[i]=p.Y;
             smpZ[i]=p.Z;
           }//i
    return processGesture();
}
Gesture PreprocessG(Gesture rg)
{
    rawg=rg;
    gname=rawg.gestureName;
    smpX=rawg.accX;
    smpY=rawg.accY;
    smpZ=rawg.accZ;


    return processGesture();
}
Gesture processGesture(){
//gesture creation
//ActaccIs(smpX,smpY,smpZ);
 g=(new Gesture(gname,smpX,smpY,smpZ,""));

/*//low pass filter;
 g.accX=lowpassFilter(g.accX,0.028,1);
 g.accY=lowpassFilter( g.accY,0.028,1);
 g.accZ=lowpassFilter(g.accZ,0.028,1);
 */

int w=2;
int s=1;
g.accX=avgWindowFilter(g.accX,w,s);
g.accY=avgWindowFilter(g.accY,w,s);
g.accZ=avgWindowFilter(g.accZ,w,s);

g.accX=quantization(g.accX);
g.accY=quantization(g.accY);
g.accZ=quantization(g.accZ);

/*
 //slop
 g.accX=slop( g.accX);
 g.accY=slop( g.accY);
 g.accZ=slop( g.accZ);
*/


return g;
}//
void  ActaccIs(int[]accx,int[]accy,int[]accz)
{
   int acc1avg=Average(accx);
   int acc2avg=Average(accy);
   int acc3avg=Average(accz);

     // +x
     if(acc1avg>200)
         g=(new Gesture(gname,inverse(accz),inverse(accy),inverse(accx),"+x"));
    // +y
     else if(acc2avg>200)
          g= (new Gesture(gname,accz,accx,inverse(accy),"+y"));
    // +z
     else if(acc3avg>200)
         g= (new Gesture(gname,accx,inverse(accy),inverse(accz),"+z"));
   // -x
     else if(acc1avg<-200)
         g=(new Gesture(gname,inverse(accz),accy,accx,"-x"));
   // -y
     else if(acc2avg<-200)
         g= (new Gesture(gname,accx,inverse(accz),accy,"-y"));
  // -z
     else if(acc3avg<-200)
          g= (new Gesture(gname,accx,accy,accz,"-z"));
    // else
    //     g= (new Gesture(gname,accx,accy,accz,"-z"));


}//ActaccIs
int Average(int[] arr)
{
    int avg, i,sum=0;
    for(i=0;i<arr.length;i++)
        sum+=arr[i];
   
    avg=(sum/arr.length);
   //System.out.print(avg+"\n");
    return avg;
}//Average
int[]inverse(int[]arr)
{
    int i=0;
    for(i=0;i<arr.length;i++)
    if(arr[i]>0)
        arr[i]*=-1;

    return arr;

}//
//Fc cut of frequency
//dt time interval
int[] lowpassFilter(int[]x,double dt,double Fc)
{

double Inertia;
double RC;
RC=(double)(1/(Fc*2*3.1416));
	int i=0;
   Inertia=(double)dt/(RC+dt);

   int[] y=new int[x.length];
   y[0] = x[0];
   for (i=1;i< x.length;i++)
   {

       y[i] =(int)(Inertia * x[i]) + (int)((1-Inertia) * y[i-1] );
   }
 return y;
}//lowpassFilter
int[] avgWindowFilter(int[]x,int win,int step)
    {

                int xSize=x.length;
		int ySize=((xSize-win)/step)+1;
		int y[]=new int[ySize];
		int i,j,z=0;
		int avg=0,sum=0;

		for(i=0;i<xSize-win;i+=step)
		{
			for(j=i;j<i+win;j++)
			{
				sum+=x[j];
			}//j
			avg=sum/win;
            y[z]=avg;
			sum=0;
            z++;
		}//i

   return y;
    }//avgWindowFilter
int[] slop(int[]x)
{
	 int i;
   int[] y=new int[x.length];

   for (i=1;i< x.length-1;i++)
   {

       y[i] =x[i]-x[i-1];
   }
 return y;
}//slop
int[] quantization(int []x)
{ double t;
    int y[]=new int [x.length];
    double g[]= new double [x.length];

    for(int i=0;i<x.length;i++)
    {
        t=((double)x[i]/64)*10;
        t=round(t);
        t/=10;
        g[i]=t;
      y[i]=convertedValue(g[i]);
       
    }
//System.out.print("\n");
return y;
}//quantization
double round(double x) {

  return ((x - Math.floor(x)) >= 0.5) ? Math.ceil(x) : Math.floor(x);
}//round
int convertedValue(double a)
{
        int y=0;
  if(a>1)
      y=8;
  else if (a==0.9||a==1)
      y=7;
  else if (a==0.7||a==0.8)
      y=6;
  else if (a==0.5||a==0.6)
      y=5;
 else if (a==0.4)
   y=4;
else if (a==0.3)
   y=3;
else if (a==0.2)
   y=2;
else if (a==0.1)
   y=1;
else if (a==0)
   y=4;
else if (a==-0.1)
   y=-1;
else if (a==-0.2)
   y=-2;
 else if (a==-0.3)
   y=-3;
else if (a==-0.4)
   y=-4;
else if (a==-0.5)
   y=-5;
 else if (a==-0.6)
   y=-6;
else if (a==-0.7||a==-0.8)
      y=-7;
 else if (a==-0.9||a==-1)
      y=-8;
else if (a==-1.1||a==-1.2)
      y=-9;
 else if (a==-1.3||a==-1.4)
      y=-10;
 else if (a>=-3 && a<-1.4)
      y=-11;
 else if (a==-3.1||a==-3.2)
      y=-12;
 else if (a==-3.3||a==-3.4)
      y=-13;
else if (a==-3.5||a==-3.6)
      y=-14;
 else if (a==-3.7||a==-3.8)
      y=-15;
 else if (a==-3.9)
   y=-16;
else if (a==-4.0)
   y=-17;
 else if (a==-4.1)
   y=-18;
else if (a==-4.2)
   y=-19;
else if (a==-4.3)
   y=-20;
 else if (a==-4.4)
   y=-21;
else if (a==-4.5||a==-4.6)
      y=-22;
else if (a==-4.7||a==-4.8)
      y=-23;
 else if (a==-4.9||a==-5)
      y=-24;
 else if(a>-5)
     y=-25;

return y;
}
}//GestureProcessing
