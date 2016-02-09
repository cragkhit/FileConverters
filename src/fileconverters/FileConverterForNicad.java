package fileconverters;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author parvez
 */
public class FileConverterForNicad extends Fileconverters{

    /* Accpet an output file from clone deetection tool NiCad and convert it another form */
    public String convert(File file, ArrayList list){
        //String output = "<cloneDetectionResult>\r\n";
        String output ="";///
        StringBuffer sbfilecontent = new StringBuffer();//
        sbfilecontent.append("<cloneDetectionResult>\r\n");
        String str = "";
        String tokenizedStr[];
        String system="";
        String granularity="";
        String threshold="";
        String minLines="";
        String maxLines="";
        String npcs="";
        String nclones="";
        String nfragments="";
        String npairs="";
        String nclasses="";
        String ncompares="";
        String cputime="";
       // output = output+"\n<info"+ " tool=\"NiCad\"";
        try {
            LineNumberReader lr = new LineNumberReader(new FileReader(file));
            String basepath=(String)list.get(0);
            while((str = lr.readLine())!=null){
             
                if(str.trim().startsWith("<class classid="))
                {
                    tokenizedStr = str.split("\"");
                    String id=tokenizedStr[1];
                    String strnfragments=tokenizedStr[3];
                    String nlines=tokenizedStr[5];
                    //<class id="596" nlines="29" nfragments="2">
                    String strclass="<class id=\"";
                    strclass+=id;
                    strclass+="\" nlines=\"";
                    strclass+=nlines;
                    strclass+="\" nfragments=\"";
                    strclass+=strnfragments;
                    strclass+="\">\r\n";
                    //output+=strclass;
                    sbfilecontent.append(strclass);
                
                   // System.err.println(strclass);               
                   
                }
                else if (str.trim().startsWith("<source file="))
                {
                   
                    String  sourceline[]=str.split("\"");
                    String filepath=sourceline[1];
                    
                    String filesplit[]=filepath.split(basepath, 2);
                    String remainfile=filesplit[1];
                    int postfix=remainfile.indexOf(".ifdefed");
                    if(postfix!=-1)
                    {
                        remainfile=remainfile.substring(0, postfix);                        
                    }
                    //output+=sourceline[0]+"\"";
                    //output+=remainfile;
                    sbfilecontent.append(sourceline[0]);
                    sbfilecontent.append("\"");
                    sbfilecontent.append(remainfile);
                    
                    
                    int j=2;
                    while(j<sourceline.length)
                    {   //output+="\"";
                        //output+=sourceline[j];
                        sbfilecontent.append("\"");
                        sbfilecontent.append(sourceline[j]);
                        j++;
                    }
                   // output+="\r\n";
                    sbfilecontent.append("\r\n");
                  
                }
                else if (str.trim().startsWith("</class>"))
                {
                  //  output+=str+"\r\n";
                    sbfilecontent.append(str);
                    sbfilecontent.append("\r\n");
                }
             
                    

            }
        
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileConverterForNicad.class.getName()).log(Level.SEVERE, null, ex);
        }catch(Exception e){e.printStackTrace();}
        //output = output + "</cloneDetectionResult>\r\n";
        sbfilecontent.append("</cloneDetectionResult>\r\n");
        output=sbfilecontent.toString();
     //   System.out.println(output);//
        return output;
    }
}
