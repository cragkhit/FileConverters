package fileconverters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * @author Chaiyong R.
 * @date 8 May 2015
 * The converter takes as input the Deckard's clone cluster result file
 */
public class FileConverterForDeckard extends Fileconverters {
	private static Logger log;
	
    public String convert(File file, ArrayList argslist) {
    		///// log setup
    		log = Logger.getLogger(FileConverterForNicad.class);
		// set logging level
		log.setLevel(Level.DEBUG);
		// use basic configuration
		BasicConfigurator.configure();
		
		/// variables
		boolean found = false;
		String basepath = argslist.get(2).toString(); // get base path
		int cloneClassId = 1; // starting clone class id. Each clone class has a different id
		int cloneId = 1; // starting clone id. Each clone has different id.
		String output = ""; // final output of the function
		StringBuffer sbfilecontent = new StringBuffer();//
		String cloneInfo = ""; // string that holds cloned fragments information for a class
		int numOfFragments = 0; // number of fragments in a clone class
		int numOfLines = 0; // number of Lines in a clone class
		String str = "";
		sbfilecontent.append("<cloneDetectionResult>");
		
		try {
			// read each line from the clone cluster file
			LineNumberReader lr = new LineNumberReader(new FileReader(file));
			StringBuffer sbcloneInfo = new StringBuffer();
			
			while ((str = lr.readLine()) != null) {
				if (str.contains("FILE")) { // blank line
					// start cutting from FILE to LINE --> get file name
					String filePath = str.substring(str.indexOf("FILE ")+5, str.indexOf("LINE")-1).replace(basepath, "");
					String line = str.substring(str.indexOf("LINE")+5, str.indexOf("NODE_KIND")-1);
					String[] lineSplitted = line.split(":");
					//System.out.println(str.indexOf("LINE") + ":" + str.indexOf("NODE_KIND"));
					//System.out.println("Start: " + lineSplitted[1] + "| End:" + lineSplitted[0]);
					// System.out.println("file:|" + str.substring(str.indexOf("FILE ")+5, str.indexOf("LINE")-1).replace(basepath, "") + "|");
					// log.debug("file:|" + str.substring(str.indexOf("FILE ")+5, 
					// 		str.indexOf("LINE")-1).replace(basepath, "") + "|");
					sbcloneInfo.append("\n<source file=\"");
					// sbcloneInfo.append(strfilePath);
					sbcloneInfo.append(filePath.trim());
					sbcloneInfo.append("\" startline=\"");
					sbcloneInfo.append(lineSplitted[1]);
					sbcloneInfo.append("\" endline=\"");
					sbcloneInfo.append(lineSplitted[0]);
					sbcloneInfo.append("\" pcid=\"");
					sbcloneInfo.append(cloneId);
					sbcloneInfo.append("\">");
					sbcloneInfo.append("</source>");
					
					numOfFragments++;
					numOfLines = numOfLines + 
							(Integer.parseInt(lineSplitted[0])-Integer.parseInt(lineSplitted[1]));
	
					cloneId++; // increase the clone id to uniquely identify the clones
				}
				else {
					// if found blank, add a class
					// lr.readLine(); // skip another blank line
					// start adding the class
					cloneInfo = sbcloneInfo.toString();
					sbfilecontent.append("\n<class id=\"");
					sbfilecontent.append(cloneClassId);
					sbfilecontent.append("\" nlines=\"");
					sbfilecontent.append(numOfLines);
					sbfilecontent.append("\" nfragments=\"");
					sbfilecontent.append(numOfFragments);
					sbfilecontent.append("\">");
					sbfilecontent.append(cloneInfo);
					sbfilecontent.append("\n");
					sbfilecontent.append("</class>");
					
					// increase class id by one
					cloneClassId++;
					sbcloneInfo = new StringBuffer();
					numOfFragments = 0;
					numOfLines = 0;
				}
			}
			
		} catch (FileNotFoundException ex) {
			log.error(ex.getMessage());
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		
		sbfilecontent.append("\n</cloneDetectionResult>");
		output = sbfilecontent.toString();
		System.out.println(output);
		
		return output;
    }
}
