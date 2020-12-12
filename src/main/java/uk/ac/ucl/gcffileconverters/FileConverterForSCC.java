package uk.ac.ucl.gcffileconverters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class FileConverterForSCC extends Fileconverters {
	private static Logger log;
	private HashMap<Integer, String[]> headersMap = new HashMap<>();
	
	@Override
	public String convert(File file, ArrayList<String> argslist) {
		// log setup
		log = Logger.getLogger(FileConverterForSCC.class);
		// set logging level
		log.setLevel(Level.DEBUG);

		/// variables
		String basepath = argslist.get(1).toString(); // get base path
		String headers1 = argslist.get(3).toString(); // get the 1st header file
		String headers2 = argslist.get(4).toString(); // get the 1st header file
		int startingIndex = 0;
		
		ArrayList<String[]> headersList1 = readHeadersFile(headers1, basepath);
		log.debug("header 1 size: " + headersList1.size());
		ArrayList<String[]> headersList2 = readHeadersFile(headers2, basepath);
		log.debug("header 2 size: " + headersList2.size());
		// get a starting index of the second file (higher block ids)
		startingIndex = Integer.parseInt(headersList2.get(0)[0]);
		log.debug("map headers size: " + headersMap.size());
		
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
				
				// add a clone pair.
				// 1st one
				// retrieve the file info
//				String[] file1 = headersList1.get(Integer.parseInt(clones[0]));

//				System.out.println("clone id: " + clones[0]);
//				System.out.println("clone file: " + headersMap.get(Integer.parseInt(clones[0]))[1]);
				String[] file1 = headersMap.get(Integer.parseInt(clones[0]));
				
				if (file1 == null) {
					System.out.println("couldn't find id: " + clones[0]);
				} else {

					sbfilecontent.append("\n<source file=\"");
					sbfilecontent.append(file1[1]);
					sbfilecontent.append("\" startline=\"");
					sbfilecontent.append(file1[2]);
					sbfilecontent.append("\" endline=\"");
					sbfilecontent.append(file1[3]);
					sbfilecontent.append("\" pcid=\"");
					sbfilecontent.append(cloneId);
					sbfilecontent.append("\">");
					sbfilecontent.append("</source>");
					cloneId++; // increase the clone id to uniquely identify the
								// clones

					// 2nd one
					// retrieve the file info by subtracting the index using the
					// starting index
					// String[] file2 =
					// headersList2.get(Integer.parseInt(clones[1]));
					String[] file2 = headersMap.get(Integer.parseInt(clones[1]));

					sbfilecontent.append("\n<source file=\"");
					sbfilecontent.append(file2[1]);
					sbfilecontent.append("\" startline=\"");
					sbfilecontent.append(file2[2]);
					sbfilecontent.append("\" endline=\"");
					sbfilecontent.append(file2[3]);
					sbfilecontent.append("\" pcid=\"");
					sbfilecontent.append(cloneId);
					sbfilecontent.append("\">");
					sbfilecontent.append("</source>");
					cloneId++; // increase the clone id to uniquely identify the
								// clones

					sbfilecontent.append("\n");
					sbfilecontent.append("</class>");

					// System.out.println("Done: " + cloneClassId);
					cloneClassId++;
				}
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
	
	private ArrayList<String[]> readHeadersFile(String headersfile, String basepath) {
		log.debug("reading headers file: " + headersfile);
		log.debug("base path: " + basepath);
		
		ArrayList<String[]> headers = new ArrayList<String[]>();
		LineNumberReader lr;
		String str = "";

		try {
			lr = new LineNumberReader(new FileReader(new File(headersfile)));
			while ((str = lr.readLine()) != null) {
				String[] file = str.split(",");
				file[1] = file[1].replace(basepath, "");
				headers.add(file);
				// add to map using its id as a key
				headersMap.put(Integer.parseInt(file[0]), file);
			}
			lr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return headers;
	}

}
