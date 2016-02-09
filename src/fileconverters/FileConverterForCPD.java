/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fileconverters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class FileConverterForCPD extends Fileconverters {

    public String convert(File resultFile, ArrayList list) {
        ArrayList<String> listline = new ArrayList();

        String basepath = (String) list.get(0);
        String filename = (String) list.get(1);
        String output = "";
        readConvertedFile(filename, listline);
        // System.out.println("after readConvertedFile");

        // System.out.println((String)list.get(2));
        if (list.size() == 2) {
            output = converteToGCF(listline, basepath);
        } else if (list.size() == 3) {
            String strminimumlines = (String) list.get(2);
            int minimumlines = Integer.parseInt(strminimumlines);
            output = converteToGCFmin(listline, basepath, minimumlines);
        }
        return output;
    }

    public static String readConvertedFile(String filename, ArrayList<String> list) {

        // File f = new File(filename);

        //java.io.FileReader in = null;
        java.io.BufferedReader infile = null;

        // Get file name from the text field


        String inLine;
        String filecontent = "";
        

        try {
            // Create a buffered stream
            infile = new java.io.BufferedReader(new java.io.FileReader(filename));

            // Read a line
            inLine = infile.readLine();

            boolean firstLine = true;
            // Append the line to the text area

            while (inLine != null) {
                list.add(inLine);
                //    filecontent += inLine + "\n";
                inLine = infile.readLine();
            }
        } catch (java.io.FileNotFoundException ex) {
            System.out.println("File not found: " + filename);
        } catch (java.io.IOException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if (infile != null) {
                    infile.close();
                }
            } catch (java.io.IOException ex) {
                System.out.println(ex.getMessage());
            }
        }

        return filecontent;
    }

    public static String converteToGCF(ArrayList<String> strlist, String basepath) {

        StringBuffer sbGCFfile = new StringBuffer();
        sbGCFfile.append("<CloneClasses>\r\n");
        //String GCFfile = "<CloneClasses>\r\n";
        int len = basepath.length();
        int cloneClassID = 0;
        //   System.out.println(strlist.size());
        int sum = 0;
        int tokenThreshold = 100;
        int numItem = 0;
        int minimum = 0;

        for (int i = 1; i < strlist.size(); i++) {
            // System.out.println(strlist.get(i));
            String splittedStrLine[] = strlist.get(i).split(",");
            //   System.out.println("---------");
            //   System.out.println(i);
            // System.out.println(strlist.get(i));

            if (splittedStrLine.length >= 7) {
                sbGCFfile.append( "<CloneClass>\r\n");
                cloneClassID++;
                sbGCFfile.append( "<ID>");
                sbGCFfile.append(Integer.toString(cloneClassID));
                sbGCFfile.append("</ID>\r\n");
              /*  GCFfile += "<CloneClass>\r\n";
                cloneClassID++;
                GCFfile += "<ID>";
                GCFfile += Integer.toString(cloneClassID);
                GCFfile += "</ID>\r\n";*/


                String strnumfragment = splittedStrLine[2];
                int numfragment = Integer.parseInt(strnumfragment);

                String strnumLine = splittedStrLine[0];
                int numLine = Integer.parseInt(strnumLine);

                String strnumToken = splittedStrLine[1];///
                int numToken = Integer.parseInt(strnumToken);

                // System.out.println(strnumLine+strnumToken);
                int lines = tokenThreshold * numLine / numToken;
                sum += lines;
                if (minimum == 0 || lines < minimum) {
                    minimum = numLine;
                }

                numItem++;



                int k = 2;
                //   System.out.println(numfragment);
                for (int j = 1; j <= numfragment && (2 + j * 2) < splittedStrLine.length; j++) {
                    k = k + 1;
                    String strstartline = splittedStrLine[k];
                    int startline = Integer.parseInt(strstartline);
                    int endline = startline + numLine - 1;
                    String strendline = Integer.toString(endline);

                    k = k + 1;
                    String strfiledir = splittedStrLine[k];
                    //String strsplitdir[]=strfiledir.split( basepath);
                    //String strfile=strsplitdir[1];
                    String strfile = strfiledir.substring(len);
                    // System.out.println(strfile);

                    sbGCFfile.append( "<Clone>\r\n");
                    sbGCFfile.append( "<Fragment>\r\n");
                    sbGCFfile.append("<File>");
                    
                    String strreplaced = strfile.replaceAll("/", "\\\\");
                    
                   // sbGCFfile.append(strfile);
                    sbGCFfile.append(strreplaced);
                    
                    sbGCFfile.append("</File>\r\n");
                    sbGCFfile.append("<Start>");
                    sbGCFfile.append(strstartline);
                    sbGCFfile.append("</Start>\r\n");
                    sbGCFfile.append("<End>");
                    sbGCFfile.append(strendline);
                    sbGCFfile.append("</End>\r\n");
                    sbGCFfile.append( "</Fragment>\r\n");
                    sbGCFfile.append("</Clone>\r\n");

                 /*   GCFfile += "<Clone>\r\n";
                    GCFfile += "<Fragment>\r\n";
                    GCFfile += "<File>";
                    GCFfile += strfile;
                    GCFfile += "</File>\r\n";
                    GCFfile += "<Start>";
                    GCFfile += strstartline;
                    GCFfile += "</Start>\r\n";
                    GCFfile += "<End>";
                    GCFfile += strendline;
                    GCFfile += "</End>\r\n";
                    GCFfile += "</Fragment>\r\n";
                    GCFfile += "</Clone>\r\n";*/
                    // System.out.println("fragment");

                }
                sbGCFfile.append( "</CloneClass>\r\n");
                //GCFfile += "</CloneClass>\r\n";
            }


        }

        ////////////////////////////////
         sbGCFfile.append( "</CloneClasses>\r\n");
       // GCFfile += "</CloneClasses>\r\n";
        if (numItem != 0) {
            // System.out.print("average lines per ");
            //System.out.print(tokenThreshold);
            //System.out.print(" tokens\n");            
            //System.out.println(sum/numItem);

            //System.out.print("minmum line:");
            System.out.println(minimum);

        }

        return sbGCFfile.toString();

    }

    public static String converteToGCFmin(ArrayList<String> strlist, String basepath, int minimumlines) {


        StringBuffer sbGCFfile = new StringBuffer();
        sbGCFfile.append("<CloneClasses>\r\n");
        String GCFfile = "";

        // String GCFfile = "<CloneClasses>\r\n";
        int len = basepath.length();
        int cloneClassID = 0;
        //   System.out.println(strlist.size());
        int sum = 0;
        int tokenThreshold = 100;
        //    int numItem = 0;
        int minimum = 0;
        // System.out.println( strlist.size());
        for (int i = 1; i < strlist.size(); i++) {
            //  System.out.println(i);
            String splittedStrLine[] = strlist.get(i).split(",");
            //   System.out.println("---------");
            //   System.out.println(i);
            // System.out.println(strlist.get(i));

            if (splittedStrLine.length >= 7) {
                /*
                 * GCFfile += "<CloneClass>\r\n"; cloneClassID++; GCFfile +=
                 * "<ID>"; GCFfile += Integer.toString(cloneClassID); GCFfile +=
                 * "</ID>\r\n";
                 */


                cloneClassID++;
                StringBuffer sb = new StringBuffer();
                sb.append("<CloneClass>\r\n");
                sb.append("<ID>");
                sb.append(cloneClassID);
                sb.append("</ID>\r\n");

                /*
                 * String GCFfileclass = ""; GCFfileclass += "<CloneClass>\r\n";
                 * GCFfileclass += "<ID>"; GCFfileclass +=
                 * Integer.toString(cloneClassID); GCFfileclass += "</ID>\r\n";
                 */

                int fragmentcountofclass = 0;


                String strnumfragment = splittedStrLine[2];
                int numfragment = Integer.parseInt(strnumfragment);

                String strnumLine = splittedStrLine[0];
                int numLine = Integer.parseInt(strnumLine);

                //  System.out.println(numLine);

                String strnumToken = splittedStrLine[1];///
                int numToken = Integer.parseInt(strnumToken);

                // System.out.println(strnumLine+strnumToken);
              /*
                 * int lines=tokenThreshold*numLine/numToken; sum+=lines;
                 * if(minimum==0||lines<minimum ) { minimum=numLine; }
                 */
                //   numItem++;



                int k = 2;
                //   System.out.println(numfragment);
                for (int j = 1; j <= numfragment && (2 + j * 2) < splittedStrLine.length; j++) {
                    k = k + 1;
                    String strstartline = splittedStrLine[k];
                    int startline = Integer.parseInt(strstartline);
                    int endline = startline + numLine - 1;
                    String strendline = Integer.toString(endline);

                    k = k + 1;
                    String strfiledir = splittedStrLine[k];
                    //String strsplitdir[]=strfiledir.split( basepath);
                    //String strfile=strsplitdir[1];
                    String strfile = strfiledir.substring(len);
                    // System.out.println(strfile);


                    /*
                     * GCFfile += "<Clone>\r\n"; GCFfile += "<Fragment>\r\n";
                     * GCFfile += "<File>"; GCFfile += strfile; GCFfile +=
                     * "</File>\r\n"; GCFfile += "<Start>"; GCFfile +=
                     * strstartline; GCFfile += "</Start>\r\n"; GCFfile +=
                     * "<End>"; GCFfile += strendline; GCFfile += "</End>\r\n";
                     * GCFfile += "</Fragment>\r\n"; GCFfile += "</Clone>\r\n";
                     */
                    // System.out.println("fragment");
                    if (numLine >= minimumlines) {
                        fragmentcountofclass++;
                        sb.append("<Clone>\r\n");
                        sb.append("<Fragment>\r\n");
                        sb.append("<File>");
                      //  sb.append(strfile);
                        String strreplaced = strfile.replaceAll("/", "\\\\");                
                        sb.append(strreplaced);
                        sb.append("</File>\r\n");
                        sb.append("<Start>");
                        sb.append(strstartline);
                        sb.append("</Start>\r\n");
                        sb.append("<End>");
                        sb.append(strendline);
                        sb.append("</End>\r\n");
                        sb.append("</Fragment>\r\n");
                        sb.append("</Clone>\r\n");


                        /*
                         * GCFfileclass += "<Clone>\r\n"; GCFfileclass +=
                         * "<Fragment>\r\n"; GCFfileclass += "<File>";
                         * GCFfileclass += strfile; System.out.println(strfile);
                         * GCFfileclass += "</File>\r\n"; GCFfileclass +=
                         * "<Start>"; GCFfileclass += strstartline; GCFfileclass
                         * += "</Start>\r\n"; GCFfileclass += "<End>";
                         * GCFfileclass += strendline; GCFfileclass +=
                         * "</End>\r\n"; GCFfileclass += "</Fragment>\r\n";
                         * GCFfileclass += "</Clone>\r\n";
                         */

                    }
                }

                if (fragmentcountofclass >= 2) {
                    sbGCFfile.append(sb.toString());
                    sbGCFfile.append("</CloneClass>\r\n");

                    //GCFfile += GCFfileclass;
                    //GCFfile += "</CloneClass>\r\n";

                    // GCFfile += "</CloneClass>\r\n";
                }


            }

            //////////////////////////////////
        }
        sbGCFfile.append("</CloneClasses>\r\n");
        // GCFfile += "</CloneClasses>\r\n";
        // if (numItem != 0) {
        // System.out.print("average lines per ");
        //System.out.print(tokenThreshold);
        //System.out.print(" tokens\n");            
        //System.out.println(sum/numItem);

        //System.out.print("minmum line:");


        //  }          


        System.out.println(minimumlines);
        return sbGCFfile.toString();
        //  return GCFfile;
    }
}
