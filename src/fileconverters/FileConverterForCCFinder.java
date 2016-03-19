package fileconverters;

import fileconverters.ccfinder.TestClone;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileConverterForCCFinder extends Fileconverters{
    /* the another file should point to the additional directory/file requires.
     * Otherwise set this to null
     */
    public String convert(File resultFile, ArrayList list) {
        TestClone tc = new  TestClone();
        String output="";
        try {
            output =  tc.compute(resultFile.getAbsolutePath(), list);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileConverterForCCFinder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileConverterForCCFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }

}
