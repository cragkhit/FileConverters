package fileconverters;

import fileconverters.IConstant;
import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This class is modified further to match with needs of CloPlag experiment by
 * Chaiyong R.
 *
 * @author Tian Tian Wang
 * @author Chaiyong R.
 */
public class Main {
	private static Logger log;
	private static String cloneFile = "";
	public static String prefix = ""; 
	private static String mode = "";
	private static String minLine = "6";

	public static void main(String args[]) {
		// initialize log4j logger
		log = Logger.getLogger(Main.class);
		log.setLevel(Level.DEBUG);
		BasicConfigurator.configure();

		if (args.length < 3) {
			log.debug("GCF File Converter v.0.5\n");
			log.debug("Usage: java -jar gcfFileConverter.jar <mode> <prefix path> <clone file>\n");
			log.debug("   <mode>: 1=ccfx, 2=simscan, 3=CPD, 4=ConQAT, 5=iClones, "
					+ "6=simian, 7=nicad, 8=deckard, 9=scc");
			log.debug("   <prefix_path>: the unwanted prefix path that you want to remove from the output file.");
			log.debug("   <clone_file>: the original clone file.");
			log.debug("   [min_line]: minimum number of clone line.\n");
			log.debug("Example: java -jar gcfFileConverter.jar ccfx /unwanted/path ccfx.txt 6");
			log.debug("Example: java -jar gcfFileConverter.jar 6 /unwanted/path simian.txt");
			log.debug("Example: java -jar gcfFileConverter.jar nicad /unwanted/path nicad.xml");
			System.exit(-1);
		} else {
			mode = args[0];
			prefix = args[1];
			cloneFile = args[2];
//			log.debug(cloneFile);
			
			if (args.length == 4)
				minLine = args[3];
			else
				minLine = "6";
		
			// Option 1 - CCFinder
			if (mode.trim().matches("1") || mode.trim().matches("ccfx")) {
				printHeader("ccfx");
				if (args.length >= 5)
					minLine = args[4];
				processCCFinder(args);
			} else if (mode.trim().matches("2") || mode.trim().matches("simscan")) {
				// Option 2 - SimScan
				printHeader("simscan");
				processSimScan(args);
			} else if (mode.trim().matches("3") || mode.trim().matches("cpd")) {
				// Option 3 - CPD
				printHeader("cpd");
				processCPD(args);
			} else if (mode.trim().matches("4") || mode.trim().matches("conqat")) {
				// Option 4 - ConQAT
				printHeader("conqat");
				processConQAT(args);
			} else if (mode.trim().matches("5") || mode.trim().matches("iclones")) {
				// Option 5 - RCF
				printHeader("iclones");
				processRCF(args);
			} else if (mode.trim().matches("6") || mode.trim().matches("simian")) {
				// Option 6 - Simian
				printHeader("simian");
				// process Simian output
				processSimian(args);
			} else if (mode.trim().matches("7") || mode.trim().matches("nicad")) {
				// Option 7 - NiCad
				printHeader("NiCad");
				// process NiCad output
				processNiCad(args);
			} else if (mode.trim().matches("8") || mode.trim().matches("deckard")) {
				// Option 8 - Deckard
				printHeader("Deckard");
				processDeckard(args);
			} else if (mode.trim().matches("9") || mode.trim().matches("scc")) {
				// Option 9 - SourcererCC
				printHeader("SourcererCC");
				processSCC(args);
			} else {
				log.error("Incorrect tool specified: " + mode.trim() + ". Please check again.");
				System.exit(-1);
			}
		}
		log.debug("Done ...");
	}
	
	public static void printHeader(String toolName) {
		log.debug("GCFFileConverter (v 0.4) ...");
		log.debug("Converting " + toolName + " clone report into GCF format...");
	}

	public static void saveConvertedFile(String filename, String filecontent) {
		log.debug("Saved as : " + filename);
		java.io.File f = new java.io.File(filename);
		java.io.FileWriter out = null;
		try {
			out = new java.io.FileWriter(f);
			out.write(filecontent);
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
			}
		}
	}
	
	public static void processCPD(String[] args) {
		if (args.length < 3) {
			log.debug("Please input the right paramenters:");
			log.debug("Usage: $java -jar gcfFileConverter.jar 3 E:/UCL/bellon/cook PMDCPD-cook.txt");
			System.exit(-1);
		}
		ArrayList<String> list = new ArrayList<String>();

		list.add(prefix.trim()); // base path
		list.add(cloneFile.trim()); // filename
		String filename[] = cloneFile.trim().split("\\.");

		if (args.length == 4) {
			list.add(args[3].trim());
		}

		String output = new FileConverterFactory().createFileConverter(
				IConstant.CPD_RESULT_FILE).convert(
				new File("CPD-postgresql.txt"), list);

		String gcffilename = filename[0] + "-GCF";
		saveConvertedFile(gcffilename + ".xml", output);
		log.debug("Creating GCF file at " + gcffilename + ".xml");
	}
	
	public static void processSimScan(String[] args) {
		if (args.length < 3) {
			log.debug("Please input the right paramenters");
			log.debug("$java -jar gcfFileConverter.jar 2 /Fileconverters/simscan_eclipse-ant eclipse-ant 6");
			System.exit(-1);
		}
		if (args.length == 4)
			minLine = args[3];
		else
			minLine = "6";

		ArrayList<String> list = new ArrayList<String>();

		String output = new FileConverterFactory().createFileConverter(
				IConstant.SIMSCAN_RESULT_FILE).convert(
				new File(cloneFile.trim()), list); 
		String filename = cloneFile.trim();

		saveConvertedFile(filename + "temp.xml", output);
		ArrayList<String> strlist = new ArrayList<String>();
		strlist = readConvertedFile(filename + "temp.xml");
		String GCFfile = "";
		if (args.length < 4) {
			GCFfile = converteToGCF(strlist);
		} else {
			int minLineInt = Integer.parseInt(minLine);
			GCFfile = converteToGCFmin(strlist, minLineInt);
		}
		filename += "-GCF";
		saveConvertedFile(filename + ".xml", GCFfile);
		log.debug("Creating GCF file at " + filename + ".xml");

		// remove the temp file
		File tmpfile = new File(filename + "temp.xml");
		if (tmpfile.delete()) {
			log.debug(tmpfile.getName() + " is deleted!");
		} else {
			log.error("Delete operation is failed.");
		}
	}

	private static void processDeckard(String[] args) {
		if (args.length < 3) {
			log.debug("Please input the right paramenters");
			log.debug("Usage: java -jar gcf_Fileconverter.jar [8,deckard] "
					+ "<Deckard's cluster file location> <basepath>");
			log.debug("Example: java -jar gcf_Fileconverter.jar deckard "
					+ " /home/andy/deckard_clones/ /home/post_cluster_vdb_30_0_allg_0.95_30");
			System.exit(-1);
		}
		if (args.length == 4)
			minLine = args[3];
		else
			minLine = "6";
		
		// create an array list to store all clone fragments
		ArrayList<String> argList = new ArrayList<String>();
		for (int i=0; i<args.length; i++)
			argList.add(args[i]); 
		
		String output = new FileConverterFactory().createFileConverter(IConstant.DECKARD_RESULT_FILE)
				.convert(new File(cloneFile.trim()), argList);
		String filename = cloneFile.trim();
		
		String tmpfileName = filename + "_temp.xml";
		saveConvertedFile(tmpfileName, output);
		ArrayList<String> strlist = new ArrayList<String>();
		strlist = readConvertedFile(tmpfileName);
		String GCFfile = "";
		
		if (args.length < 4) {
			GCFfile = converteToGCF(strlist);
		} else {
			int minlineInt = Integer.parseInt(minLine);
			GCFfile = converteToGCFmin(strlist, minlineInt);
		}
		filename += "-GCF";
		saveConvertedFile(filename + ".xml", GCFfile);
		
		// remove the temp file
		File tmpfile = new File(tmpfileName);

		if (tmpfile.delete()) {
			System.out.println(tmpfile.getName() + " is deleted!");
		} else {
			System.out.println("Delete operation is failed.");
		}
		log.debug("Creating GCF file at " + filename + ".xml");
	}
	
	public static void processCCFinder(String[] args) {
		ArrayList<String> list = new ArrayList<String>();
		
		if (args.length < 6) {
			log.debug("Please Input the right paramenters");
			log.debug("ccfx /path/to/remove/1 ccfx_clones.txt /location/of/ccfxprepdir minline <create clone class (true/false)>");
			System.exit(-1);
		} else {
			list.add(args[0]);
			list.add(args[1]);
			list.add(args[2]);
			list.add(args[3]);
			list.add(args[4]);
			list.add(args[5]);
		}

		String output = new FileConverterFactory().createFileConverter(
				IConstant.CCFINDER_RESULT_FILE).convert(new File(cloneFile), list);

		String filename[] = cloneFile.trim().split("\\.");
		saveConvertedFile(filename[0] + "temp.xml", output);
		
		ArrayList<String> strlist = new ArrayList<String>();

		strlist = readConvertedFile(filename[0] + "temp.xml");
		String GCFfile = "";
		
		// check if the MinLine is provided or not
		if (args.length < 5) {
			GCFfile = converteToGCF(strlist);
			String gcffilename = filename[0] + "-GCF";
			saveConvertedFile(gcffilename + ".xml", GCFfile);
			log.debug("Creating GCF file at " + gcffilename + ".xml");
		} else if (args.length == 6) {
			int minlineInt = Integer.parseInt(minLine);
			GCFfile = converteToGCFmin(strlist, minlineInt);
			String gcffilename = filename[0] + "-GCF";
			saveConvertedFile(gcffilename + ".xml", GCFfile);
			log.debug("Creating GCF file at " + gcffilename + ".xml");
		} else if (args.length == 5 && args[4].equals("-new")) {
			String gcffilename = filename[0] + "-GCF.xml";
			writeXML(converteToGcfDomMin(strlist), gcffilename);
		}
		
		// remove the temp file
		File tmpfile = new File(filename[0] + "temp.xml");
		if (tmpfile.delete()) {
			log.debug(tmpfile.getName() + " is deleted!");
		} else {
			log.error("Delete operation is failed.");
		}
	}

	public static void processConQAT(String[] args) {
		ArrayList<String> list = new ArrayList<String>();

		if (args.length < 4) {
			log.debug("Please input the right paramenters");
			log.debug("Usage: $java -jar gcfFileConverter.jar 4 /cook/ ConQAT-cook.xml 6");
			System.exit(-1);
		}
		list.add(args[1].trim()); // base path
		list.add(cloneFile.trim()); // filename
		list.add(args[3].trim()); // minimum lines
		String filename[] = cloneFile.trim().split("\\.");

		String output = new FileConverterFactory().createFileConverter(
				IConstant.CONQAT_RESULT_FILE).convert(new File(filename[0]),
				list); 
		String gcffilename = filename[0] + "-GCF";
		saveConvertedFile(gcffilename + ".xml", output);
		log.debug("Creating GCF file at " + gcffilename + ".xml");
	}

	// iClones
	public static void processRCF(String[] args) {
		if (args.length < 3) {
			log.debug("Please input the correct parameters:");
			log.debug("$ java -jar GCF_Fileconverters.jar 5 /unwanted/path iclones_result.rcf");
			System.exit(-1);
		}

		ArrayList<String> list = new ArrayList<String>();
		list.add(cloneFile.trim());

		/* if (args.length == 3) {
			list.add(args[2].trim());
		} */

		String output = new FileConverterFactory().createFileConverter(
				IConstant.RCF_RESULT_FILE).convert(new File("IClone-cook.txt"),
				list);
		String filename[] = cloneFile.trim().split("\\.");
		String gcffilename = filename[0] + "-GCF";
		saveConvertedFile(gcffilename + ".xml", output);
		log.debug("Creating GCF file at " + gcffilename + ".xml");

		// remove the temp file
		File tmpfile = new File(filename[0] + "temp.xml");
		if (tmpfile.delete()) {
			log.debug(tmpfile.getName() + " is deleted!");
		} else {
			log.error("Delete operation is failed.");
		}
	}

	public static void processSimian(String[] args) {
		log.debug("Simian");
		if (args.length < 3) {
			log.debug("Please Input the right paramenters:");
			log.debug("Usage: $java -jar GCF_Fileconverters.jar 6 E:/UCL/2simian/simian-2.3.33/bin/cook simian-cook-6-ignorevariablename.txt");
			System.exit(-1);
		}

		ArrayList<String> list = new ArrayList<String>();
		list.add(args[1].trim()); // base path
		String output = new FileConverterFactory().createFileConverter(
				IConstant.SIIMIAN_RESULT_FILE).convert(
				new File(cloneFile.trim()), list);
	
		// set prefix value
		prefix = args[1];
		String filename[] = cloneFile.trim().split("\\."); // filename
		saveConvertedFile(filename[0] + "temp.xml", output);
		log.debug("Filename = " + filename[0]);
		ArrayList<String> strlist = new ArrayList<String>();
		strlist = readConvertedFile(filename[0] + "temp.xml");

		String GCFfile = "";
		if (args.length < 4) {
			GCFfile = converteToGCF(strlist);
		} else {
			int minLineInt = Integer.parseInt(minLine);
			GCFfile = converteToGCFmin(strlist, minLineInt);
		}
		String gcffilename = filename[0] + "-GCF";
		saveConvertedFile(gcffilename + ".xml", GCFfile);
		log.debug("Creating GCF file at " + gcffilename + ".xml");
		
		// remove the temp file
		File tmpfile = new File(filename[0] + "temp.xml");
		if (tmpfile.delete()) {
			log.debug(tmpfile.getName() + " is deleted!");
		} else {
			log.error("Delete operation is failed.");
		}
	}

	public static void processNiCad(String[] args) {
		if (args.length < 3) {
			log.debug("Please input parameters correclty:");
			log.debug("Usage: java -jar GCF_Fileconverters.jar 7 "
					+ "<subject program name> <NiCad result file>.xml");
			System.exit(-1);
		}

		ArrayList<String> list = new ArrayList<String>();
		list.add(args[1].trim()); // base path
		log.debug("NiCad file = " + cloneFile.trim());
		
		 String output = new FileConverterFactory().
				 createFileConverter(IConstant.NICAD_RESULT_FILE).
				 convert(new File(cloneFile.trim()), list);
		 
		// cut out the extension
		String filename[] = cloneFile.trim().split(".xml");
		log.debug("Converted file: " + filename[0] + "temp.xml");
		saveConvertedFile(filename[0] + "temp.xml", output);

		ArrayList<String> strlist = new ArrayList<String>();
		// read each line into an array list: strlist
		strlist = readConvertedFile(filename[0] + "temp.xml");
		// log.debug("readConvertedFile");
		// log.debug(strlist.size());

		// check mode of conversion
		String GCFfile = "";
		if (args.length == 3) {
			GCFfile = converteToGCF(strlist);
		} else {
			int minilines = Integer.parseInt(args[3]);
			GCFfile = converteToGCFmin(strlist, minilines);
		}

		// System.out.println("converteToGCF");
		String gcffilename = filename[0] + "-GCF";
		saveConvertedFile(gcffilename + ".xml", GCFfile);
		log.debug("Creating GCF file at " + gcffilename + ".xml");
		
		// remove the temp file
		File tmpfile = new File(filename[0] + "temp.xml");
		if (tmpfile.delete()) {
			log.debug(tmpfile.getName() + " is deleted!");
		} else {
			log.error("Delete operation is failed.");
		}
	}

	public static ArrayList<String> readConvertedFile(String fileName) {
		java.io.BufferedReader infile = null;
		// Get file name from the text field
		String inLine;
		ArrayList<String> lines = new ArrayList<>();

		try {
			// Create a buffered stream
			infile = new java.io.BufferedReader(new java.io.FileReader(fileName));
			// Read a line
			inLine = infile.readLine();
			while (inLine != null) {
				lines.add(inLine);
				inLine = infile.readLine();
			}
		} catch (java.io.FileNotFoundException ex) {
			log.error("File not found: " + fileName);
		} catch (java.io.IOException ex) {
			log.error(ex.getMessage());
		} finally {
			try {
				if (infile != null) infile.close();
			} catch (java.io.IOException ex) { 
				log.error(ex.getMessage()); 
			}
		}
		
		return lines;
	}

	public static String converteToGCF(ArrayList<String> strlist) {
		log.debug("in converteToGCF");
		StringBuffer sbGCFfile = new StringBuffer();
		sbGCFfile.append("<CloneClasses>\r\n");

		int classIndex = 0;
		int endclassIndex = 0;
		boolean findclass = false;
		boolean endclass = false;
		int minimumLines = 0;
		while (classIndex < strlist.size()) {
			// System.out.println("classIndex=" + classIndex + ": " + strlist.get(classIndex));
			if (strlist.get(classIndex).startsWith("<class")) {
				String classInfo = strlist.get(classIndex);
				endclassIndex = classIndex + 1;
				endclass = false;

				while (endclassIndex < strlist.size() && !endclass) {
					if (strlist.get(endclassIndex).startsWith("</class>")) {
						endclass = true;
						findclass = true;
						int i = 0;
						int nfragment = 0;
						String strid = "";
						
						while (i < classInfo.length()) {
							if (classInfo.regionMatches(i, "id=", 0, 3)) {
								int start = i + 4;
								int j = start + 1;
								boolean endid = false;
								// finding id
								while (j < classInfo.length() && !endid) {
									if (classInfo.regionMatches(j, "\"", 0, 1)) {
										endid = true;
										i = j;
										strid = classInfo.substring(start, j); 
									} else {
										j++;
									}

								}
							}
							
							if (classInfo.regionMatches(i, "nfragments=", 0, 11)) {
								int startfrag = i + 12;
								int k = startfrag + 1;
								boolean endfrag = false;
								String strfrag = "";
								while (k < classInfo.length() && !endfrag) {

									if (classInfo.regionMatches(k, "\"", 0, 1)) {
										endfrag = true;
										i = k;
										strfrag = classInfo.substring(
												startfrag, k); // nfragments

										nfragment = Integer.parseInt(strfrag);
									} else {
										k++;
									}
								}

							}
							i++;
						}
						
						String tab = "    ";
						sbGCFfile.append(tab + "<CloneClass>\r\n");
						sbGCFfile.append(tab + "<ID>");
						sbGCFfile.append(strid);
						sbGCFfile.append("</ID>\r\n");
						
						int numf = nfragment;
						int strindex = classIndex + 1;
						while (numf > 0 && strindex < endclassIndex
								&& strindex < strlist.size()) {
							String strfragment = strlist.get(strindex);
							String startline = "";
							String endline = "";
							String filepath = "";
							if (strfragment.startsWith("<source file=")) {
								int findex = 0;
								while (findex < strfragment.length()) {
									if (strfragment.regionMatches(findex,"file=", 0, 5)) {
										int startf = findex + 6;
										int endf = startf + 1;
										boolean bendf = false;
										while (endf < strfragment.length() && !bendf) {
											if (strfragment.regionMatches(endf,"\"", 0, 1)) {
												bendf = true;
												filepath = strfragment.substring(startf, endf);
												findex = endf;
											} else {
												endf++;
											}
										}
									}
									if (strfragment.regionMatches(findex, "startline=", 0, 10)) {
										int startf = findex + 11;
										int endf = startf + 1;
										boolean bendf = false;
										while (endf < strfragment.length() && !bendf) {
											if (strfragment.regionMatches(endf, "\"", 0, 1)) {
												bendf = true;
												startline = strfragment.substring(startf, endf);
												findex = endf;
											} else {
												endf++;
											}
										}
									}
									if (strfragment.regionMatches(findex, "endline=", 0, 8)) {
										int startf = findex + 9;
										int endf = startf + 1;
										boolean bendf = false;
										while (endf < strfragment.length() && !bendf) {
											if (strfragment.regionMatches(endf, "\"", 0, 1)) {
												bendf = true;
												endline = strfragment.substring(startf, endf);
												findex = endf;
											} else {
												endf++;
											}
										}
									}
									findex++;
								}
							}
							sbGCFfile.append(tab + tab + "<Clone>\r\n");
							sbGCFfile.append(tab + tab + tab + "<Fragment>\r\n");
							sbGCFfile.append(tab + tab + tab + tab + "<File>");
							// remove the unwanted prefix
							sbGCFfile.append(filepath.replace(prefix, ""));
							sbGCFfile.append("</File>\r\n");
							sbGCFfile.append(tab + tab + tab + tab + "<Start>");
							sbGCFfile.append(startline);
							sbGCFfile.append("</Start>\r\n");
							sbGCFfile.append(tab + tab + tab + tab + "<End>");
							sbGCFfile.append(endline);
							sbGCFfile.append("</End>\r\n");
							sbGCFfile.append(tab + tab + tab + "</Fragment>\r\n");
							sbGCFfile.append(tab + tab + "</Clone>\r\n");
							
							int lines = Integer.parseInt(endline) - Integer.parseInt(startline) + 1;
							if (minimumLines == 0 || lines < minimumLines) {
								minimumLines = lines;
							}
							strindex++;
							numf--;
						} 

						if (findclass && endclass) {
							sbGCFfile.append(tab + "</CloneClass>\r\n");
						}
						classIndex = endclassIndex;
					} 
					else {
						endclassIndex++;
					}
				}
			}
			classIndex++;
		}
		sbGCFfile.append("</CloneClasses>\r\n");

		return sbGCFfile.toString();
	}

	public static String converteToGCFmin(ArrayList<String> convertedFileLines, int minCloneLine) {
		log.debug("No. of lines: " + convertedFileLines.size());
		StringBuffer sbGCFfile = new StringBuffer();
		sbGCFfile.append("<CloneClasses>\r\n");
		int lineCount = 0;
		int endclassIndex = 0;
		boolean foundAClass = false;
		boolean reachEndClass = false;
		int minimumLines = 0;
		
		while (lineCount < convertedFileLines.size()) {
			// get the head of each clone class
			if (convertedFileLines.get(lineCount).startsWith("<class")) {
				String classInfo = convertedFileLines.get(lineCount);
				endclassIndex = lineCount + 1;
				reachEndClass = false;

				while (endclassIndex < convertedFileLines.size() && !reachEndClass) {
					if (convertedFileLines.get(endclassIndex).startsWith("</class>")) {
						reachEndClass = true;
						foundAClass = true;
						int i = 0;
						int nfragment = 0;
						String strid = "";

						while (i < classInfo.length()) {
							if (classInfo.regionMatches(i, "id=", 0, 3)) {
								int start = i + 4;
								int j = start + 1;
								boolean endid = false;

								while (j < classInfo.length() && !endid) {
									if (classInfo.regionMatches(j, "\"", 0, 1)) {
										endid = true;
										i = j;
										strid = classInfo.substring(start, j); 
									} else 
										j++;
								}
							}
							
							if (classInfo.regionMatches(i, "nfragments=", 0, 11)) {
								int startfrag = i + 12;
								int k = startfrag + 1;
								boolean endfrag = false;
								String strfrag = "";
								while (k < classInfo.length() && !endfrag) {
									if (classInfo.regionMatches(k, "\"", 0, 1)) {
										endfrag = true;
										i = k;
										strfrag = classInfo.substring(startfrag, k);
										nfragment = Integer.parseInt(strfrag);

									} else
										k++;
								}
							}
							i++;
						}
						
						String tab = "    ";
						StringBuffer sb = new StringBuffer();
						sb.append("<CloneClass>\r\n");
						sb.append(tab + "<ID>");
						sb.append(strid);
						sb.append("</ID>\r\n");

						int fragmentcountofclass = 0;
						int numf = nfragment;
						int strindex = lineCount + 1;
						
						while (numf > 0 
								&& strindex < endclassIndex
								&& strindex < convertedFileLines.size()) {
							String fragmentLine = convertedFileLines.get(strindex);
							String startline = "";
							String endline = "";
							String filepath = "";

							if (fragmentLine.startsWith("<source file=")) {
								int fileIndex = 0;
								while (fileIndex < fragmentLine.length()) {
									if (fragmentLine.regionMatches(fileIndex, "file=", 0, 5)) {
										int startf = fileIndex + 6;
										int endf = startf + 1;
										boolean bendf = false;
										while (endf < fragmentLine.length() && !bendf) {
											if (fragmentLine.regionMatches(endf, "\"", 0, 1)) {
												bendf = true;

												filepath = fragmentLine.substring(startf, endf);
												fileIndex = endf;
											} else {
												endf++;
											}
										}
									}
									
									// Start line
									if (fragmentLine.regionMatches(fileIndex,"startline=", 0, 10)) {
										int startf = fileIndex + 11;
										int endf = startf + 1;
										boolean bendf = false;
										while (endf < fragmentLine.length() && !bendf) {
											if (fragmentLine.regionMatches(endf, "\"", 0, 1)) {
												bendf = true;
												startline = fragmentLine.substring(startf, endf);
												fileIndex = endf;
											} else {
												endf++;
											}
										}
									}
									
									// End line
									if (fragmentLine.regionMatches(fileIndex, "endline=", 0, 8)) {
										int startf = fileIndex + 9;
										int endf = startf + 1;
										boolean bendf = false;
										while (endf < fragmentLine.length() && !bendf) {
											if (fragmentLine.regionMatches(endf, "\"", 0, 1)) {
												bendf = true;
												endline = fragmentLine.substring(startf, endf);
												fileIndex = endf;
											} else {
												endf++;
											}
										}
									}
									fileIndex++;
								}
							}

							int lines = Integer.parseInt(endline) - Integer.parseInt(startline) + 1;
							
							// Only add clone to the result if the size is larger than minimum line
							if (lines >= minCloneLine) {
								fragmentcountofclass++;
								if (minimumLines == 0 || lines < minimumLines) {
									minimumLines = lines;
								}
								sb.append(tab + tab + "<Clone>\r\n");
								sb.append(tab + tab + tab + "<Fragment>\r\n");
								sb.append(tab + tab + tab + tab + "<File>");
								// remove the give unwanted prefix
								sb.append(filepath.replace(prefix, ""));
								sb.append("</File>\r\n");
								sb.append(tab + tab + tab + tab + "<Start>");
								sb.append(startline);
								sb.append("</Start>\r\n");
								sb.append(tab + tab + tab + tab + "<End>");
								sb.append(endline);
								sb.append("</End>\r\n");
								sb.append(tab + tab + tab + "</Fragment>\r\n");
								sb.append(tab + tab + "</Clone>\r\n");
							} 
							// TODO: only used when debugging
//							else {
//								log.debug(lines + " vs. " + minCloneLine);
//								log.debug(filepath.replace(prefix, "") + " (" + startline + "," + endline + ")");
//							}
							strindex++;
							numf--;
						} 
						
						if (foundAClass && reachEndClass && fragmentcountofclass >= 2) {
							sbGCFfile.append(sb.toString());
							sbGCFfile.append("</CloneClass>\r\n");
						}
						lineCount = endclassIndex;
					} 
					else { endclassIndex++; }
				}
			}
			lineCount++;
		}
		sbGCFfile.append("</CloneClasses>\r\n");

		return sbGCFfile.toString();
	}
	
	public static Document converteToGcfDomMin(ArrayList<String> strlist) {
		// root elements
		Document doc = null;
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("cloneclasses");
			doc.appendChild(rootElement);

			// StringBuffer sbGCFfile = new StringBuffer();
			// sbGCFfile.append("<cloneclasses>\r\n");
			int classIndex = 0;
			int endclassIndex = 0;
			boolean findclass = false;
			boolean endclass = false;
			int minimumLines = 0;
			while (classIndex < strlist.size()) {
				if (strlist.get(classIndex).startsWith("<class")) {
					String classInfo = strlist.get(classIndex);
					endclassIndex = classIndex + 1;
					endclass = false;
					while (endclassIndex < strlist.size() && !endclass) {
						if (strlist.get(endclassIndex).startsWith("</class>")) {
							endclass = true;
							findclass = true;
							int i = 0, nfragment = 0;
							String strid = "";
							while (i < classInfo.length()) {
								if (classInfo.regionMatches(i, "id=", 0, 3)) {
									int start = i + 4;
									int j = start + 1;
									boolean endid = false;
									// finding id
									while (j < classInfo.length() && !endid) {
										if (classInfo.regionMatches(j, "\"", 0, 1)) {
											endid = true;
											i = j;
											strid = classInfo.substring(start, j);
										} else {
											j++;
										}
									}
								}

								if (classInfo.regionMatches(i, "nfragments=", 0, 11)) {
									int startfrag = i + 12;
									int k = startfrag + 1;
									boolean endfrag = false;
									String strfrag = "";
									while (k < classInfo.length() && !endfrag) {
										if (classInfo.regionMatches(k, "\"", 0, 1)) {
											endfrag = true;
											i = k;
											strfrag = classInfo.substring(startfrag, k); // nfragments
											nfragment = Integer.parseInt(strfrag);
										} else {
											k++;
										}
									}
								}
								i++;
							}

							// staff elements
							Element cloneClass = doc.createElement("cloneclass");
							rootElement.appendChild(cloneClass);
							cloneClass.setAttribute("id", strid);
							int numf = nfragment;
							int strindex = classIndex + 1;
							while (numf > 0 && strindex < endclassIndex && strindex < strlist.size()) {
								String strfragment = strlist.get(strindex);
								String startline = "";
								String endline = "";
								String filepath = "";
								if (strfragment.startsWith("<source file=")) {
									int findex = 0;
									while (findex < strfragment.length()) {
										if (strfragment.regionMatches(findex, "file=", 0, 5)) {
											int startf = findex + 6;
											int endf = startf + 1;
											boolean bendf = false;
											while (endf < strfragment.length() && !bendf) {
												if (strfragment.regionMatches(endf, "\"", 0, 1)) {
													bendf = true;
													filepath = strfragment.substring(startf, endf);
													findex = endf;
												} else {
													endf++;
												}
											}
										}
										if (strfragment.regionMatches(findex, "startline=", 0, 10)) {
											int startf = findex + 11;
											int endf = startf + 1;
											boolean bendf = false;
											while (endf < strfragment.length() && !bendf) {
												if (strfragment.regionMatches(endf, "\"", 0, 1)) {
													bendf = true;
													startline = strfragment.substring(startf, endf);
													findex = endf;
												} else {
													endf++;
												}
											}
										}
										if (strfragment.regionMatches(findex, "endline=", 0, 8)) {
											int startf = findex + 9;
											int endf = startf + 1;
											boolean bendf = false;
											while (endf < strfragment.length() && !bendf) {
												if (strfragment.regionMatches(endf, "\"", 0, 1)) {
													bendf = true;
													endline = strfragment.substring(startf, endf);
													findex = endf;
												} else {
													endf++;
												}
											}
										}
										findex++;
									}
								}
								
								int lines = Integer.parseInt(endline) - Integer.parseInt(startline) + 1;

								// Only add clone to the result if the size is
								// larger than minimum line
								if (lines >= Integer.parseInt(minLine)) {
									// clone elements
									Element clone = doc.createElement("clone");
									cloneClass.appendChild(clone);

									// fragment elements
									Element frag = doc.createElement("fragment");
									frag.setAttribute("file", filepath.replace(prefix, ""));
									frag.setAttribute("start", startline);
									frag.setAttribute("end", endline);
									clone.appendChild(frag);
								}

								if (minimumLines == 0 || lines < minimumLines) {
									minimumLines = lines;
								}
								strindex++;
								numf--;
							}

							if (findclass && endclass) {
								// sbGCFfile.append(tab + "</CloneClass>\r\n");
							}
							classIndex = endclassIndex;
						} else {
							endclassIndex++;
						}
					}
				}
				classIndex++;
			}
			// sbGCFfile.append("</CloneClasses>\r\n");

			// return sbGCFfile.toString();
		} catch (ParserConfigurationException e) {
			log.error(e.getMessage());
		}
		
		return doc;
	}
	
	private static void processSCC(String[] args) {
		if (args.length < 6) {
			log.debug("Please input the right paramenters");
			log.debug("Usage: java -jar gcf_Fileconverter.jar <9 or scc> "
					+ "<basepath> <SourcererCC's clone file location>"
					+ "<SourcererCC's 1st headers file location> "
					+ "<SourcererCC's 2nd headers file location> "
					+ "[minline (optional -- 6: default)]");
			log.debug("Example: java -jar gcf_Fileconverter.jar scc"
					+ " /path/to/remove/ /home/tokens_soclones_index_WITH_FILTER.txt"
					+ " /home/headers.file");
			System.exit(-1);
		}
		if (args.length == 6)
			minLine = args[5];
		else
			minLine = "6";
		
		// create an array list to store all clone fragments
		ArrayList<String> argList = new ArrayList<String>();
		for (int i=0; i<args.length; i++)
			argList.add(args[i]); 
		
		String output = new FileConverterFactory()
				.createFileConverter(IConstant.SOURCERERCC_RESULT_FILE)
				.convert(new File(cloneFile.trim()), argList);
		String filename = cloneFile.trim();
		
		String tmpfileName = filename + "_temp.xml";
		saveConvertedFile(tmpfileName, output);
		
		ArrayList<String> convertedFileLines = new ArrayList<String>();
		convertedFileLines = readConvertedFile(tmpfileName);
		String GCFfile = "";
		
		if (args.length < 6) {
			GCFfile = converteToGCF(convertedFileLines);
		} else {
			int minlineInt = Integer.parseInt(minLine);
			GCFfile = converteToGCFmin(convertedFileLines, minlineInt);
		}
		filename += "-GCF";
		saveConvertedFile(filename + ".xml", GCFfile);
		
		// remove the temp file
		File tmpfile = new File(tmpfileName);

		if (tmpfile.delete()) {
			log.debug(tmpfile.getName() + " is deleted!");
		} else {
			log.error("Delete operation is failed.");
		}
		log.debug("Creating GCF file at " + filename + ".xml");
	}
	
	public static void writeXML(Document doc, String gcffilename) {
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(gcffilename));
			// StreamResult result = new StreamResult(System.out);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
			transformer.transform(source, result);
			log.debug("Creating GCF file at " + gcffilename + ".xml");
		} catch (TransformerConfigurationException e) {
			log.error(e.getMessage());
		} catch (TransformerException e) {
			log.error(e.getMessage());
		}
	}
}
