package uk.ac.ucl.gcffileconverters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;

import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author parvez
 * @author Chaiyong R.
 */
public class FileConverterForSimian extends Fileconverters {

	/*
	 * Reads a .txt output file from Simian and convert it to GCF format
	 */
	@Override
	public String convert(File file, ArrayList<String> list) {
		boolean found = false;
		int cloneClassId = 1; // starting clone class id. Each clone class has different id
		int cloneId = 1; // starting clone id. Each clone has different id.
		String output = ""; // final output of the function
		StringBuffer sbfilecontent = new StringBuffer();
		String cloneInfo = ""; // string that holds cloned fragments information for a class

		int numOfFragments = 0; // numf of fragments in a clone class
		int numOfLines = 0; // num of Lines in a clone class
		FileReader fr = null;
		String tool = "";
		String processingTime = "";
		String processedLines = "";
		String processedFiles = "";
		String duplicateLines = "";
		String duplicateFiles = "";
		String duplicateBlocks = "";
		try {
			LineNumberReader lr = new LineNumberReader(new FileReader(file));
			lr.skip(Long.MAX_VALUE);
			int totalLinesInFile = lr.getLineNumber();
			lr.close();
			lr = new LineNumberReader(new FileReader(file));
			tool = lr.readLine();
			for (int i = 1; i < totalLinesInFile - 3; i++) {
				lr.readLine();
			}
			String s = lr.readLine();
			String tokenizedStr[] = s.split("\\s+");
			duplicateLines = tokenizedStr[1];
			duplicateFiles = tokenizedStr[5];
			duplicateBlocks = tokenizedStr[8];
			tokenizedStr = lr.readLine().split("\\s+");
			processedFiles = tokenizedStr[tokenizedStr.length - 2];
			processedLines = tokenizedStr[4];
			tokenizedStr = lr.readLine().split("\\s+");
			processingTime = tokenizedStr[2];
			lr.close();
		} catch (FileNotFoundException ex) {
			Logger.getLogger(FileConverterForSimian.class.getName()).log(
					Level.SEVERE, null, ex);
		} catch (Exception e) {
			e.printStackTrace();
		}

		output = "";
		sbfilecontent.append("<cloneDetectionResult>");
		// output = output + info;
		try {
			fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String lineStr = null;
			try {
				String basepath = (String) list.get(0);
				int pos = basepath.length();
				while ((found == true) || (lineStr = br.readLine()) != null) {
					StringBuffer sbcloneInfo = new StringBuffer();
					// Starting of a clone class
					if ((found == true) || (lineStr.startsWith("Found") && lineStr.endsWith("files:"))) { 
						found = false; // reset found
						/*
						 * process each cloned fragment belong to a class
						 */
						while (true) {
							lineStr = br.readLine();
							/*
							 * A line starting with 'found' indicate the end of
							 * a clone class
							 */
							if (lineStr.startsWith("Found")) {
								if (lineStr.startsWith("Found") && lineStr.endsWith("files:")) {
									// found is set to true to bypass the next lineread in the while loop,
								    // posssibly we found another clone class
									found = true; 
								}
								break;
							}

							String splittedStr[] = lineStr.trim().split(" ");
							// split the line containing a cloned fragment info
							String filePath = "";
							for (int i = 6; i < splittedStr.length; i++) {
								filePath = filePath + " " + splittedStr[i];
							}
							sbcloneInfo.append("\n<source file=\"");
							sbcloneInfo.append(filePath.trim());
							sbcloneInfo.append("\"  startline=\"");
							sbcloneInfo.append(splittedStr[2]);
							sbcloneInfo.append("\"  endline=\"");
							sbcloneInfo.append(splittedStr[4]);
							sbcloneInfo.append("\"  pcid=\"");
							sbcloneInfo.append(cloneId);
							sbcloneInfo.append("\">");
							sbcloneInfo.append("</source>");

							numOfFragments++;
							numOfLines = numOfLines + (Integer.parseInt(splittedStr[4]) - Integer.parseInt(splittedStr[2]));
							// increase the clone id to uniquely identify the clones
							cloneId++; 
						}
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

						// Now reset the variables for the next clone class
						numOfFragments = 0;
						numOfLines = 0;
						cloneInfo = "";
						// increase clone class Id to uniquely identify each clone class
						cloneClassId++; 
					}
				}

				br.close();
				fr.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		} catch (FileNotFoundException ex) {
			Logger.getLogger(FileConverterForSimian.class.getName()).log(
					Level.SEVERE, null, ex);
		} finally {
			try {
				fr.close();
			} catch (IOException ex) {
				Logger.getLogger(FileConverterForSimian.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		}
		/*
		 * Finally encode the clone detection tool and other information
		 */
		sbfilecontent.append("\n</cloneDetectionResult>");
		output = sbfilecontent.toString();

		return output; // return the formatted result
	}
}