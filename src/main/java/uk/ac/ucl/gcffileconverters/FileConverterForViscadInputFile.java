package uk.ac.ucl.gcffileconverters;

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
public class FileConverterForViscadInputFile extends Fileconverters{

    /* The file is in Viscad Input File. so we need to just copy the content*/
    /* We can add some integrity testing here */
    public String convert(File file, ArrayList list){
        String output = "";
        String str = "";
        try {
            LineNumberReader lr = new LineNumberReader(new FileReader(file));

            while((str = lr.readLine())!=null){
                  output = output+str+"\n";
            }
        
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileConverterForNicad.class.getName()).log(Level.SEVERE, null, ex);
        }catch(Exception e){e.printStackTrace();}
        
        return output;
    }
}
