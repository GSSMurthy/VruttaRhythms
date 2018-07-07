 import java.io.*;/*ask for the keyboard input
read it and process it to get its scan,store the scan(scanStore)
compare the scan with data in Maatraa.txt
if there is a match (flagL=1)
   output the matching scan
   output the next line, which is the name of the vRutta 
else (flagL=0)
   output "I do not know the vRutta. If you want me to learn it input the 
   name of vRutta else input NO"
   read input
   if nameInput==NO (flagL==0,flagM=1)
   do nothing
   if nameInput !=NO (flagL==0,flagM==0)
      normalize input to barahaNormal. 
      compare normalized input with data in file
        if there is a match (flagL==0,flagM==0,flagN=1)
        output "The name corresponds to another vRutta whose scan is"
        output the data appearing before the matched line.
        end
        if there is no match (flagL==0,flagM==0,flagN==0)
        write the scanStore into the file
write the nameInput into the file*/
 class BarahaString/*Baraha permits more than one way of 
representing a devanagari character by a string of one,two or three 
Roman characters. This class converts a baraha string 
to a normalized form where a devanagari alphabet is represented
by a unique string of one,two or three Roman alphabets*/ {
    String strinput=new String();
    String strOut=new String();
    String strinMaatraa=new String();
    String scanIn=new String();
    String scanOut=new String();
    String stringY=new String();
    void BarahaString( String bString,String cString,String dString, String eString, String fString ) {
        strinput=bString;
        strOut=cString;
        strinMaatraa=dString;
        scanIn=eString;
        scanOut=fString;

    }
    RandomAccessFile outfile1=null;
    void outBytes() {
        try {
            outfile1=new RandomAccessFile("outMaatraa.txt","rw");
            outfile1.seek(outfile1.length());
            outfile1.writeBytes(strOut);
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
        catch(IOException e) {
            System.out.println("IO exception");
        }
    }

    String normal()/*This method normalizes the input string.*/ {
        String subst2[]= {"##","$$","^^","__","A","I","U","$u","$U","$u","$U","au","K",
"G","c","C","J","P","B","S","Ma","MA","Mi","MI","Mu","MU","Me","Mo","MR","Ml","My","Mv",
"Ha","HA","Hi","HI","Hu","HU","He","Ho","HR","Hl","Hy","Hv","~Gg","~GG","~Gk","~GK","~Jc","~JC","~Jj","~JJ","NT","ND","nt","nd","Mp","MP","Mb","MB"
        } ;
        String asIs2[]= {"TH","tH","DH","dH","aa","ee","oo","Ri","RI","Ru","RU","ou","kh",
"gh","ch","Ch","jh","ph","bh","sh","ma","mA","mi","mI","mu","mU","me","mo","mR","ml","my","mv",
"ha","hA","hi","hI","hu","hU","he","ho","hR","hl","hy","hv","mg","mG","mk","mK","mc","mC","mj","mJ","mT","mD","mt","md","mp","mP","mb","mB"
        } ;
        String subst3[]= {"l$u","~G","~J"
        } ;
        String asIs3[]= {"lRi","Gny","jny"
        } ;
        String asIs4[]= {"tH","TH","SH","dH","DH","__","^^","##","$$"
        } ;
        String subst4[]= {"th","Th","Sh","dh","Dh","dH","DH","TH","tH"
        } ;

        strinput=strinput.replace('Y','y').replace('V','v')
            .replace('W','v').replace('w','v');/*We replace 
        Y,V,W,w by y,v,w,w*/
        strinput="`".concat(strinput).concat("```");/* This stuffing ensures  
        that loop1 and loop2 do not lead to overflow errors*/
        int n1=strinput.length();



loop3:for (int p=0;p<n1-1;p++)/*loop3 replaces asIs2 strings with 
        subst2 strings.*/ {
            for (int q=0;q<asIs2.length;q++) {
                if (strinput.substring(p,p+2).equals("``"))
                break loop3;
                if (strinput.substring(p,p+2).equals(asIs2[q])) {
                    strinput=strinput.substring(0,p).concat(subst2[q]).concat(strinput.substring(p+2,n1));


                    n1=strinput.length();

                }
            }
        }
        loop1: for (int p=0;p<n1-1;p++)
        loop2: {
            for (int q=0;q<asIs3.length;q++) {
                if (strinput.substring(p,p+2).equals("``"))
      break loop1;/*This avoids overflow, after input phrase is 
                fully processed.*/

                if (strinput.substring(p,p+3).equals(asIs3[q])) {
                    strinput=strinput.substring(0,p).concat(subst3[q]).concat(strinput.substring(p+3,n1));/* This replaces array string asIs3 by subst3 */

                    n1=strinput.length();/*n1 reduces with the processing above*/


                }
            }
        }
        loop4:for (int p=0;p<n1-1;p++)

        loop5: {
            for (int q=0;q<asIs4.length;q++) {
                if (strinput.substring(p,p+2).equals("``"))
      break loop4;/*This avoids overflow, after input phrase is 
                fully processed.*/

                if (strinput.substring(p,p+2).equals(asIs4[q])) {
                    strinput=strinput.substring(0,p).concat(subst4[q]).concat(strinput.substring(p+2,n1));/* This replaces array string asIs3 by subst3 */

                    n1=strinput.length();/*n1 reduces with the processing above*/

                }
            }
        }
        strinput=strinput.replace('R','r');
      strinput=strinput.replace('$','R');/* Note that $ is an 
      internal intermediate step in replacement, to avoid clash in 
        replacing R by r with characters like Ri,Ru etc.*/

        strinput=strinput.substring(1,n1-3);/*stuffed 1's are removed*/

        return strinput;

    }


    String inMaatraa()/*This method processes the input string for subsequent 'scanning' which evaluates maatraas.*/ {
        int pad=0;
        int n1=strinMaatraa.length();/* compute length of strinMaatraa*/
        if  (n1<3) {
            /*5.1*/strinMaatraa=strinMaatraa.concat("111");
            pad=1;
            n1=n1+3;
        } /*-5.1*/
        strinMaatraa=strinMaatraa.trim();
        int countY=0;
        for (int n=0;n<n1;n++)/* */
        if ((strinMaatraa.charAt(n)==' ')&&(strinMaatraa.charAt(n+1)==' ')) {
            strinMaatraa=strinMaatraa.substring(0,n+1).concat(strinMaatraa.substring(n+2)).concat("f");
            countY++;
            n--;/*This takes careof the string length shrinking.*/
        }
        strinMaatraa=strinMaatraa.substring(0,n1-countY);

        n1=strinMaatraa.length();
        strinMaatraa=strinMaatraa.concat("11");
        for (int n=0;n<n1;n++)/*if 'a' is the last letter of a word and 'i' or 'u' is the first letter*/
        /*of the next,this ensures that it is not mixed up with 'ai' or'au' after spaces are removed */
        if ((strinMaatraa.charAt(n)=='a')&&(strinMaatraa.charAt(n+1)==' ')&&((strinMaatraa.charAt(n+2)=='i')||(strinMaatraa.charAt(n+2)=='u'))) {
            strinMaatraa=strinMaatraa.substring(0,n+1).concat("^").concat(strinMaatraa.substring(n+2));


        }
        int n4=n1;
        int space[]=new int[n4];
        int count1=0;
        String varNa[]=new String[n1];
        String step[]=new String[n1+1];
        for (int i=0;i<n1;i++)/*creates an array(varNa[]) of characters thet form the input.*/ {
            /*6*/varNa[i]=strinMaatraa.substring(i,i+1);
            if (varNa[i].equals(" ")) {
                space[count1]=i;
                count1++;
            }
        /*if (i==n1-1)
        System.out.println("varNa["+i+"]="+varNa[i]);*/
        } /*-6*/
        n4=count1;



        StringBuffer str=new StringBuffer(0);
        for (int n=0;n<n1;n++)/* removes all spaces in the input*/
        if (strinMaatraa.charAt(n)!=' ') {
            str=str.append(varNa[n]);

        }
        strinMaatraa=str.toString();



        String svara1[]= {"a","a","R","R","g","~","~","t","d","S","w","q"
        } ;
        String svara2[]= {"i","u","u","U","h","G","J","h","h","h","h","h"
        } ;
        String svarep[]= {"E","O","Z","X","G","q","Q","T","D","x","W","Q"
        } ;

        section2:/*All 2-letter representations are replaced by single letter representation internally in a unique manner*/
        strinMaatraa=strinMaatraa.replace('T','q');/*T of baraha is replaced by q internally as T has been used for th in this program.*/
        strinMaatraa=strinMaatraa.replace('D','w');/*D of baraha is replaced by w internally as D has been used for dh in this  program,*/
        step[0]=strinMaatraa;
        int count=0;
        int n2=0;
        int n3=0;
        for (int i=0;i<svara1.length;i++)
        if (varNa[0].equals(svara1[i]) && varNa[1].equals(svara2[i])) {
            /*7*/step[1]=svarep[i].concat(step[0].substring(2));
            step[1]=step[1].concat("f");
            count=1;
       /*System.out.println("step[1]="+step[1]);*/
            break;
        } /*-7*/else {
            /*8*/step[1]=strinMaatraa;

        } /*-8*/
        n2=n1;
        for (int j=1;j<n2-1;j++) {
            /*9*/
            n2=step[j].length();
            for (int k=0;k<n2;k++) {
                /*10*/varNa[k]=step[j].substring(k,k+1);
            } /*-10*/
            for (int i=0;i<svara1.length;i++)
            if (varNa[j].equals(svara1[i]) && varNa[j+1].equals(svara2[i])) {
                /*11*/step[j+1]=step[j].substring(0,j).concat(svarep[i]).concat(step[j].substring(j+2));

                step[j+1]=step[j+1].concat("f");/*'f' compensates for shrinking of step[]*/
                count=count+1;/*count is the number of 'f's to be removed later*/
       /*System.out.println("step["+(j+1)+"]="+step[j+1]);*/
                break;
            } /*-11*/ else {
                /*12*/step[j+1]=step[j];
            } /*-12*/

            strinMaatraa=step[j+1].substring(0,n2-count);/*'f's are removed*/

            n3=strinMaatraa.length();
            if (pad==1)
            strinMaatraa=strinMaatraa.substring(0,n3-3);/*stuffed 'one's are removed*/
        }
        return strinMaatraa;/*-9*/
    }


    String scanBaraha()/*This method scans the input string which has been already processed by inMaatraa.*/ {
        int n3=0;
        n3=scanIn.length();
        String varNa[]=new String[n3];
        String svara[]= {"a","i","u","Z","A","I","U","e","o","E","O","X"
        } ;
        int letCount=0;
        int m=0;
        int maatraa[]=new int[n3+3];
        for (int z=0;z<n3+3;z++)
        maatraa[z]=0;
        section3:/*implements the algorithm for determining the maatraa of each akShara.*/
        loop1: for (m=0;m<n3;m++) {
            /*13*/varNa[m]=scanIn.substring(m,m+1);

            loop2: {
                for (int j=0;j<svara.length;j++)
                if (varNa[m].equals(svara[j])) {
                    if  (j<4) {
                        maatraa[m]=1;/*short vowels are laghu.*/
                    } else {
                        maatraa[m]=2;/*long vowels are guru.*/
                    }
                    break;
                } else {
                    maatraa[m]=0;/*vyanjanas are of zero maatraa.*/
                }
            }
        } /*-13*/
        maatraa[n3+1]=0;/*These two initializations ensure that the last letter of input line is treated as 'guru'*/
        maatraa[n3+2]=0;


        int akShara=0;
        int sum=0;
        int q=0;
        int m1=0;
        int count2[]=new int[15];
        int sumAtpause[][]=new int[9][15];
        int nonZero[]=new int[n3+2];
        for (int j=0;j<n3+1;j++) {
            if (maatraa[j]==1 && maatraa[j+1]==0 && maatraa[j+2]==0)/*laghu followed by a  samuktaakShara is a guru.*/ {
                maatraa[j]=2;
            }
            if (maatraa[j]!=0) {
                akShara++;/*counts the number of akSharas, same as number of discrete svaras.*/
                nonZero[akShara]=maatraa[j];/*nonZero stores the scan of the input line.*/
                sum=sum+maatraa[j];/* totals the number of maatraas in the input line.*/


            }
        }
        int sumAkshara[]=new int[nonZero.length];

        System.out.println("No. of akShara's ="+akShara);
        System.out.println("No. of Maatraa's ="+sum);
     /* barString2.strOut=("No. of akShara's ="+akShara+'\n'+"No. of Maatraa's ="+sum+'\n');
        barString2.outBytes();*/
        StringBuffer str1=new StringBuffer(0);
        String scanOut=new String();
        String scan[]=new  String[akShara+1];
        for (int n=1;n<akShara+1;n++)/* converts the result of scan to a string*/ {
            scan[n]=Integer.toString(nonZero[n]);
            str1=str1.append(scan[n]);

            scanOut=str1.toString();
        }
        System.out.println("scanOut="+scanOut);
        return scanOut ;
    }
}
class MaatraaFile22 {/*1*//*This program reads a string input from the keyboard and inputs into "rama.brh", if it is not a duplicate"*/
    public static void main(String args[]) {
        RandomAccessFile file=null;
        RandomAccessFile outfile1=null;

        String strB=new  String();
        String strA=new String();
        String strD=new String();
        BarahaString barString1=new BarahaString();
        BarahaString barString2=new BarahaString();
        String string1=new String();
        String stringOut=new String();
        String stringC=new String();
        String stringTemp=new String();
        String scanStore= new String();
        String nameInput=new String();
        String stringZ=new String();
        int countA=0;
        int temp=0;
        int lineNumb=0;
        int flagM=0;
        int flagL=0;
        int flagN=0; {
            try/*i/o interaction through monitor*/ {
                /*4*/System.out.println("Pl enter a paada of a vRutta");
                System.out.flush();
                DataInputStream in=new DataInputStream(System.in);
                string1=in.readLine();/*Input is string1*/
            } /*-4*/
            catch(IOException e) {
                /*5*/System.out.println("I/O/error");
                System.exit(1);
            } /*-5*/
            barString2.strinput=string1;
            string1=barString2.normal();
            barString2.strinMaatraa=string1;
            string1=barString2.inMaatraa();
      /*System.out.println("Normalized-"+string1);         
          int n1=string1.length();
            String varNa[]=new String[n1]; */
            barString2.scanIn=string1;
            stringZ=barString2.scanBaraha();
            barString2.strOut=("scan="+stringZ+'\n');
            barString2.outBytes();


            int n3=0;

            n3=string1.length();
            String varNa[]=new String[n3];
            String svara[]= {"a","i","u","Z","A","I","U","e","o","E","O","X"
            } ;
            int letCount=0;
            int m=0;
            int maatraa[]=new int[n3+3];
            for (int z=0;z<n3+3;z++)
            maatraa[z]=0;
            section3:/*implements the algorithm for determining the maatraa of each akShara.*/
            loop1: for (m=0;m<n3;m++) {
                /*13*/varNa[m]=string1.substring(m,m+1);

                loop2: {
                    for (int j=0;j<svara.length;j++)
                    if (varNa[m].equals(svara[j])) {
                        if  (j<4) {
                            maatraa[m]=1;/*short vowels are laghu.*/
                        } else {
                            maatraa[m]=2;/*long vowels are guru.*/
                        }
                        break;
                    } else {
                        maatraa[m]=0;/*vyanjanas are of zero maatraa.*/
                    }
                }
            } /*-13*/
            maatraa[n3+1]=0;/*These two initializations ensure that the last letter of input line is treated as 'guru'*/
            maatraa[n3+2]=0;


            int akShara=0;
            int sum=0;
            int q=0;
            int m1=0;
            int count2[]=new int[15];
            int sumAtpause[][]=new int[9][15];
            int nonZero[]=new int[n3+2];
            for (int j=0;j<n3+1;j++) {
                if (maatraa[j]==1 && maatraa[j+1]==0 && maatraa[j+2]==0)/*laghu followed by a  samuktaakShara is a guru.*/ {
                    maatraa[j]=2;
                }
                if (maatraa[j]!=0) {
                    akShara++;/*counts the number of akSharas, same as number of discrete svaras.*/
                    nonZero[akShara]=maatraa[j];/*nonZero stores the scan of the input line.*/
                    sum=sum+maatraa[j];/* totals the number of maatraas in the input line.*/


                }
            }
            int sumAkshara[]=new int[nonZero.length];

   /*   System.out.println("No. of akShara's ="+akShara);
      System.out.println("No. of Maatraa's ="+sum);
      barString2.strOut=("No. of akShara's ="+akShara+'\n'+"No. of Maatraa's ="+sum+'\n');
            barString2.outBytes();*/
            StringBuffer str1=new StringBuffer(0);
            String str2=new String();
            String scan[]=new  String[akShara+1];
            for (int n=1;n<akShara+1;n++)/* converts the result of scan to a string*/ {
                scan[n]=Integer.toString(nonZero[n]);
                str1=str1.append(scan[n]);

                str2=str1.toString();
            }
            barString2.strOut=("No. of akShara's ="+akShara+'\n'+"No. of Maatraa's ="+sum+'\n');
            barString2.outBytes();
            int flag1=0;
            int flag2=0;
            section4:/*checks if the input vRutta is anuShTup*/

            if (akShara==8) {
                if (nonZero[5]==1 && nonZero[6]==1 && nonZero[7]==2) {
                    flag1=1;
                    flag2=1;
                }
                if (nonZero[5]==1 && nonZero[6]==2 && nonZero[7]==1) {
                    flag1=1;
                    flag2=1;
                }
                if (flag1==0) {
                    if(((nonZero[2]==1 && nonZero[3]==2 && nonZero[4]==2 &&
                    nonZero[5]==1 && nonZero[6]==2 && nonZero[7]==2 )||
                    (nonZero[2]==2 && nonZero[3]==2 && nonZero[4]==2 &&
                    nonZero[5]==1 && nonZero[6]==2 && nonZero[7]==2 )||
                    (nonZero[2]==2 && nonZero[3]==2 && nonZero[4]==1 &&
                    nonZero[5]==1 && nonZero[6]==2 && nonZero[7]==2 )||
                    (nonZero[2]==2 && nonZero[3]==1 && nonZero[4]==2 &&
                    nonZero[5]==1 && nonZero[6]==2 && nonZero[7]==2 )||
                    (nonZero[2]==1 && nonZero[3]==2 && nonZero[4]==1 &&
                    nonZero[5]==1 && nonZero[6]==2 && nonZero[7]==2 )||
                    (nonZero[2]==2 && nonZero[3]==1 && nonZero[4]==1 &&
                    nonZero[5]==1 && nonZero[6]==2 && nonZero[7]==2 )||
                    (nonZero[2]==2 && nonZero[3]==1 && nonZero[4]==2 &&
                    nonZero[5]==2 && nonZero[6]==2 && nonZero[7]==2 )||
                    (nonZero[2]==1 && nonZero[3]==2 && nonZero[4]==2 &&
                    nonZero[5]==2 && nonZero[6]==1 && nonZero[7]==2 )||
                    (nonZero[2]==2 && nonZero[3]==2 && nonZero[4]==2 &&
                    nonZero[5]==2 && nonZero[6]==1 && nonZero[7]==2 )||
                    (nonZero[2]==2 && nonZero[3]==1 && nonZero[4]==2 &&
                    nonZero[5]==2 && nonZero[6]==1 && nonZero[7]==2 )||
                    (nonZero[2]==1 && nonZero[3]==2 && nonZero[4]==2 &&
                    nonZero[5]==2 && nonZero[6]==1 && nonZero[7]==1 )||
                    (nonZero[2]==2 && nonZero[3]==2 && nonZero[4]==2 &&
                    nonZero[5]==2 && nonZero[6]==1 && nonZero[7]==1 )||
                    (nonZero[2]==2 && nonZero[3]==1 && nonZero[4]==2 &&
                    nonZero[5]==2 && nonZero[6]==1 && nonZero[7]==1 )||
                    (nonZero[2]==1 && nonZero[3]==2 && nonZero[4]==2 &&
                    nonZero[5]==1 && nonZero[6]==1 && nonZero[7]==1 )||
                    (nonZero[2]==2 && nonZero[3]==2 && nonZero[4]==2 &&
                    nonZero[5]==1 && nonZero[6]==1 && nonZero[7]==1 )||
                    (nonZero[2]==2 && nonZero[3]==1 && nonZero[4]==2 &&
                    nonZero[5]==1 && nonZero[6]==1 && nonZero[7]==1 ))) {
                        System.out.println("The vRutta is odd paada of anuShTup"+('\n'));
                        flag1=1;
                        flag2=0;
                    } else {
                        flag2=1;
                    }
                }
            } else {
                flag2=1;
            }
            if (flag1==1 && flag2==1 ) {
                if((nonZero[5]==1 && nonZero[6]==2 && nonZero[7]==1) &&
                ((nonZero[2]==2 && nonZero[3]==2 && nonZero[4]==2 )||
                (nonZero[2]==2 && nonZero[3]==2 && nonZero[4]==1 )||
                (nonZero[2]==1 && nonZero[3]==2 && nonZero[4]==1 )||
                (nonZero[2]==2 && nonZero[3]==1 && nonZero[4]==1 )||
                (nonZero[2]==1 && nonZero[3]==2 && nonZero[4]==2 ))) {
                    System.out.println("The vRutta is even paada of anuShTup"+('\n'));
                    flag1=1;
                    flag2=0;
                }
            }
            int flag3=0;
            if (flag1==1 && flag2==0)
            flag3=1;      /*    */
            sumAkshara[0]=0;
            for (int a=0;a<akShara+1;a++) {
                sumAkshara[a+1]=nonZero[a+1]+sumAkshara[a];
                /**/
            }

            

            int flag5=0;
            int flag6=0;
            int flag7=0;
            int flag8=0;
            /*checks for vaitaalIyaM.*/
            if (sum==16 && str2.substring(akShara-5).equals("21212")) {
                if (str2.substring(0,akShara-5).equals("111111")
                || str2.substring(0,akShara-5).equals("222"));
                else
                for (int a=0;a<akShara+1;a++) {
                    if (sumAkshara[a+1]==2)
                    flag5=1;
                    if (sumAkshara[a+1]==4)
                    flag6=1;
                    if (sumAkshara[a+1]==6)
                    flag7=1;
                    if (sumAkshara[a+1]==8)
                    flag8=1;
                }
            }
            if (flag5==1 && flag6==1 && flag7==1 && flag8==1) {
                System.out.println("It is even paada of vaitaalIyaM.");
                flag3=1;
            }
            flag5=0;

            if (sum==14 && str2.substring(akShara-5).equals("21212")) {
                for (int a=0;a<akShara+1;a++) {
                    if (sumAkshara[a+1]==2)
                    flag5=1;
                    if (sumAkshara[a+1]==4)
                    flag6=1;
                    if (sumAkshara[a+1]==6)
                    flag7=1;

                }
            }
            if (flag5==1 && flag6==1 && flag7==1 ) {
                System.out.println("It is odd paada of vaitaalIyaM.");
                flag3=1;
            }
            /*checks for oupacchandasikaM.*/
            flag5=0;
            if (sum==18 && str2.substring(akShara-6).equals("212122")) {
                {
                    if (str2.substring(0,akShara-6).equals("111111")
                    || str2.substring(0,akShara-6).equals("222"));
                    else {
                        for (int a=0;a<akShara+1;a++) {
                            if (sumAkshara[a+1]==2)
                            flag5=1;
                            if (sumAkshara[a+1]==4)
                            flag6=1;
                            if (sumAkshara[a+1]==6)
                            flag7=1;
                            if (sumAkshara[a+1]==8)
                            flag8=1;
                        }
                    }
                }
            }
            if (flag5==1 && flag6==1 && flag7==1 && flag8==1) {
                System.out.println("It is even paada of oupacchandasikaM.");
                flag3=1;
            }
            flag5=0;

            if (sum==16 && str2.substring(akShara-6).equals("212122")) {
                for (int a=0;a<akShara+1;a++) {
                    if (sumAkshara[a+1]==2)
                    flag5=1;
                    if (sumAkshara[a+1]==4)
                    flag6=1;
                    if (sumAkshara[a+1]==6)
                    flag7=1;

                }
            }
            if (flag5==1 && flag6==1 && flag7==1 ) {
                System.out.println("It is odd paada of oupacchandasikaM.");
                flag3=1;
            }


            int z=0,r=0;
            if ((sum+1)/2==sum/2)/*sum is even*/ {
                r=(sum+1)/2;
            }
            else {
                r=sum/2;/*sum is odd*/
            }


            int countX[]=new int[r+1];
            int pause[][]=new int[r+1][sum];/*generates multiples of 2,3,--<sum/2 within sum.*/
            for (int d=2;d<r+1;d++) {
                for (z=1;d*z<(sum+1);z++) {
                    pause[d][z+1]=d*z;
                    if (pause[d][z+1]!=0)
                    countX[d]++;

                }
            }
/*Algorithm for checking if B is a subset of A,
          both A and B being ordered sets.
           Compare B1 with A1
           B1>A1->
             Count<lengthB-1->
              Compare B1 with A2.
             Count=lengthB-1->
              B is not a subset of A. End.
           B1=A1->
             Count+1;
             Count<lengthB-1->
               Compare B2 with A2.
             Count=lengthB-1->
             B is a subset of A.End.  
           B1<A1->
            B is not a subset of A.End.*/
            int d1=2,d2=1,d3=1,d5=0;

           int countBeats=0,numBeats=0;/*countBeats is the number of maatraa-rhythms in the input phrase."+/n/+
           "Some of the later maatraa-rhythms may be multiples of earlier rhythms.*/
            int taal[]= new int[20];/* array length 20 is arbitrary and sufficiently large*/
            int flagX[]=new int[r+1];
            for (d3=1;d3<r+1;d3++) {
                d1=2;
                d2=1;
                d5=0;
                do {
                    if (pause[d3][d1]>sumAkshara[d2]) {
                        if (d5<countX[d3]) {
                            d2++;

                        }
                        else {
                            flagX[d3]=0;

                            break;
                        }
                    } else

                    if (pause[d3][d1]==sumAkshara[d2]) {
                        d5++;
                        if (d5<countX[d3]) {
                            d2++;
                            d1++;

                        }
                        else {
                            flagX[d3]=1;
                            break;
                        }
                    } else {
                        flagX[d3]=0;


                        break;
                    }
                }
                while(d2<akShara+1);
                if (flagX[d3]==1) {
                    countBeats++;
                    taal[numBeats]=d3;
                    numBeats++;

                }
            }
        /*System.out.println("countBeats="+countBeats);*/            int countMark=0;
            int mark[]=new int[countBeats+1];

            for (int d23=0;d23<countBeats;d23++)
            mark[d23]=1;


            countMark=0;
            int flag[]=new int[countBeats-countMark];
            int d21;
            for (int d20=0;d20<countBeats;d20++) {
                for ( d21=d20+1;d21<countBeats;d21++) {
                    if (taal[d21]>taal[d20] && taal[d21]%taal[d20]==0 && flag[d21]==0) {
                        mark[d21]=0;
                        flag[d21]=1;
                        countMark++;

                    }
                }
            }

            int numBasic= countBeats-countMark;
            int taalBasic[]= new int[numBasic+1];
            for (int d12=0,d13=0;d12<countBeats && d13<numBasic+1;d12++)
            /*This loop removes taals which are multiples of earlier taals.*/ {
                if (mark[d12]!=0) {
                    taalBasic[d13]=taal[d12];
                    d13++;
                }
            }
            System.out.println("Number of basic taals="+numBasic);
            for (int d14=0;d14<numBasic;d14++) {
                System.out.println("taalBasic["+d14+"]="+taalBasic[d14]);
                barString2.strOut=("taalBasic["+d14+"]="+taalBasic[d14]+'\n');
                barString2.outBytes();

                /*  */

            }

            int subBeat[][]= new int[numBasic+1][akShara+1];
            for (int d26=0;d26<numBasic+1;d26++)/*initializes 2-D array: subBeat[][]*/ {
                for (int d27=0;d27<akShara+1;d27++) {
                    subBeat[d26][d27]=0;
                }
            }
            int modSum[][]=new int[numBasic+1][akShara+1];
            int d16=0,d17=0;
            int countMod[][]=new int[numBasic+1][40];/* array length 40 is arbitrary and suffficiently large*/
            int flagBeat[]=new int[numBasic+1];
            int valSubbeat[][]=new int[numBasic][akShara];
            int countSubbeat[]= new int[numBasic+1];
            int d26=0;

         for (d17=0;d17<numBasic;d17++) /*This double loop calculates (sumAkshara)modulo(taalBasic) 
            for each taalBasic.*/ {
                for (int d15=0;d15<akShara+1;d15++) {
                    modSum[d17][d15]=sumAkshara[d15]% taalBasic[d17];

                    for (d16=0;sumAkshara[d16]<taalBasic[d17];d16++) {
                        if (modSum[d17][d15]==sumAkshara[d16])
                        countMod[d17][d16]++;

                    }
                }
            }


            int markSubbeat[]=new int[10];/* array length 10 is arbitrary and sufficiently large*/
            for (d17=0;d17<numBasic;d17++) {
                for (d16=0;(d16<akShara+1) ;d16++) {
                    if ( modSum[d17][akShara]==0)/*if total maatraa is a multiple of taalBasic[d17]*/ {
                        if (sumAkshara[d16]<taalBasic[d17]&&(countMod[d17][d16]==countMod[d17][0]-1)) {
                            if (sumAkshara[d16]!=0) {
                                valSubbeat[d17][markSubbeat[d17]]=sumAkshara[d16];

                                markSubbeat[d17]++;
                                countSubbeat[d17]++;
                                flagBeat[d17]=1;/*flagBeat[d17] is 1, if sumAkshara[d16] is a valid beat.*/                      subBeat[d17][d16]=sumAkshara[d16];
                            }
                        }
                    } else/*if total maatraa is not a multiple of taalBasic[d17]*/ {
                        if ( sumAkshara[d16]<taalBasic[d17] && countMod[d17][d16]==countMod[d17][0]) {
                            if (sumAkshara[d16]!=0) {
                                valSubbeat[d17][markSubbeat[d17]]=sumAkshara[d16];

                                markSubbeat[d17]++;
                                countSubbeat[d17]++;
                                flagBeat[d17]=1;
                                subBeat[d17][d16]=sumAkshara[d16];
                            }
                        }
                    }
                }
                if (flagBeat[d17]==0) {
                    System.out.println("No internal beat in taalBasic["+d17+"]");
                }/*else
           System.out.println("countSubbeat["+d17+"]="+countSubbeat[d17]);*/
            }


            for (d17=0;d17<numBasic;d17++) {
                if (flagBeat[d17]!=0) {
                    valSubbeat[d17][countSubbeat[d17]]=taalBasic[d17];

                }
            }
            int d27;
            int laya[][]=new int[numBasic+1][10];

            for (d17=0;d17<numBasic;d17++) {
                for  (d27=0;d27<countSubbeat[d17]+1;d27++) {
                    if (valSubbeat[d17][d27]!=0) {
                        if (d27==0)
                        laya[d17][d27]=valSubbeat[d17][d27];
                        else
                        laya[d17][d27]=valSubbeat[d17][d27]-valSubbeat[d17][d27-1];
                    }
                /*if (laya[d17][d27]!=0)
                {System.out.println("laya["+d17+"]["+d27+"]="+laya[d17][d27]);*/

                }
            }
            int layaLength[]=new int[numBasic+1];
            StringBuffer strLaya1=new StringBuffer(0);
            String strLaya2[]=new String[numBasic+1];
            String scanLaya[][]=new String[numBasic+1][20];
            for (d17=0;d17<numBasic;d17++) {/*System.out.println("d17="+d17+",numBasic="+numBasic);*/
                for (int d28=0;d28<countSubbeat[d17]+1;d28++) {/*System.out.println("d28="+d28);*/
                    scanLaya[d17][d28]=Integer.toString(laya[d17][d28]);
                    strLaya1=strLaya1.append(scanLaya[d17][d28]).append("-");
                    strLaya2[d17]= strLaya1.toString();
                    layaLength[d17]= strLaya2[d17].length();
                    if (d17!=0)
                    strLaya2[d17]=strLaya2[d17].substring(layaLength[d17-1]);
                    strLaya2[d17]=strLaya2[d17].substring(0,(strLaya2[d17].length()-1));
                }
                if (strLaya2[d17].equals("0"));
                else {
                    System.out.println("laya in basic taal of "+taalBasic[d17]+" maatraas is "+strLaya2[d17]);
                    barString2.strOut=("laya["+ d17+"]="+strLaya2[d17]+'\n');
                    barString2.outBytes();
                }
            }






     /* barString2.strOut=("scan="+str2+'\n');
            barString2.outBytes();*/
            int numbWords=200;

            String dataPresent[]=new String[numbWords];
            int tempArray[]=new int[numbWords];
            int item=0;
            strB=null;
            if (flag3==0) {
                try/* Read the data(scans and names of vRuttas) in Maatraa.txt and store it in an array 'dataPresent'.*/ {
                    file=new RandomAccessFile("Maatraa.txt","rw");
                    for (item=0;item<=file.length()-1;item=item+strB.length()+1) {
                        file.seek(item);
                        strB=file.readLine();
                        strB=strB.trim();
                        dataPresent[countA]=strB;
 /*System.out.println("countA="+countA);*/
     /*System.out.println("strB="+strB);*/
                        countA++;
                        tempArray[countA]=item+strB.length();

                        numbWords=countA;
                    }
                }
                catch(IOException e) {
                    System.out.println(e);
                }


                try/*Compare the the scan of the input pada with the data in dataPresent array and indicate if there is a match.*/ {
                    file.seek(file.length());
                    for (int j=0;j<=countA;++j) {
                        if
                        (str2.equals(dataPresent[j])) {
                            System.out.println("I know the vRutta");
      /*System.out.println("Its scan is "+dataPresent[j]);*/      System.out.println("Its name is "+dataPresent[j+1]);
                            barString2.strOut=("name="+dataPresent[j+1]+'\n'+'\n');
                            barString2.outBytes();

      /*System.out.println("The total  number of maatraas = "+sum);
      System.out.println("No. of akSharas="+akShara);*/

                            flagL=1;
                            lineNumb=j+1;
                            break;
                        }
                    }
                }
                catch(IOException e) {
                    System.out.println(e);
                }

                if (flagL==0 )
                /* If there is no match,*/
                /*  ask for the name of the vRutta*/ {/*barString2.strOut=("name: unknown"+'\n'+'\n');
                    barString2.outBytes();*/

                    System.out.println("I do not know this vRutta."+'\n'
      /*+"Its scan is "+str2+'\n'*/+"Would you like me to learn it?"+'\n'+
"If yes,enter the name of the vRutta, else enter NO");
                    System.out.flush();
                    try {
                        DataInputStream in=new DataInputStream(System.in);
                        stringC=in.readLine();/* Input is string1*/
                    }
                    catch(IOException e) {
                        /*5*/System.out.println("I/O/error");
                        System.exit(1);
                    }
                }

                if (stringC.equals("NO")) {
                    flagM=1;
                }
                else if (flagL==0 && flagM==0) {
                    scanStore=str2;
                    String stringTemp1=new String();
                    loopA:for (int k=0;k<3;k++) {
                        stringTemp1=stringC;
                        System.out.println("Please enter YES to confirm that you"+'\n'+
"have spelt the name correctly, else enter the name again correctly");
                        try {
                            DataInputStream in=new DataInputStream(System.in);
                            stringC=in.readLine();/* Input is stringC*/
                        }
                        catch(IOException e) {
                            /*5*/System.out.println("I/O/error");
                            System.exit(1);
                        }
                        barString1.strinput=stringTemp1;
                        if (stringC.equals("YES"))
                        break loopA;
                    }

                    /*Compare the input string with the data in dataPresent array and indicate if there is a match.*/
                    for (int j=0;j<=countA;++j)
                    loopB: {
                        if (barString1.normal().equals(dataPresent[j])) {
                            System.out.println("The name refers to another vRutta,whose scan is "+dataPresent[j-1]);
                            System.out.println("Pl recheck your info.");
                            stringTemp=null;
                            flagN=1;
                            break loopB;
                        }
                    }
                }
                if ( flagM==0 && flagL==0  && flagN==0) {
                    nameInput=barString1.normal();
                    try {
                        file.seek(file.length());
                        file.writeBytes(scanStore+'\n'+nameInput+'\n');
                        barString2.strOut=("name: "+nameInput+'\n'+'\n');
                        barString2.outBytes();
                        System.out.println("I have learnt the new vRutta");
        /*System.out.println("FlagL="+flagL);*/
                    }
                    catch(IOException e) {
                        System.out.println(e);
                    }
                }
            }
        }
    }
} /*-2*/