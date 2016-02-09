package fileconverters;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;

import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author parvez
 */
public class FileConverterForSimian extends Fileconverters {

	/*
	 * Accpet an output file from clone deetection tool Simian and convert it
	 * another form
	 */
	@Override
	public String convert(File file, ArrayList list) {
		boolean found = false;
		int cloneClassId = 1; // starting clone class id. Each clone class has
								// different id
		int cloneId = 1; // starting clone id. Each clone has different id.
		String output = ""; // final output of the function
		StringBuffer sbfilecontent = new StringBuffer();//
		String cloneInfo = ""; // string that holds cloned fragments information
								// for a class

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
			// System.out.print(tool);
			for (int i = 1; i < totalLinesInFile - 3; i++) {
				lr.readLine();
			}
			String s = lr.readLine();
			String tokenizedStr[] = s.split("\\s+");
			// System.out.println("??"+s+"TotalLinesInFIles = "+totalLinesInFile);

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

		// output=;
		String info = "\n<info " + " tool=\"" + tool + "\""
				+ "    processedLines=\"" + processedLines + "\""
				+ "    processedFiles=\"" + processedFiles + "\""
				+ "    duplicatedLines=\"" + duplicateLines + "\""
				+ "  duplicatedFiles=\"" + duplicateFiles + "\""
				+ "  duplicatedBlocks=\"" + duplicateBlocks + "\""
				+ "   processingTime=\"" + processingTime + "\"" + " />";
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
					/*
					 * if(found == true) System.out.println("found == true");
					 * if(lineStr != null) { System.out.println(lineStr);}
					 * System.out.println("--------------------------");
					 */
					StringBuffer sbcloneInfo = new StringBuffer();//
					if ((found == true)
							|| (lineStr.startsWith("Found") && lineStr
									.endsWith("files:"))) { // Starting of a
															// clone class
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
								if (lineStr.startsWith("Found")
										&& lineStr.endsWith("files:")) {
									found = true; // found is set to true to
													// bypass the next lineread
													// in the while loop,
													// posssibly we found
													// another clone class
								}

								break;
							}

							String splittedStr[] = lineStr.trim().split(" ");
							// split the line containing a cloned fragment info
							String filePath = "";
							for (int i = 6; i < splittedStr.length; i++) {
								filePath = filePath + " " + splittedStr[i];
							}
							
							// System.out.println(filePath.trim());
							// String strfilePath = filePath.trim().substring(pos);

							sbcloneInfo.append("\n<source file=\"");
							// sbcloneInfo.append(strfilePath);
							sbcloneInfo.append(filePath.trim());
							sbcloneInfo.append("\"  startline=\"");
							sbcloneInfo.append(splittedStr[2]);
							sbcloneInfo.append("\"  endline=\"");
							sbcloneInfo.append(splittedStr[4]);
							sbcloneInfo.append("\"  pcid=\"");
							sbcloneInfo.append(cloneId);
							sbcloneInfo.append("\">");
							sbcloneInfo.append("</source>");

							/*
							 * cloneInfo = cloneInfo + "\n<source file=\"" +
							 * strfilePath + "\"  startline=\"" + splittedStr[2]
							 * + "\"  endline=\"" + splittedStr[4] +
							 * "\"  pcid=\"" + cloneId + "\">" + "</source>";
							 */

							// cloneInfo = cloneInfo + "</source>";
							numOfFragments++;
							numOfLines = numOfLines
									+ (Integer.parseInt(splittedStr[4]) - Integer
											.parseInt(splittedStr[2]));

							// System.out.println(splittedStr[2]+"----"+splittedStr[4]
							// );

							cloneId++; // increase the clone id to uniquely
										// identify the clones
						}
						cloneInfo = sbcloneInfo.toString();
						// System.out.println(cloneInfo );

						// output = output + "\n<class id=\"" + cloneClassId +
						// "\" nlines=\"" + numOfLines
						// + "\" nfragments=\"" + numOfFragments + "\">" +
						// cloneInfo +"\n" + "</class>";
						/*
						 * String strclass = "\n<class id=\"" + cloneClassId +
						 * "\" nlines=\"" + numOfLines + "\" nfragments=\"" +
						 * numOfFragments + "\">" + cloneInfo + "\n" +
						 * "</class>";
						 */
						// sbfilecontent.append(strclass);
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
						cloneClassId++; // increase clone class Id to uniquely
										// identify each clone class
					}
				}

				br.close();
				fr.close();
				// System.out.println(output );
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

			/*
			 * Finally encode the clone detection tool and other information
			 */
			sbfilecontent.append("\n</cloneDetectionResult>");
			output = sbfilecontent.toString();
			// output = "<cloneDetectionResult>" + output +
			// "\n</cloneDetectionResult>";

			return output; // return the formatted result
		}
	}
}

// ~ Formatted by Jindent --- http://www.jindent.com
