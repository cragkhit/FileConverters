/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fileconverters;

import de.uni_bremen.st.rcf.persistence.*;
import de.uni_bremen.st.rcf.model.*;
import de.uni_bremen.st.rcf.util.*;
import de.uni_bremen.st.rcf.schema.AttributeType;
import de.uni_bremen.st.rcf.persistence.AbstractRelationBinding;
import de.uni_bremen.st.rcf.persistence.AbstractPersistenceManager;
import java.util.List;
import java.util.ArrayList;



import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author muhammad
 */
public class FileConverterForRCF extends Fileconverters {

    //   @Override
    public String convert(java.io.File f, ArrayList list) {
        String output = "";
        try {
            String filename = (String) list.get(0);
            java.io.File fold = new java.io.File(filename);
            if (!fold.exists()) {
                System.out.println("the file does not exist!!");
            } else {

                AbstractPersistenceManager apm = PersistenceManagerFactory.getPersistenceManager(fold);
                RCF rcfold;
                if (apm != null) {
                    rcfold = apm.load(fold);
                    if (list.size() == 1) {
                        output = converter(rcfold);
                    } else if (list.size() == 2) {
                        String strminimiumlines = (String) list.get(1);
                        int minimumlines = Integer.parseInt(strminimiumlines);
                        output = convertermin(rcfold, minimumlines);
                    }
                }

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
        }
        return output;
    }

    public static String converter(RCF rcfold) {

        StringBuffer sbGCFfile = new StringBuffer();
        sbGCFfile.append("<CloneClasses>\r\n");
        // String GCFfile = "<CloneClasses>\r\n";
        int minimumline = 0;

        for (Version v : rcfold.getVersions()) {
            ///////////////clone class////////////////////////////////
            for (CloneClass cc : v.getCloneClasses()) {

                /*
                 * GCFfile += "<CloneClass>\r\n"; int cloneClassID = cc.getId();
                 * GCFfile += "<ID>"; GCFfile += Integer.toString(cloneClassID);
                 * GCFfile += "</ID>\r\n";
                 */

                StringBuffer sb = new StringBuffer();
                int cloneClassID = cc.getId();
                sb.append("<CloneClass>\r\n");
                sb.append("<ID>");
                sb.append(Integer.toString(cloneClassID));
                sb.append("</ID>\r\n");


                /*
                 * String GCFfileclass = ""; GCFfileclass += "<CloneClass>\r\n";
                 * GCFfileclass += "<ID>"; int cloneClassID = cc.getId();
                 * GCFfileclass += Integer.toString(cloneClassID); GCFfileclass
                 * += "</ID>\r\n";
                 *
                 */



                for (Fragment f : cc.getFragments()) {

                    SourcePosition start = f.getStart();
                    SourcePosition end = f.getEnd();
                    int linecount = end.getLine() - start.getLine() + 1;

                    if (minimumline == 0 || linecount < minimumline) {
                        minimumline = linecount;

                    }


                    File startfile = start.getFile();
                    Directory startdirectory = startfile.getDirectory();
                    String strpath = startdirectory.getPath() + "\\" + startfile.getName();
                    
                    String strreplace=strpath.replaceAll("/","\\\\");                        
                    strpath=strreplace;
                    String strstart = "\\";
                    if (!strpath.startsWith("\\")) {
                        strpath = strstart.concat(strpath);
                    }

                    String path = "<File>" + strpath + "</File>\r\n";
                    String line = "";
                    line += "<Start>" + start.getLine() + "</Start>\r\n";

                    line += "<End>" + end.getLine() + "</End>\r\n";

                    sb.append("<Clone>\r\n");
                    sb.append("<Fragment>\r\n");
                    sb.append(path);
                    sb.append(line);
                    sb.append("</Fragment>\r\n</Clone>\r\n");


                    // GCFfileclass += "<Clone>\r\n" + "<Fragment>\r\n" + path + line + "</Fragment>\r\n</Clone>\r\n";

                }


                sbGCFfile.append(sb.toString());
                sbGCFfile.append("</CloneClass>\r\n");

                //GCFfile += GCFfileclass;
                //GCFfile += "</CloneClass>\r\n";

            }

////////////////////////////////////////////////////////////////////////////////////////////////////
            //////////////////////clone pairs////////////////////
            // String GCFfile = "<CloneClasses>\r\n";///
            for (ClonePair cp : v.getClonePairs()) {
                /*
                 * GCFfile += "<CloneClass>\r\n"; int cloneClassID = cp.getId();
                 * GCFfile += "<ID>"; GCFfile += Integer.toString(cloneClassID);
                 * GCFfile += "</ID>\r\n";
                 */


                Fragment fl = cp.getLeft();
                SourcePosition startleft = fl.getStart();
                SourcePosition endleft = fl.getEnd();
                int linecount1 = endleft.getLine() - startleft.getLine() + 1;

                Fragment fr = cp.getRight();
                SourcePosition startright = fr.getStart();
                SourcePosition endright = fr.getEnd();
                int linecountr = endright.getLine() - startright.getLine() + 1;



                int cloneClassID = cp.getId();
                sbGCFfile.append("<CloneClass>\r\n");
                sbGCFfile.append("<ID>");
                sbGCFfile.append(Integer.toString(cloneClassID));
                sbGCFfile.append("</ID>\r\n");

                /*
                 * GCFfile += "<CloneClass>\r\n"; int cloneClassID = cp.getId();
                 * GCFfile += "<ID>"; GCFfile += Integer.toString(cloneClassID);
                 * GCFfile += "</ID>\r\n";
                 */

                File startfile = startleft.getFile();
                Directory startdirectory = startfile.getDirectory();
                String strpath = startdirectory.getPath() + "\\" + startfile.getName();
                 String strreplace=strpath.replaceAll("/","\\\\");                        
                  strpath=strreplace;
                String strstart = "\\";
                if (!strpath.startsWith("\\")) {
                    strpath = strstart.concat(strpath);
                }
                String path = "<File>" + strpath + "</File>\r\n";
                String line = "";
                line += "<Start>" + startleft.getLine() + "</Start>\r\n";
                line += "<End>" + endleft.getLine() + "</End>\r\n";
                //  GCFfile += "<Clone>\r\n" + "<Fragment>\r\n" + path + line + "</Fragment>\r\n</Clone>\r\n";

                sbGCFfile.append("<Clone>\r\n");
                sbGCFfile.append("<Fragment>\r\n");
                sbGCFfile.append(path);
                sbGCFfile.append(line);
                sbGCFfile.append("</Fragment>\r\n</Clone>\r\n");

                startfile = startright.getFile();
                startdirectory = startfile.getDirectory();
                strpath = startdirectory.getPath() + "\\" + startfile.getName();
                 strreplace=strpath.replaceAll("/","\\\\");                        
                 strpath=strreplace;
                /*  strstart = "/";
                if (!strpath.startsWith("/")) {
                    strpath = strstart.concat(strpath);
                }*/
                path = "<File>" + strpath + "</File>\r\n";
                line = "";
                line += "<Start>" + startright.getLine() + "</Start>\r\n";
                line += "<End>" + endright.getLine() + "</End>\r\n";
                // GCFfile += "<Clone>\r\n" + "<Fragment>\r\n" + path + line + "</Fragment>\r\n</Clone>\r\n";
                sbGCFfile.append("<Clone>\r\n");
                sbGCFfile.append("<Fragment>\r\n");
                sbGCFfile.append(path);
                sbGCFfile.append(line);
                sbGCFfile.append("</Fragment>\r\n</Clone>\r\n");
                sbGCFfile.append("</CloneClass>\r\n");
                //GCFfile += "</CloneClass>\r\n";

            }







            //GCFfile += "</CloneClasses>\r\n";
            sbGCFfile.append("</CloneClasses>\r\n");

        }

        System.out.println(minimumline);
        return sbGCFfile.toString();
    }

    public static String convertermin(RCF rcfold, int minimumlines) {

        StringBuffer sbGCFfile = new StringBuffer();
        sbGCFfile.append("<CloneClasses>\r\n");
        // String GCFfile = "<CloneClasses>\r\n";
        // int minimumline = 0;

        for (Version v : rcfold.getVersions()) {
            ///////////////clone class////////////////////////////////
            for (CloneClass cc : v.getCloneClasses()) {

                /*
                 * GCFfile += "<CloneClass>\r\n"; int cloneClassID = cc.getId();
                 * GCFfile += "<ID>"; GCFfile += Integer.toString(cloneClassID);
                 * GCFfile += "</ID>\r\n";
                 */

                StringBuffer sb = new StringBuffer();
                int cloneClassID = cc.getId();
                sb.append("<CloneClass>\r\n");
                sb.append("<ID>");
                sb.append(Integer.toString(cloneClassID));
                sb.append("</ID>\r\n");


                /*
                 * String GCFfileclass = ""; GCFfileclass += "<CloneClass>\r\n";
                 * GCFfileclass += "<ID>"; int cloneClassID = cc.getId();
                 * GCFfileclass += Integer.toString(cloneClassID); GCFfileclass
                 * += "</ID>\r\n";
                 *
                 */

                int fragmentcountofclass = 0;

                for (Fragment f : cc.getFragments()) {

                    SourcePosition start = f.getStart();
                    SourcePosition end = f.getEnd();
                    int linecount = end.getLine() - start.getLine() + 1;
                    if (linecount >= minimumlines) {
                        fragmentcountofclass++;


                        File startfile = start.getFile();
                        Directory startdirectory = startfile.getDirectory();
                        String strpath = startdirectory.getPath() + "\\" + startfile.getName();
                        
                        String strreplace=strpath.replaceAll("/","\\\\");
                        
                        strpath=strreplace;
                        String strstart = "\\";                        
                                    
                        if (!strpath.startsWith("\\")) {
                           strpath = strstart.concat(strpath);
                       }



                        String path = "<File>" + strpath + "</File>\r\n";
                        String line = "";
                        line += "<Start>" + start.getLine() + "</Start>\r\n";

                        line += "<End>" + end.getLine() + "</End>\r\n";

                        sb.append("<Clone>\r\n");
                        sb.append("<Fragment>\r\n");
                        sb.append(path);
                        sb.append(line);
                        sb.append("</Fragment>\r\n</Clone>\r\n");


                        // GCFfileclass += "<Clone>\r\n" + "<Fragment>\r\n" + path + line + "</Fragment>\r\n</Clone>\r\n";

                    }
                }
                if (fragmentcountofclass >= 2) {
                    sbGCFfile.append(sb.toString());
                    sbGCFfile.append("</CloneClass>\r\n");

                    //GCFfile += GCFfileclass;
                    //GCFfile += "</CloneClass>\r\n";
                }
            }

////////////////////////////////////////////////////////////////////////////////////////////////////
            //////////////////////clone pairs////////////////////
            // String GCFfile = "<CloneClasses>\r\n";///
            for (ClonePair cp : v.getClonePairs()) {
                /*
                 * GCFfile += "<CloneClass>\r\n"; int cloneClassID = cp.getId();
                 * GCFfile += "<ID>"; GCFfile += Integer.toString(cloneClassID);
                 * GCFfile += "</ID>\r\n";
                 */


                Fragment fl = cp.getLeft();
                SourcePosition startleft = fl.getStart();
                SourcePosition endleft = fl.getEnd();
                int linecount1 = endleft.getLine() - startleft.getLine() + 1;

                Fragment fr = cp.getRight();
                SourcePosition startright = fr.getStart();
                SourcePosition endright = fr.getEnd();
                int linecountr = endright.getLine() - startright.getLine() + 1;

                if (linecount1 >= minimumlines && linecountr >= minimumlines) {

                    int cloneClassID = cp.getId();
                    sbGCFfile.append("<CloneClass>\r\n");
                    sbGCFfile.append("<ID>");
                    sbGCFfile.append(Integer.toString(cloneClassID));
                    sbGCFfile.append("</ID>\r\n");

                    /*
                     * GCFfile += "<CloneClass>\r\n"; int cloneClassID =
                     * cp.getId(); GCFfile += "<ID>"; GCFfile +=
                     * Integer.toString(cloneClassID); GCFfile += "</ID>\r\n";
                     */

                    File startfile = startleft.getFile();
                    Directory startdirectory = startfile.getDirectory();
                    String strpath = startdirectory.getPath() + "\\" + startfile.getName();
                    
                    String strreplace=strpath.replaceAll("/","\\\\");                        
                    strpath=strreplace;
                    String strstart = "\\";
                    if (!strpath.startsWith("\\")) {
                        strpath = strstart.concat(strpath);
                    }
                    String path = "<File>" + strpath + "</File>\r\n";
                    String line = "";
                    line += "<Start>" + startleft.getLine() + "</Start>\r\n";
                    line += "<End>" + endleft.getLine() + "</End>\r\n";
                    //  GCFfile += "<Clone>\r\n" + "<Fragment>\r\n" + path + line + "</Fragment>\r\n</Clone>\r\n";

                    sbGCFfile.append("<Clone>\r\n");
                    sbGCFfile.append("<Fragment>\r\n");
                    sbGCFfile.append(path);
                    sbGCFfile.append(line);
                    sbGCFfile.append("</Fragment>\r\n</Clone>\r\n");

                    startfile = startright.getFile();
                    startdirectory = startfile.getDirectory();
                    strpath = startdirectory.getPath() + "\\" + startfile.getName();
                    strreplace=strpath.replaceAll("/","\\\\");                        
                    strpath=strreplace;
                    strstart = "\\";
                    if (!strpath.startsWith("\\")) {
                        strpath = strstart.concat(strpath);
                    }
                    path = "<File>" + strpath + "</File>\r\n";
                    line = "";
                    line += "<Start>" + startright.getLine() + "</Start>\r\n";
                    line += "<End>" + endright.getLine() + "</End>\r\n";
                    // GCFfile += "<Clone>\r\n" + "<Fragment>\r\n" + path + line + "</Fragment>\r\n</Clone>\r\n";
                    sbGCFfile.append("<Clone>\r\n");
                    sbGCFfile.append("<Fragment>\r\n");
                    sbGCFfile.append(path);
                    sbGCFfile.append(line);
                    sbGCFfile.append("</Fragment>\r\n</Clone>\r\n");
                    sbGCFfile.append("</CloneClass>\r\n");
                    //GCFfile += "</CloneClass>\r\n";

                }






            }
            //GCFfile += "</CloneClasses>\r\n";
       //     sbGCFfile.append("</CloneClasses>\r\n");

        }
        // System.out.print("minimumLines:");
        
        sbGCFfile.append("</CloneClasses>\r\n");
        System.out.println(minimumlines);
        return sbGCFfile.toString();
    }
}
