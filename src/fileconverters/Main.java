package fileconverters;

import fileconverters.IConstant;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import fileconverters.ccfinder.testClone;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

/**
 *
 * @author parvez
 */
public class Main {

	public static void main(String args[]) {
		if (args.length == 0) {
			System.out.println("Usage: java -jar GCFFileConverter.jar <mode> <prefix path> <clone file>\n");
			System.out.println("   <mode>: 1=ccfx, 2=simscan, 3=CPD, 4=ConQAT, 5=iClones, 6=simian, 7=nicad,");
			System.out.println("   <prefix_path>: the unwanted prefix path that you want to remove from the output file.");
			System.out.println("   <clone_file>: the original clone file.\n");
			System.out.println("Example: java -jar gcfFileConverter.jar 6 /unwanted/path simian.txt");
			System.exit(-1);
		}
		
		System.out.println("GCFFileConverter (v. 0.1) ...");
		String[] tools = {"ccfx", "simscan", "CPD", "ConQAT", "iClones", "Simian", "NiCad" };
		System.out.println("Converting " + tools[Integer.valueOf(args[0].trim())-1] + " clone report into GCF format...");
		File tempFile = null;
		
		if (args[0].trim().matches("1")) // ccfinder
		{

			ArrayList<String> list = new ArrayList<String>();
			if (args.length < 2) {
				System.out.println("Please Input the right paramenters");
				System.out.println("1  ccfinder-cook.txt");
				System.exit(-1);
			}
			String strfile = args[1];
			String output = new FileConverterFactory().createFileConverter(IConstant.CCFINDER_RESULT_FILE).convert(new File(args[1]), list);
			
			String filename[] = args[1].trim().split("\\.");
			saveConvertedFile(filename[0] + "temp.xml", output);
			ArrayList<String> strlist = new ArrayList<String>();
			String filecontent = readConvertedFile(filename[0] + "temp.xml", strlist);
			tempFile = new File(filename[0] + "temp.xml");
			String GCFfile = "";
			if (args.length == 2) {
				GCFfile = converteToGCF(strlist);
			} else if (args.length == 3) {
				int minilines = Integer.parseInt(args[2]);
				GCFfile = converteToGCFmin(strlist, minilines);
			}
			String gcffilename = filename[0] + "-GCF";
			saveConvertedFile(gcffilename + ".xml", GCFfile);
		}
		// SIMSCAN
		else if (args[0].trim().matches("2"))
		{
			if (args.length < 3) {
				System.out.println("Please Input the right paramenters");
				System.out.println("2 E:\\Fileconverters\\simscan_eclipse-ant  eclipse-ant 6");
				System.exit(-1);
			}

			ArrayList<String> list = new ArrayList<String>();
			String output = new FileConverterFactory().createFileConverter(
					IConstant.SIMSCAN_RESULT_FILE).convert(
					new File(args[1].trim()), list);
			String filename = args[2].trim();
			saveConvertedFile(filename + "temp.xml", output);
			ArrayList<String> strlist = new ArrayList<String>();
			String filecontent = readConvertedFile(filename + "temp.xml", strlist);
			tempFile = new File(filename + "temp.xml");
			String GCFfile = "";
			if (args.length == 3) {
				GCFfile = converteToGCF(strlist);
			} else if (args.length == 4) {
				int minilines = Integer.parseInt(args[3]);
				GCFfile = converteToGCFmin(strlist, minilines);
			}
			filename += "-GCF";
			saveConvertedFile(filename + ".xml", GCFfile);
		}
		// CPD
		else if (args[0].trim().matches("3"))
		{
			if (args.length < 3) {
				System.out.println("Please Input the right paramenters");
				System.out.println("3 E:/UCL/bellon/cook  PMDCPD-cook.txt");
				System.exit(-1);
			}
			ArrayList<String> list = new ArrayList<String>();
			list.add(args[1].trim());// base path
			list.add(args[2].trim());// //filename
			String filename[] = args[2].trim().split("\\.");

			if (args.length == 4) {
				list.add(args[3].trim());
			}
			String output = new FileConverterFactory().createFileConverter(
					IConstant.CPD_RESULT_FILE).convert(
					new File("CPD-postgresql.txt"), list);
			String gcffilename = filename[0] + "-GCF";
			saveConvertedFile(gcffilename + ".xml", output);
			tempFile = new File(gcffilename + "temp.xml");
		}
		// ConQAT
		else if (args[0].trim().matches("4"))
		{
			ArrayList<String> list = new ArrayList<String>();
			if (args.length < 4) {
				System.out.println("Please Input the right paramenters");
				System.out.println("4 cook ConQAT-cook.xml 6");
				System.exit(-1);
			}
			list.add(args[1].trim()); // base path
			list.add(args[2].trim()); // filename
			list.add(args[3].trim()); // minimum lines
			String filename[] = args[2].trim().split("\\.");

			String output = new FileConverterFactory().createFileConverter(
					IConstant.CONQAT_RESULT_FILE).convert(new File(filename[0]), list);
			String gcffilename = filename[0] + "-GCF";
			saveConvertedFile(gcffilename + ".xml", output);
			tempFile = new File(gcffilename + "temp.xml");
		}
		// iClones
		else if (args[0].trim().matches("5"))
		{
			if (args.length < 2) {
				System.out.println("Please Input the right paramenters");
				System.out.println("5 IClone-cook.rcf");
				System.exit(-1);
			}
			ArrayList<String> list = new ArrayList<String>();
			list.add(args[1].trim());
			if (args.length == 3) {
				list.add(args[2].trim());
			}

			String output = new FileConverterFactory().createFileConverter(
					IConstant.RCF_RESULT_FILE).convert(
					new File("IClone-cook.txt"), list);
			String filename[] = args[1].trim().split("\\.");
			String gcffilename = filename[0] + "-GCF";
			saveConvertedFile(gcffilename + ".xml", output);
			tempFile = new File(gcffilename + "temp.xml");
		}
		// Simian
		else if (args[0].trim().matches("6"))
		{
			if (args.length < 3) {
				System.out.println("Please Input the right paramenters");
				System.out.println("6 E:/UCL/2simian/simian-2.3.33/bin/cook simian-cook-6-ignorevariablename.txt");
				System.exit(-1);
			}

			ArrayList<String> list = new ArrayList<String>();
			list.add(args[1].trim()); // basepath
			String output = new FileConverterFactory().createFileConverter(IConstant.SIIMIAN_RESULT_FILE).convert(new File(args[2].trim()), list);
			
			String filename[] = args[2].trim().split("\\."); // filename
			saveConvertedFile(filename[0] + "temp.xml", output);
			ArrayList<String> strlist = new ArrayList<String>();
			String filecontent = readConvertedFile(filename[0] + "temp.xml", strlist);
			tempFile = new File(filename[0] + "temp.xml");
			String GCFfile = "";
			
			if (args.length == 3) {
				GCFfile = converteToGCF(strlist);
			} else if (args.length == 4) {
				int minilines = Integer.parseInt(args[3]);
				GCFfile = converteToGCFmin(strlist, minilines);
			}
			String gcffilename = filename[0] + "-GCF";
			saveConvertedFile(gcffilename + ".xml", GCFfile);
			System.out.println(gcffilename + ".xml");
		}
		// NiCad
		else if (args[0].trim().matches("7"))
		{
			if (args.length < 3) {
				System.out.println("Please Input the right paramenters");
				System.out.println("7 cook cook_blocks-clones-0.3-classes.xml");
				System.exit(-1);
			}
			ArrayList<String> list = new ArrayList<String>();
			list.add(args[1].trim()); // basepath
			String output = new FileConverterFactory().createFileConverter(IConstant.NICAD_RESULT_FILE).convert(new File(args[2].trim()), list);
			String filename[] = args[2].trim().split("\\."); // filename
			saveConvertedFile(filename[0] + "temp.xml", output);
			tempFile = new File(filename[0] + "temp.xml");
			ArrayList<String> strlist = new ArrayList<String>();
			String filecontent = readConvertedFile(filename[0] + "temp.xml", strlist);
			String GCFfile = "";
			if (args.length == 3) {
				GCFfile = converteToGCF(strlist);
			} else if (args.length == 4) {
				int minilines = Integer.parseInt(args[3]);
				GCFfile = converteToGCFmin(strlist, minilines);
			}
			String gcffilename = filename[0] + "-GCF";
			saveConvertedFile(gcffilename + ".xml", GCFfile);
		}

		if (tempFile != null)
			// delete the unused temp file
			tempFile.delete();
	}

	public static void saveConvertedFile(String filename, String filecontent) {
		java.io.File f = new java.io.File(filename);
		java.io.FileWriter out = null;
		try {
			out = new java.io.FileWriter(f);
			out.write(filecontent);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
			}
		}

	}

	public static String readConvertedFile(String filename,
			ArrayList<String> list) {
		java.io.BufferedReader infile = null;
		String inLine;
		String filecontent = "";
		try {
			// Create a buffered stream
			infile = new java.io.BufferedReader(
					new java.io.FileReader(filename));
			inLine = infile.readLine();
			boolean firstLine = true;
			while (inLine != null) {
				list.add(inLine);
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

	public static String converteToGCF(ArrayList<String> strlist) {
		StringBuffer sbGCFfile = new StringBuffer();
		sbGCFfile.append("<CloneClasses>\r\n");

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
										strfrag = classInfo.substring(startfrag, k); 
										nfragment = Integer.parseInt(strfrag);
									} else {
										k++;
									}
								}
							}
							i++;
						}
						sbGCFfile.append("<CloneClass>\r\n");
						sbGCFfile.append("<ID>");
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
							
							// System.out.println("strfragment=" + strfragment);
							if (strfragment.startsWith("<source file=")) {
								int findex = 0;
								while (findex < strfragment.length()) {
									if (strfragment.regionMatches(findex,
											"file=", 0, 5)) {
										int startf = findex + 6;
										int endf = startf + 1;
										boolean bendf = false;
										while (endf < strfragment.length()
												&& !bendf) {
											if (strfragment.regionMatches(endf,
													"\"", 0, 1)) {
												bendf = true;

												filepath = strfragment.substring(startf, endf);
												findex = endf;
											} else {
												endf++;
											}
										}
									}
									if (strfragment.regionMatches(findex,
											"startline=", 0, 10)) {
										int startf = findex + 11;
										int endf = startf + 1;
										boolean bendf = false;
										while (endf < strfragment.length()
												&& !bendf) {
											if (strfragment.regionMatches(endf,"\"", 0, 1)) {
												bendf = true;
												startline = strfragment.substring(startf, endf);
												findex = endf;
											} else {
												endf++;
											}
										}

									}
									if (strfragment.regionMatches(findex,"endline=", 0, 8)) {
										int startf = findex + 9;
										int endf = startf + 1;
										boolean bendf = false;
										while (endf < strfragment.length()
												&& !bendf) {
											if (strfragment.regionMatches(endf,"\"", 0, 1)) {
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

							sbGCFfile.append("<Clone>\r\n");
							sbGCFfile.append("<Fragment>\r\n");
							sbGCFfile.append("<File>");
							sbGCFfile.append(filepath);
							sbGCFfile.append("</File>\r\n");
							sbGCFfile.append("<Start>");
							sbGCFfile.append(startline);
							sbGCFfile.append("</Start>\r\n");
							sbGCFfile.append("<End>");
							sbGCFfile.append(endline);
							sbGCFfile.append("</End>\r\n");
							sbGCFfile.append("</Fragment>\r\n");
							sbGCFfile.append("</Clone>\r\n");
							int lines = Integer.parseInt(endline) - Integer.parseInt(startline) + 1;
							if (minimumLines == 0 || lines < minimumLines) {
								minimumLines = lines;
							}

							strindex++;
							numf--;
						}

						if (findclass && endclass) {
							sbGCFfile.append("</CloneClass>\r\n");
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
		System.out.println("Finished converting the given file to GCF: ");
		
		return sbGCFfile.toString();
	}

	public static String converteToGCFmin(ArrayList<String> strlist,
			int minimumlines) {

		StringBuffer sbGCFfile = new StringBuffer();
		sbGCFfile.append("<CloneClasses>\r\n");

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
									} else {
										j++;
									}
								}
							}
							if (classInfo
									.regionMatches(i, "nfragments=", 0, 11)) {
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
									} else {
										k++;
									}
								}
							}
							i++;
						}
						StringBuffer sb = new StringBuffer();
						sb.append("<CloneClass>\r\n");
						sb.append("<ID>");
						sb.append(strid);
						sb.append("</ID>\r\n");
						int fragmentcountofclass = 0;
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
										while (endf < strfragment.length()
												&& !bendf) {
											if (strfragment.regionMatches(endf,"\"", 0, 1)) {
												bendf = true;
												filepath = strfragment.substring(startf, endf);
												// filepath);
												findex = endf;
											} else {
												endf++;
											}
										}
									}
									if (strfragment.regionMatches(findex,
											"startline=", 0, 10)) {
										int startf = findex + 11;
										int endf = startf + 1;
										boolean bendf = false;
										while (endf < strfragment.length()&& !bendf) {
											if (strfragment.regionMatches(endf,"\"", 0, 1)) {
												bendf = true;
												startline = strfragment.substring(startf, endf);
												findex = endf;
											} else {
												endf++;
											}
										}
									}
									if (strfragment.regionMatches(findex,"endline=", 0, 8)) {
										int startf = findex + 9;
										int endf = startf + 1;
										boolean bendf = false;
										while (endf < strfragment.length()&& !bendf) {
											if (strfragment.regionMatches(endf,"\"", 0, 1)) {
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

							int lines = Integer.parseInt(endline)
									- Integer.parseInt(startline) + 1;
							if (lines >= minimumlines) {
								fragmentcountofclass++;
								if (minimumLines == 0 || lines < minimumLines) {
									minimumLines = lines;
								}
								sb.append("<Clone>\r\n");
								sb.append("<Fragment>\r\n");
								sb.append("<File>");
								sb.append(filepath);
								System.out.println(filepath);
								sb.append("</File>\r\n");
								sb.append("<Start>");
								sb.append(startline);
								sb.append("</Start>\r\n");
								sb.append("<End>");
								sb.append(endline);
								sb.append("</End>\r\n");
								sb.append("</Fragment>\r\n");
								sb.append("</Clone>\r\n");
							}
							strindex++;
							numf--;
						}
						if (findclass && endclass && fragmentcountofclass >= 2) {
							sbGCFfile.append(sb.toString());
							sbGCFfile.append("</CloneClass>\r\n");
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
		System.out.println(minimumLines);

		return sbGCFfile.toString();

	}
}
