package uk.ac.ucl.fileconverters;

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

public class FileConverterForNicad extends Fileconverters {

	/*
	 * Accept an output file from NiCad clone detection tool and convert it
	 * to another format.
	 */
	public String convert(File file, ArrayList list) {
		String output = "";
		StringBuffer sbfilecontent = new StringBuffer();
		sbfilecontent.append("<cloneDetectionResult>\r\n");
		String str = "";
		String tokenizedStr[];
		
		try {
			LineNumberReader lr = new LineNumberReader(new FileReader(file));
			String basepath = (String) list.get(0);
			while ((str = lr.readLine()) != null) {

				if (str.trim().startsWith("<class classid=")) {
					tokenizedStr = str.split("\"");
					String id = tokenizedStr[1];
					String strnfragments = tokenizedStr[3];
					String nlines = tokenizedStr[5];
					String strclass = "<class id=\"";
					strclass += id;
					strclass += "\" nlines=\"";
					strclass += nlines;
					strclass += "\" nfragments=\"";
					strclass += strnfragments;
					strclass += "\">\r\n";
					
					sbfilecontent.append(strclass);

				} else if (str.trim().startsWith("<source file=")) {

					String sourceline[] = str.split("\"");
					String filepath = sourceline[1];

					String filesplit[] = filepath.split(basepath, 2);
					String remainfile = filesplit[1];
					int postfix = remainfile.indexOf(".ifdefed");
					
					if (postfix != -1) {
						remainfile = remainfile.substring(0, postfix);
					}
					
					sbfilecontent.append(sourceline[0]);
					sbfilecontent.append("\"");
					sbfilecontent.append(remainfile);

					int j = 2;
					while (j < sourceline.length) {
						sbfilecontent.append("\"");
						sbfilecontent.append(sourceline[j]);
						j++;
					}
					sbfilecontent.append("\r\n");
				} else if (str.trim().startsWith("</class>")) {
					sbfilecontent.append(str);
					sbfilecontent.append("\r\n");
				}
			}
			lr.close();
			
		} catch (FileNotFoundException ex) {
			Logger.getLogger(FileConverterForNicad.class.getName()).log(Level.SEVERE, null, ex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		sbfilecontent.append("</cloneDetectionResult>\r\n");
		output = sbfilecontent.toString();
		
		return output;
	}
}
