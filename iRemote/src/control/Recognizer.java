package control;



public class Recognizer
{
    String result="";
   Gesture inputg;
   
    iRemote iRemoteS;
    Render Ren;
	public Recognizer(iRemote m,Gesture ge,Render r)
	{
            
             inputg= ge;
             iRemoteS=m;
             Ren=r;
    while(true)
    {
    if(iRemoteS.set==1)break;
    }//
         

//maximum int value
inputg.setScore(2147483647);

        CalculateScores();
        
        Recognize();
      /*  result+="\n******************\n";
     result+="Gesture: "+inputg.gestureName+"\n";
     result+="Distance:"+inputg.distance+"\n";
     result+="The Active Axis:"+inputg.ActiveAcc+"\n";
     result+="Data length:"+inputg.accX.length+"\n";

        DisplayRes dre=new DisplayRes(iRemoteS,result);
       * */
        Ren.Recognized(Integer.parseInt(inputg.gestureName));
	}
int DTWDistance(int[]sX,int[]sY,int[]sZ, int[]tX, int[]tY, int[]tZ) {
    int r=sX.length+1;
    int c=tX.length+1;
    
    int [][]DTW= new int [r][c];
    int i=0, j=0;
    int cost=0;

    for (i=1;i<c;i++)
        DTW[0][i]=2147483647;

    for (i=1;i<r;i++)
        DTW[i][0]=2147483647;

    DTW[0][0]= 0;

    for (i=1;i<r;i++){
       for( j=1;j<c;j++){
            cost= (int)Math.sqrt(squer(sX[i-1]- tX[j-1])+
                   squer(sY[i-1]- tY[j-1])+squer(sZ[i-1]- tZ[j-1]));

            DTW[i][j] =cost +Math.min( Math.min(DTW[i-1][j],DTW[i][j-1]),DTW[i-1][j-1]);       // match
        }//j
}//i

    return DTW[r-1][c-1];
 }

void CalculateScores()
{
   
    int sco;
   for(int j=0;j<iRemoteS.gTemplete.size();j++)
   {
         Gesture t=(Gesture) iRemoteS.gTemplete.elementAt(j);
         sco=DTWDistance(inputg.accX,inputg.accY,inputg.accZ
                 ,t.accX,t.accY,t.accZ);
 
             t.setScore(sco);
   }//j
    
}//Calculateresult
void Recognize()
{
    for(int j=0;j<iRemoteS.gTemplete.size();j++)
   {
   Gesture t=(Gesture) iRemoteS.gTemplete.elementAt(j);
  /* result+="Gesture: "+t.gestureName+"\n";
   result+="Distance:"+t.distance+"\n";
   result+="The Active Axis:"+t.ActiveAcc+"\n";
   result+="Data length:"+t.accX.length+"\n*****\n";
*/
         if(inputg.distance>t.distance)
             inputg.Clone(t);

    }//j

}// Recognize
int squer(int x){
    return (x*x);
}//squer



}
