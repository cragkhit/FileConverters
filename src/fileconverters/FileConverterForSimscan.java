/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fileconverters;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author muhammadult The converter takes as input the result folder created by
 * Simscan Adding new directory in the result folder may produce wrong result
 */
public class FileConverterForSimscan extends Fileconverters {

    public String convert(File file, ArrayList list) {

        HashMap hm = new HashMap();
        String output = "";         // final output that will be returned
        String detectionInfo = "";  // information about clone detection
        String cloneClassList = "";// clone classes along with the clone fragments
        StringBuffer sbcloneClassList = new StringBuffer();//
        sbcloneClassList.append("<cloneDetectionResult>");
        sbcloneClassList.append("\n");

        if (file.isDirectory() == true) {// The input file is a directory
            readSummary(file, hm); // read the summary file that map pcid to file name

            //read detection parameter and update the detectionInfo
            //  detectionInfo = "<info "+ "tool=\""+"simscan\" "+readDetectionInfo(file)+"/>";
            //System.out.println( detectionInfo);
            // Now process each folder that represent a clone class
            File fileList[] = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory() == false) {
                    continue;//just ignore, adirectory under the result directory indicate clone class
                }
                File folder = fileList[i];
                String folderName = folder.getName();

                //System.out.println( folderName+"foldname");

                /*
                 * we need to parse the name. This contains the information. A
                 * folder represent a clone class
                 */
                String classInfo = "";
                //String classInfo = "<class id=\""+Integer.parseInt(folderName.trim())+"\" nfragments=\""+folder.listFiles().length+"\">";
                String strclassInfo = "<class id=\"" + Integer.parseInt(folderName.trim()) + "\" nfragments=\"" + folder.listFiles().length + "\">";
                StringBuffer sbclassInfo = new StringBuffer();//
                sbclassInfo.append(strclassInfo);


                File cloneFragmentList[] = folder.listFiles();
                for (int j = 0; j < cloneFragmentList.length; j++) {

                    /*
                     * Since we iterate over all files, som of them may be
                     * invalid. SO check integretity first
                     */
                    /*
                     * A simple check is that all file ends with .txt and
                     * contain 'node' word
                     */

                    String cloneFragmentName = cloneFragmentList[j].getName();
                    if (cloneFragmentName.endsWith(".txt") && cloneFragmentName.contains("Node")) {

                        String sourceFile = "";
                        String startline = "";
                        String endline = "";
                        String pcid;
                        String splittedStr[] = cloneFragmentName.split("\\s+");
                        System.out.println("Total item = " + splittedStr.length);
                        for (int in = 0; in < splittedStr.length; in++) {
                            System.out.println("item = " + splittedStr[in]);
                        }

                        String splittedStrLine[] = splittedStr[1].split("-");
                        //  System.out.println(splittedStr[1]+"spliterr1");
                        startline = splittedStrLine[0].substring(1);
                        endline = splittedStrLine[1].substring(0, splittedStrLine[1].length() - 1);

                        pcid = splittedStr[3].substring(0, splittedStr[3].length() - 5);
                        //     System.out.println(splittedStr[3]+"spliterr3");

                        sourceFile = (String) hm.get(pcid.trim());

                        // classInfo= classInfo +"\n"+"<source file=\""+sourceFile+"\" startline=\""+startline+"\" endline=\""+endline+"\""+" pcid=\""+pcid+"\"/>";
                        // classInfo= classInfo +"</source>"; 
                        strclassInfo = "\n" + "<source file=\"" + sourceFile + "\" startline=\"" + startline + "\" endline=\"" + endline + "\"" + " pcid=\"" + pcid + "\">";
                        sbclassInfo.append(strclassInfo);
                        sbclassInfo.append("</source>");
                    } else {
                        continue;
                    }
                }
                // classInfo=classInfo+"\n</class>\n";
                sbclassInfo.append("\n</class>\n");

                //cloneClassList=cloneClassList+classInfo;
                sbcloneClassList.append(sbclassInfo.toString());
            }
        }
        // return "<cloneDetectionResult>"+"\n"+cloneClassList+"\n</cloneDetectionResult>";
        sbcloneClassList.append("\n</cloneDetectionResult>");
        return sbcloneClassList.toString();

    }

    protected static void readSummary(File file, HashMap hm) {
        try {
            /*
             * duplicates_summary map pcid to file name We parse the file and
             * create a hashmap on the id
             */
            File duplicateSummaryfile = new File(file.getAbsolutePath() + System.getProperty("file.separator") + "duplicates_summary.txt");
            FileReader fin = new FileReader(duplicateSummaryfile);
            BufferedReader br = new BufferedReader(fin);
            //Read the whole file content first
            StringBuffer fileContent = new StringBuffer("");
            String temp; //a variable to read each line of the file
            int counter = 0;
            while ((temp = br.readLine()) != null) {
                //counter value 0 indicates first line
                //System.out.println(temp);//////////
                if (counter == 0) {
                    fileContent.append(temp);
                } else {
                    fileContent.append(", " + temp); //we append a comma after each line so that it can split
                } //we append a comma after each line so that it can split
                counter++;
            }
            //System.out.println("-------------------------------------------------");
            // System.out.println(fileContent.toString());//

            // System.out.println("-------------------------------------------------");

            //Split the file content where each part indicate a clone entry
            String[] splittedStr = new String(fileContent).split(",\\s+");

            for (int i = 0; i < splittedStr.length; i++) {
                String[] splittedStrItem = splittedStr[i].split("-|:", 2);
                //  System.out.println(splittedStrItem[1]+"--i");
                String[] splittedStrItem3 = splittedStrItem[1].split(":", 2);

                String strsplittedstritem30 = splittedStrItem3[0].replaceAll("/", "\\\\");

                String[] splittedStrItem2 = strsplittedstritem30.split("\\u005C", 2);


                //  System.out.println(splittedStrItem2[1]+"--i");

                // System.out.println(splittedStrItem[0].trim()+"--0");

                String strtemp = "\\";

                strtemp += splittedStrItem2[1].trim();
                // System.out.println(strtemp);
                //hm.put(splittedStrItem[0].trim(), splittedStrItem[1].trim());
                hm.put(splittedStrItem[0].trim(), strtemp);
            }
        } catch (IOException ex) {
            Logger.getLogger(FileConverterForSimscan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected static String readDetectionInfo(File file) {

        String temp = "";
        String output = "";
        StringBuffer sboutput = new StringBuffer();//
        File detectionInfoFile = new File(file.getAbsolutePath() + System.getProperty("file.separator") + "log_simscan.txt");
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(detectionInfoFile);
            br = new BufferedReader(fr);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            while ((temp = br.readLine()) != null) {
                /*
                 * If any parameter specified for clone detection are the
                 * explicit parameter. So first, we will encounter them, if they
                 * exist Then comes the other parameters starting with the
                 * line:"Scan parameters here" So, we first check for the
                 * explicit. If they exist then we continuously read them until
                 * we encounter the line with"Scan parameters here". then we
                 * read the implicit parameters
                 */
                if (temp.trim().endsWith("Explicit scan parameters are:")) {
                    while (true) {
                        String str = br.readLine();
                        if (str.trim().endsWith("Scan parameters are:")) {
                            //explicit ends. We now start reading the implicit
                            str = br.readLine();
                            String splittedStr[] = str.split("INFO:");
                            splittedStr = splittedStr[1].split("=");
                            //  output=output+splittedStr[0]+"=\""+splittedStr[1]+"\"  ";
                            sboutput.append(splittedStr[0]);
                            sboutput.append("=\"");
                            sboutput.append(splittedStr[1]);
                            sboutput.append("\"  ");


                            //read the rest information
                            for (int i = 0; i < 14; i++) {
                                str = br.readLine();
                                //System.out.println(str);
                                splittedStr = str.split("=");
                                //output=output+splittedStr[0]+"=\""+splittedStr[1]+"\"  ";
                                sboutput.append(splittedStr[0]);
                                sboutput.append("=\"");
                                sboutput.append(splittedStr[1]);
                                sboutput.append("\"  ");

                            }
                            output = sboutput.toString();
                            return output;

                        } else {
                            //read the explicit parameters
                            String splittedStr[] = str.split("INFO:");
                            splittedStr = splittedStr[1].split("=");
                            //output = output + splittedStr[0]+"=\""+splittedStr[1]+"\"  ";
                            sboutput.append(splittedStr[0]);
                            sboutput.append("=\"");
                            sboutput.append(splittedStr[1]);
                            sboutput.append("\"  ");
                        }

                    }
                } /*
                 * If the if loop is unsuccessful, it means there is no explicit
                 * parameter we now use the else if part to read the rest
                 * parameters.
                 */ else if (temp.trim().endsWith("Scan parameters are:")) {
                    String str = "";
                    str = br.readLine();
                    String splittedStr[] = str.split("INFO:");
                    splittedStr = splittedStr[1].split("=");

                    //  output=output+splittedStr[0]+"=\""+splittedStr[1]+"\" ";

                    sboutput.append(splittedStr[0]);
                    sboutput.append("=\"");
                    sboutput.append(splittedStr[1]);
                    sboutput.append("\" ");

                    for (int i = 0; i < 14; i++) {
                        str = br.readLine();
                        splittedStr = str.split("=");
                        //output=output+splittedStr[0]+"=\""+splittedStr[1]+"\" ";
                        sboutput.append(splittedStr[0]);
                        sboutput.append("=\"");
                        sboutput.append(splittedStr[1]);
                        sboutput.append("\" ");
                    }
                    output = sboutput.toString();
                    return output;
                }
            }
            if(br!=null)
                br.close();
            if(fr!=null)
                fr.close();
        } catch (IOException ex) {
            Logger.getLogger(FileConverterForSimscan.class.getName()).log(Level.SEVERE, null, ex);
        }
        output = sboutput.toString();
        return output;
    }
}
