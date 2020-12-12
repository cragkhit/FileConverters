/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ucl.fileconverters;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class FileConverterForConQAT extends Fileconverters {

    public String convert(File resultFile, ArrayList list) {
        ArrayList<String> filelist = new ArrayList();
        ArrayList<String> clonelist = new ArrayList();
        String basepath = (String) list.get(0);
        String filename = (String) list.get(1);
        String strminimumlines = (String) list.get(2);
        String output = "";
        int minimumlines = Integer.parseInt(strminimumlines.trim());

        readConvertedFile(filename, filelist, clonelist, basepath);


        // System.out.println(filelist.size());
        //  System.out.println(clonelist.size());

        output = converteToGCF(clonelist, filelist, minimumlines);

        return output;
    }

    public static String readConvertedFile(String filename, ArrayList<String> filelist, ArrayList<String> clonelist, String basepath) {

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
                //////////////////////////
                if (inLine.trim().startsWith("<sourceFile")) {
                    String strsplitted[] = inLine.split("\"");
                    String strsplittedfurther[] = strsplitted[3].split(basepath, 2);
                    //int pos = strsplittedfurther.length;
                    //if (pos >= 1) {
                    //  String strfile = strsplittedfurther[pos - 1];
                    String strfile = strsplittedfurther[1];
                    //strfile.replace( "//","\\u005C");
                    String strreplaced = strfile.replaceAll("/", "\\\\");


                    //  System.out.println(strreplaced);


                    filelist.add(strreplaced);
                    //}
                } else {
                    clonelist.add(inLine);

                }

                ///////////////////////////////////       

                //filecontent += inLine + "\n";
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

    public static String converteToGCF(ArrayList<String> clonelist, ArrayList<String> filelist, int minimumlines) {
        StringBuffer sb = new StringBuffer();
        sb.append("<CloneClasses>\r\n");
        //String GCFfile = "<CloneClasses>\r\n";
        int minimum = 0;
        int fragmentnum = 0;
        int allclone = 0;

        int i = 0;
        while (i < clonelist.size()) {
            String strline = clonelist.get(i);
            if (strline.trim().startsWith("<cloneClass")) {

                boolean endcloneclass = false;
                String strsplitcloneclass[] = strline.split("\"");
                String strID = strsplitcloneclass[3];
                // System.out.println(strID);
                StringBuffer sbGCFfile = new StringBuffer();
                sbGCFfile.append("<CloneClass>\r\n");
                sbGCFfile.append("<ID>");
                sbGCFfile.append(strID);
                sbGCFfile.append("</ID>\r\n");
                /*
                 * String GCFfileclass=""; GCFfileclass += "<CloneClass>\r\n";
                 * GCFfileclass += "<ID>"; GCFfileclass += strID; GCFfileclass
                 * += "</ID>\r\n";
                 */
                int fragmentcountofclass = 0;

                i++;
                while (!endcloneclass && i < clonelist.size()) {
                    strline = clonelist.get(i);
                    if (strline.trim().startsWith("<clone")) {
                        allclone++;

                        String strsplitclone[] = strline.split("\"");
                        String strLinecount = strsplitclone[3];
                        //System.out.println(strLinecount); 
                        int linecount = Integer.parseInt(strLinecount);
                        if (linecount >= minimumlines) {

                            fragmentnum++;
                            fragmentcountofclass++;

                            if (minimum == 0 || linecount < minimum) {
                                minimum = linecount;
                            }


                            String strstartline = strsplitclone[7];
                            int startline = Integer.parseInt(strstartline);
                            int endline = startline + linecount - 1;
                            String strendline = Integer.toString(endline);
                            //System.out.println(strstartline);
                            String strfileID = strsplitclone[9];
                            int fileID = Integer.parseInt(strfileID);
                            String strfile = "";
                            if (fileID < filelist.size()) {
                                strfile = filelist.get(fileID);

                            }
                            // System.out.println(strfileID);
                            sbGCFfile.append("<Clone>\r\n");
                            sbGCFfile.append("<Fragment>\r\n");
                            sbGCFfile.append("<File>");
                            sbGCFfile.append(strfile);
                            sbGCFfile.append("</File>\r\n");
                            sbGCFfile.append("<Start>");
                            sbGCFfile.append(strstartline);
                            sbGCFfile.append("</Start>\r\n");
                            sbGCFfile.append("<End>");
                            sbGCFfile.append(strendline);
                            sbGCFfile.append("</End>\r\n");
                            sbGCFfile.append("</Fragment>\r\n");
                            sbGCFfile.append("</Clone>\r\n");
                            /*
                             * GCFfileclass += "<Clone>\r\n"; GCFfileclass +=
                             * "<Fragment>\r\n"; GCFfileclass += "<File>";
                             * GCFfileclass += strfile; GCFfileclass +=
                             * "</File>\r\n"; GCFfileclass += "<Start>";
                             * GCFfileclass += strstartline; GCFfileclass +=
                             * "</Start>\r\n"; GCFfileclass += "<End>";
                             * GCFfileclass += strendline; GCFfileclass +=
                             * "</End>\r\n"; GCFfileclass += "</Fragment>\r\n";
                             * GCFfileclass += "</Clone>\r\n";
                             */
                        }
                    }
                    if (strline.trim().startsWith("</cloneClass>")) {
                        endcloneclass = true;
                    }
                    i++;

                }
                sbGCFfile.append("</CloneClass>\r\n");

                //GCFfileclass += "</CloneClass>\r\n";
                if (fragmentcountofclass >= 2) {
                    sb.append(sbGCFfile.toString());
                    //GCFfile += GCFfileclass;
                }


            } else {
                i++;
            }


        }
        sb.append("</CloneClasses>\r\n");
        //GCFfile += "</CloneClasses>\r\n";
        // System.out.print("minimumLines:");
        System.out.println(minimum);
        //  System.out.print("fragmentnum:");
        //System.out.println(fragmentnum);
        // System.out.print("allclone:");
        //System.out.println(allclone);
        //return GCFfile;
        return sb.toString();

    }
}
