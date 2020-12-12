package uk.ac.ucl.gcffileconverters;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class FileConverterForCCAligner extends Fileconverters {
	private static Logger log;
	private HashMap<Integer, String[]> headersMap = new HashMap<>();
	
	@Override
	public String convert(File file, ArrayList<String> argslist) {
		String unwantedPath = argslist.get(1);
		// log setup
		log = Logger.getLogger(FileConverterForCCAligner.class);
		// set logging level
		log.setLevel(Level.DEBUG);
		int cloneClassId = 1; // starting clone class id. Each clone class has a different id
		int cloneId = 1; // starting clone id. Each clone has different id.
		String output = ""; // final output of the function
		StringBuffer sbfilecontent = new StringBuffer();
		int numOfLines = 0; // number of Lines in a clone class
		String str = "";
		sbfilecontent.append("<cloneDetectionResult>");

		try {
			// read each line from the clone file
			LineNumberReader lr = new LineNumberReader(new FileReader(file));

			while ((str = lr.readLine()) != null) {
				String[] clones = str.split(",");
				sbfilecontent.append("\n<class id=\"");
				sbfilecontent.append(cloneClassId);
				sbfilecontent.append("\" nlines=\"");
				sbfilecontent.append(numOfLines);
				sbfilecontent.append("\" nfragments=\"2\">");

				// 1st one
				sbfilecontent.append("\n<source file=\"");
				sbfilecontent.append(clones[0]);
				sbfilecontent.append("\" startline=\"");
				sbfilecontent.append(clones[1]);
				sbfilecontent.append("\" endline=\"");
				sbfilecontent.append(clones[2]);
				sbfilecontent.append("\" pcid=\"");
				sbfilecontent.append(cloneId);
				sbfilecontent.append("\">");
				sbfilecontent.append("</source>");
				cloneId++; // increase the clone id to uniquely identify the clones

				// 2nd one
				sbfilecontent.append("\n<source file=\"");
				sbfilecontent.append(clones[3]);
				sbfilecontent.append("\" startline=\"");
				sbfilecontent.append(clones[4]);
				sbfilecontent.append("\" endline=\"");
				sbfilecontent.append(clones[5]);
				sbfilecontent.append("\" pcid=\"");
				sbfilecontent.append(cloneId);
				sbfilecontent.append("\">");
				sbfilecontent.append("</source>");
				cloneId++; // increase the clone id to uniquely identify the clones

				// finish one clone class
				sbfilecontent.append("\n");
				sbfilecontent.append("</class>");

				cloneClassId++;
			}
			lr.close();

		} catch (FileNotFoundException ex) {
			log.error(ex.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

		sbfilecontent.append("\n</cloneDetectionResult>");
		output = sbfilecontent.toString();

		return output;
	}
	
	private ArrayList<String[]> readCloneFile(String headersfile, String basepath) {
		log.debug("reading clone file: " + headersfile);
		ArrayList<String[]> clonePairs = new ArrayList<String[]>();
		LineNumberReader lr;
		String str = "";

		try {
			lr = new LineNumberReader(new FileReader(new File(headersfile)));
			while ((str = lr.readLine()) != null) {
				String[] clonePair = str.split(",");
				clonePairs.add(clonePair);
			}
			lr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return clonePairs;
	}

}
