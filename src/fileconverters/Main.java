package fileconverters;

import fileconverters.IConstant;
import java.io.File;
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
		// String output = new
		// FileConverterFactory().createFileConverter(CloneDetector.NICAD).convert(new
		// File("/home/parvez/Downloads/junit_functions-clones/junit_functions-clones-0.3.xml"));
		// String output = new
		// FileConverterFactory().createFileConverter(CloneDetector.SIMIAN).convert(new
		// File("/home/parvez/Downloads/Simian/bin/jhotdraw/resultSimian"));

		if (args.length == 0) {
			System.out.println("Please Input paramenters");
			System.exit(-1);
		}

		if (args[0].trim().matches("1")) // ccfinder
		{

			ArrayList list = new ArrayList();

			// list.add("E:/UCL/ccfx-win32-en");//base path of ccfinder tool's
			// folder
			// before running the fileconverter move the content under
			// .ccfxprepdir folder and the ccfinderresult.txt to the parent
			// folder of .ccfxprepdir
			// String output = new
			// FileConverterFactory().createFileConverter(CloneDetector.CCFINDER).convert(new
			// File("/home/parvez/Downloads/result/ccfinder/ArgoUML-0.32.BETA_2-src.txt"),
			// list);
			/*
			 * String output = new
			 * FileConverterFactory().createFileConverter(IConstant
			 * .CCFINDER_RESULT_FILE).convert(new
			 * File("ccfinder-postgresql.txt"), list);
			 * 
			 * String filename = "ccfinder-postgresql";
			 */
			// String strfile="ccfinder-weltab.txt";

			// System.out.println("create file converter");
			// String output = new
			// FileConverterFactory().createFileConverter(IConstant.CCFINDER_RESULT_FILE).convert(new
			// File(strfile), list);

			// String filename = "ccfinder-weltab";

			// ////////////////////////
			if (args.length < 2) {
				System.out.println("Please Input the right paramenters");
				System.out.println("1  ccfinder-cook.txt");
				System.exit(-1);
			}
			// list.add(args[1].trim());// directory of .ccfxprepdir file
			String strfile = args[1];
			String output = new FileConverterFactory().createFileConverter(
					IConstant.CCFINDER_RESULT_FILE).convert(new File(args[1]),
					list);
			String filename[] = args[1].trim().split("\\.");

			// /////////////////////////////////////////

			// String output = new
			// FileConverterFactory().createFileConverter(IConstant.NICAD_RESULT_FILE).convert(new
			// File("ccfinder-postgresql.txt"),list);/////////////
			// String output = new
			// FileConverterFactory().createFileConverter(CloneDetector.NICAD).convert(new
			// File("/home/parvez/Downloads/linux-2.6.24.2_functions-clones/linux-2.6.24.2_functions-clones-0.3.xml"));

			// //////////////////////////////////////////////////////////////////////
			saveConvertedFile("." + filename[0] + "temp.xml", output);

			ArrayList<String> strlist = new ArrayList();
			String filecontent = readConvertedFile("." + filename[0]
					+ "temp.xml", strlist);

			// System.out.println(strlist.size());

			// String GCFfile = converteToGCF(strlist);
			String GCFfile = "";
			if (args.length == 2) {
				GCFfile = converteToGCF(strlist);
			} else if (args.length == 3) {
				int minilines = Integer.parseInt(args[2]);
				GCFfile = converteToGCFmin(strlist, minilines);
			}

			String gcffilename = filename[0] + "-GCF";

			saveConvertedFile(gcffilename + ".xml", GCFfile);
		} else if (args[0].trim().matches("2"))// SIMSCAN
		{
			if (args.length < 3) {
				System.out.println("Please Input the right paramenters");
				System.out
						.println("2 E:\\Fileconverters\\simscan_eclipse-ant  eclipse-ant 6");
				System.exit(-1);
			}

			ArrayList list = new ArrayList();

			String output = new FileConverterFactory().createFileConverter(
					IConstant.SIMSCAN_RESULT_FILE).convert(
					new File(args[1].trim()), list);// ///////////directory
			// String output = new
			// FileConverterFactory().createFileConverter(IConstant.SIMSCAN_RESULT_FILE).convert(new
			// File("\\simscan_eclipse-ant"),list);/////////////directory
			String filename = args[2].trim();

			saveConvertedFile("." + filename + "temp.xml", output);
			ArrayList<String> strlist = new ArrayList();
			String filecontent = readConvertedFile("." + filename + "temp.xml",
					strlist);
			String GCFfile = "";
			if (args.length == 3) {
				GCFfile = converteToGCF(strlist);
			} else if (args.length == 4) {
				int minilines = Integer.parseInt(args[3]);
				GCFfile = converteToGCFmin(strlist, minilines);
			}
			filename += "-GCF";
			saveConvertedFile(filename + ".xml", GCFfile);
		} else if (args[0].trim().matches("3"))// CPD/////////////////////////////////////////////
		{
			if (args.length < 3) {
				System.out.println("Please Input the right paramenters");
				System.out.println("3 E:/UCL/bellon/cook  PMDCPD-cook.txt");
				System.exit(-1);
			}
			ArrayList list = new ArrayList();
			/*
			 * list.add(
			 * "E:\\UCL\\pmd-bin-5.0-alpha\\pmd-bin-5.0-alpha\\bin\\postgresql"
			 * );//base path list.add("CPD-postgresql.txt");////filename String
			 * filename ="CPD-postgresql";
			 */

			list.add(args[1].trim());// base path
			list.add(args[2].trim());// //filename
			String filename[] = args[2].trim().split("\\.");

			if (args.length == 4) {
				list.add(args[3].trim());
			}

			// System.out.println(args[1]);
			// System.out.println(args[2]);

			String output = new FileConverterFactory().createFileConverter(
					IConstant.CPD_RESULT_FILE).convert(
					new File("CPD-postgresql.txt"), list);// ///////////filename

			// System.out.println(output);///////////

			String gcffilename = filename[0] + "-GCF";

			saveConvertedFile(gcffilename + ".xml", output);

		} else if (args[0].trim().matches("4"))// ConQAT
		{
			ArrayList list = new ArrayList();
			/*
			 * list.add("eclipse-ant");//detecte fold name
			 * list.add("ConQAT-eclipse-ant.xml");////filename String filename
			 * ="ConQAT-eclipse-ant";
			 */

			/*
			 * list.add("postgresql");//detecte fold name
			 * list.add("ConQAT-postgresql.xml");////filename String filename
			 * ="ConQAT-postgresql";
			 */
			if (args.length < 4) {
				System.out.println("Please Input the right paramenters");
				System.out.println("4 cook ConQAT-cook.xml 6");
				System.exit(-1);
			}
			list.add(args[1].trim());// base path
			list.add(args[2].trim());// //filename
			list.add(args[3].trim());// minimum lines
			String filename[] = args[2].trim().split("\\.");

			String output = new FileConverterFactory().createFileConverter(
					IConstant.CONQAT_RESULT_FILE).convert(
					new File(filename[0]), list);// ///////////filename
			String gcffilename = filename[0] + "-GCF";
			// System.out.println(gcffilename);
			saveConvertedFile(gcffilename + ".xml", output);

		} else if (args[0].trim().matches("5"))// RCF
												// IClone/////////////////////////////
		{

			if (args.length < 2) {
				System.out.println("Please Input the right paramenters");
				System.out.println("5 IClone-cook.rcf");
				System.exit(-1);
			}

			ArrayList list = new ArrayList();
			list.add(args[1].trim());

			if (args.length == 3) {
				list.add(args[2].trim());
			}
			// String strfile=args[2];

			String output = new FileConverterFactory().createFileConverter(
					IConstant.RCF_RESULT_FILE).convert(
					new File("IClone-cook.txt"), list);
			String filename[] = args[1].trim().split("\\.");
			String gcffilename = filename[0] + "-GCF";
			saveConvertedFile(gcffilename + ".xml", output);

		} else if (args[0].trim().matches("6"))// Simian
		{
			// System.out.println("simian");
			if (args.length < 3) {
				System.out.println("Please Input the right paramenters");
				System.out
						.println("6 E:/UCL/2simian/simian-2.3.33/bin/cook simian-cook-6-ignorevariablename.txt");
				System.exit(-1);
			}

			ArrayList list = new ArrayList();
			list.add(args[1].trim());// basepath
			// System.out.println("berfore converterfactory");
			String output = new FileConverterFactory().createFileConverter(
					IConstant.SIIMIAN_RESULT_FILE).convert(
					new File(args[2].trim()), list);
			// System.out.println("after converterfactory");
			String filename[] = args[2].trim().split("\\."); // filename
			saveConvertedFile("." + filename[0] + "temp.xml", output);

			ArrayList<String> strlist = new ArrayList();
			String filecontent = readConvertedFile("." + filename[0]
					+ "temp.xml", strlist);
			// System.out.println(filecontent);

			// System.out.println(strlist.size());
			System.out.println("after viscad.xml");

			// String GCFfile = converteToGCF(strlist);
			String GCFfile = "";
			if (args.length == 3) {
				GCFfile = converteToGCF(strlist);
			} else if (args.length == 4) {
				int minilines = Integer.parseInt(args[3]);
				// System.out.println("convert to GCF");
				GCFfile = converteToGCFmin(strlist, minilines);
			}

			// System.out.println("converteToGCF");

			String gcffilename = filename[0] + "-GCF";

			saveConvertedFile(gcffilename + ".xml", GCFfile);
		} else if (args[0].trim().matches("7"))// NiCad
		{
			if (args.length < 3) {
				System.out.println("Please Input the right paramenters");
				System.out.println("7 cook cook_blocks-clones-0.3-classes.xml");
				System.exit(-1);
			}
			ArrayList list = new ArrayList();
			list.add(args[1].trim());// basepath
			String output = new FileConverterFactory().createFileConverter(
					IConstant.NICAD_RESULT_FILE).convert(
					new File(args[2].trim()), list);// ///////////
			// System.out.println(output);
			// String output = new
			// FileConverterFactory().createFileConverter(CloneDetector.NICAD).convert(new
			// File("/home/parvez/Downloads/junit_functions-clones/junit_functions-clones-0.3.xml"));
			String filename[] = args[2].trim().split("\\."); // filename
			saveConvertedFile("." + filename[0] + "temp.xml", output);

			ArrayList<String> strlist = new ArrayList();
			String filecontent = readConvertedFile("." + filename[0]
					+ "temp.xml", strlist);
			// System.out.println("readConvertedFile");

			// System.out.println(strlist.size());

			// String GCFfile = converteToGCF(strlist);
			String GCFfile = "";
			if (args.length == 3) {
				GCFfile = converteToGCF(strlist);
			} else if (args.length == 4) {
				int minilines = Integer.parseInt(args[3]);
				GCFfile = converteToGCFmin(strlist, minilines);
			}

			// System.out.println("converteToGCF");

			String gcffilename = filename[0] + "-GCF";

			saveConvertedFile(gcffilename + ".xml", GCFfile);
		}

		// System.out.println(output);
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

		// File f = new File(filename);

		// java.io.FileReader in = null;
		java.io.BufferedReader infile = null;

		// Get file name from the text field

		String inLine;
		String filecontent = "";
		// StringBuffer sbfilecontent = new StringBuffer();//

		try {
			// Create a buffered stream
			infile = new java.io.BufferedReader(
					new java.io.FileReader(filename));

			// Read a line
			inLine = infile.readLine();

			boolean firstLine = true;
			// Append the line to the text area

			while (inLine != null) {
				list.add(inLine);
				// System.out.println(inLine);
				// filecontent += inLine + "\n";
				// sbfilecontent.append(inLine);
				// sbfilecontent.append( "\n");

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
		// filecontent = sbfilecontent.toString();
		return filecontent;
	}

	public static String converteToGCF(ArrayList<String> strlist) {

		// String GCFfile = "<CloneClasses>\r\n";
		StringBuffer sbGCFfile = new StringBuffer();
		sbGCFfile.append("<CloneClasses>\r\n");

		int classIndex = 0;
		int endclassIndex = 0;
		boolean findclass = false;
		boolean endclass = false;
		int minimumLines = 0;
		while (classIndex < strlist.size()) {
			// System.out.println(strlist.get(classIndex));///////////
			if (strlist.get(classIndex).startsWith("<class")) {
				String classInfo = strlist.get(classIndex);

				endclassIndex = classIndex + 1;
				endclass = false;

				while (endclassIndex < strlist.size() && !endclass) {
					if (strlist.get(endclassIndex).startsWith("</class>")) {
						// System.out.println(classInfo);
						endclass = true;
						findclass = true;// ///////////////

						int idpo;

						int i = 0;
						int num = 1;
						int nid = 0;
						int nfragment = 0;
						String strid = "";
						// /////////////////////////////////id////////////////////////
						while (i < classInfo.length()) {
							if (classInfo.regionMatches(i, "id=", 0, 3)) {
								// System.out.println( "findid");
								int start = i + 4;
								// System.out.println(classInfo.substring(start)
								// );

								int j = start + 1;
								boolean endid = false;

								while (j < classInfo.length() && !endid) {
									// System.out.println("in j");
									if (classInfo.regionMatches(j, "\"", 0, 1)) {
										endid = true;
										i = j;
										// System.out.println(
										// classInfo.substring(j));
										strid = classInfo.substring(start, j); // ////id
										// System.out.println( strid);
									} else {
										j++;
									}

								}
							}
							// //////////////////////nfragment/////////////////
							if (classInfo
									.regionMatches(i, "nfragments=", 0, 11)) {
								// System.out.println( "findnfragment");
								int startfrag = i + 12;
								// System.out.println(classInfo.substring(start)
								// );

								int k = startfrag + 1;
								boolean endfrag = false;
								String strfrag = "";
								// System.out.println(classInfo.substring(k) );
								while (k < classInfo.length() && !endfrag) {
									// System.out.println("in j");

									if (classInfo.regionMatches(k, "\"", 0, 1)) {
										// System.out.println("find \"");
										endfrag = true;
										i = k;
										// System.out.println(
										// classInfo.substring(j));

										/*
										 * System.out.println( startfrag);
										 * System.out.println( "----");
										 * System.out.println( k-1);
										 * System.out.println( "ok");
										 */
										strfrag = classInfo.substring(
												startfrag, k); // nfragments

										nfragment = Integer.parseInt(strfrag);
										// System.out.println( nfragment);

									} else {
										k++;
									}

								}
								// /////////////////////////////////////

							}// </cloneclass>
							i++;
						}// while(i) classInfo

						// //////////////////
						sbGCFfile.append("<CloneClass>\r\n");
						sbGCFfile.append("<ID>");
						sbGCFfile.append(strid);
						sbGCFfile.append("</ID>\r\n");

						// GCFfile += "<CloneClass>\r\n";
						// GCFfile += "<ID>" + strid + "</ID>\r\n";

						// /////////////////////////////////
						int numf = nfragment;
						int strindex = classIndex + 1;
						while (numf > 0 && strindex < endclassIndex
								&& strindex < strlist.size()) {
							String strfragment = strlist.get(strindex);
							// System.out.println( strfragment);
							String startline = "";
							String endline = "";
							String filepath = "";
							
							System.out.println("strfragment=" + strfragment);
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

												filepath = strfragment
														.substring(startf, endf);
												// System.out.println(
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
										while (endf < strfragment.length()
												&& !bendf) {
											if (strfragment.regionMatches(endf,
													"\"", 0, 1)) {
												bendf = true;

												startline = strfragment
														.substring(startf, endf);
												// System.out.println(startline);
												findex = endf;
											} else {
												endf++;
											}
										}

									}
									if (strfragment.regionMatches(findex,
											"endline=", 0, 8)) {

										int startf = findex + 9;
										int endf = startf + 1;
										boolean bendf = false;
										while (endf < strfragment.length()
												&& !bendf) {
											if (strfragment.regionMatches(endf,
													"\"", 0, 1)) {
												bendf = true;

												endline = strfragment
														.substring(startf, endf);
												// System.out.println(endline);
												findex = endf;
											} else {
												endf++;
											}
										}

									}

									findex++;
								}

							}

							// ////////////////////////////////
							sbGCFfile.append("<Clone>\r\n");
							sbGCFfile.append("<Fragment>\r\n");
							sbGCFfile.append("<File>");
							sbGCFfile.append(filepath);
							// String strreplaced = filepath.replaceAll("/", "\\\\");
							// sbGCFfile.append(strreplaced);

							sbGCFfile.append("</File>\r\n");
							sbGCFfile.append("<Start>");
							sbGCFfile.append(startline);
							sbGCFfile.append("</Start>\r\n");
							sbGCFfile.append("<End>");
							sbGCFfile.append(endline);
							sbGCFfile.append("</End>\r\n");
							sbGCFfile.append("</Fragment>\r\n");
							sbGCFfile.append("</Clone>\r\n");
							/*
							 * GCFfile += "<Clone>\r\n"; GCFfile +=
							 * "<Fragment>\r\n"; GCFfile += "<File>"; GCFfile +=
							 * filepath; GCFfile += "</File>\r\n"; GCFfile +=
							 * "<Start>"; GCFfile += startline; GCFfile +=
							 * "</Start>\r\n"; GCFfile += "<End>"; GCFfile +=
							 * endline; GCFfile += "</End>\r\n"; GCFfile +=
							 * "</Fragment>\r\n"; GCFfile += "</Clone>\r\n";
							 */
							// System.out.println(GCFfile);

							// System.out.println(endline+" "+startline);
							int lines = Integer.parseInt(endline)
									- Integer.parseInt(startline) + 1;

							if (minimumLines == 0 || lines < minimumLines) {
								minimumLines = lines;
							}

							// //////////////////////////////////

							strindex++;
							numf--;
						}// /while fragments

						if (findclass && endclass) {
							sbGCFfile.append("</CloneClass>\r\n");
							// GCFfile += "</CloneClass>\r\n";
						}

						classIndex = endclassIndex; // /////////
					} // if</class>
					else {
						endclassIndex++;
					}

				}
				// findclass=true;

			}
			/*
			 * if(findclass) { classIndex=endclassIndex+1; } else
			 */

			classIndex++;

			// System.out.println("ok");
		}
		// System.out.println("end convert");
		// GCFfile += "</CloneClasses>\r\n";
		sbGCFfile.append("</CloneClasses>\r\n");

		// System.out.print("minimumLines:");
		System.out.println(minimumLines);

		// return GCFfile;
		return sbGCFfile.toString();

	}

	/*
	 * public static String getNum (int pos, String strline, String substr ) {
	 * 
	 * String strnum="";
	 * 
	 * return strnum; }
	 */

	public static String converteToGCFmin(ArrayList<String> strlist,
			int minimumlines) {

		StringBuffer sbGCFfile = new StringBuffer();
		sbGCFfile.append("<CloneClasses>\r\n");
		// String GCFfile = "<CloneClasses>\r\n";

		int classIndex = 0;
		int endclassIndex = 0;
		boolean findclass = false;
		boolean endclass = false;
		int minimumLines = 0;
		while (classIndex < strlist.size()) {
			// System.out.println(strlist.get(classIndex));///////////
			if (strlist.get(classIndex).startsWith("<class")) {
				String classInfo = strlist.get(classIndex);

				endclassIndex = classIndex + 1;
				endclass = false;

				while (endclassIndex < strlist.size() && !endclass) {
					if (strlist.get(endclassIndex).startsWith("</class>")) {
						// System.out.println(classInfo);
						endclass = true;
						findclass = true;// ///////////////

						int idpo;

						int i = 0;
						int num = 1;
						int nid = 0;
						int nfragment = 0;
						String strid = "";
						// /////////////////////////////////id////////////////////////
						while (i < classInfo.length()) {
							if (classInfo.regionMatches(i, "id=", 0, 3)) {
								// System.out.println( "findid");
								int start = i + 4;
								// System.out.println(classInfo.substring(start)
								// );

								int j = start + 1;
								boolean endid = false;

								while (j < classInfo.length() && !endid) {
									// System.out.println("in j");
									if (classInfo.regionMatches(j, "\"", 0, 1)) {
										endid = true;
										i = j;
										// System.out.println(
										// classInfo.substring(j));
										strid = classInfo.substring(start, j); // ////id
										// System.out.println( strid);
									} else {
										j++;
									}

								}
							}
							// //////////////////////nfragment/////////////////
							if (classInfo
									.regionMatches(i, "nfragments=", 0, 11)) {
								// System.out.println( "findnfragment");
								int startfrag = i + 12;
								// System.out.println(classInfo.substring(start)
								// );

								int k = startfrag + 1;
								boolean endfrag = false;
								String strfrag = "";
								// System.out.println(classInfo.substring(k) );
								while (k < classInfo.length() && !endfrag) {
									// System.out.println("in j");

									if (classInfo.regionMatches(k, "\"", 0, 1)) {
										// System.out.println("find \"");
										endfrag = true;
										i = k;
										// System.out.println(
										// classInfo.substring(j));

										/*
										 * System.out.println( startfrag);
										 * System.out.println( "----");
										 * System.out.println( k-1);
										 * System.out.println( "ok");
										 */
										strfrag = classInfo.substring(
												startfrag, k); // nfragments

										nfragment = Integer.parseInt(strfrag);
										// System.out.println( nfragment);

									} else {
										k++;
									}

								}
								// /////////////////////////////////////

							}// </cloneclass>
							i++;
						}// while(i) classInfo

						// //////////////////
						// GCFfile += "<CloneClass>\r\n";
						// GCFfile += "<ID>" + strid + "</ID>\r\n";

						StringBuffer sb = new StringBuffer();
						sb.append("<CloneClass>\r\n");
						sb.append("<ID>");
						sb.append(strid);
						sb.append("</ID>\r\n");

						/*
						 * String GCFfileclass = ""; GCFfileclass +=
						 * "<CloneClass>\r\n"; GCFfileclass += "<ID>";
						 * GCFfileclass += strid; GCFfileclass += "</ID>\r\n";
						 */
						int fragmentcountofclass = 0;

						// /////////////////////////////////
						int numf = nfragment;
						int strindex = classIndex + 1;
						while (numf > 0 && strindex < endclassIndex
								&& strindex < strlist.size()) {
							String strfragment = strlist.get(strindex);
							// System.out.println( strfragment);
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

												filepath = strfragment
														.substring(startf, endf);
												// System.out.println(
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
										while (endf < strfragment.length()
												&& !bendf) {
											if (strfragment.regionMatches(endf,
													"\"", 0, 1)) {
												bendf = true;

												startline = strfragment
														.substring(startf, endf);
												// System.out.println(startline);
												findex = endf;
											} else {
												endf++;
											}
										}

									}
									if (strfragment.regionMatches(findex,
											"endline=", 0, 8)) {

										int startf = findex + 9;
										int endf = startf + 1;
										boolean bendf = false;
										while (endf < strfragment.length()
												&& !bendf) {
											if (strfragment.regionMatches(endf,
													"\"", 0, 1)) {
												bendf = true;

												endline = strfragment
														.substring(startf, endf);
												// System.out.println(endline);
												findex = endf;
											} else {
												endf++;
											}
										}

									}

									findex++;
								}

							}

							// ////////////////////////////////
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
								// String strreplaced = filepath.replaceAll("/", "\\\\");
								// sb.append(strreplaced);
								sb.append("</File>\r\n");
								sb.append("<Start>");
								sb.append(startline);
								sb.append("</Start>\r\n");
								sb.append("<End>");
								sb.append(endline);
								sb.append("</End>\r\n");
								sb.append("</Fragment>\r\n");
								sb.append("</Clone>\r\n");
								/*
								 * GCFfileclass += "<Clone>\r\n"; GCFfileclass
								 * += "<Fragment>\r\n"; GCFfileclass +=
								 * "<File>"; GCFfileclass += filepath;
								 * GCFfileclass += "</File>\r\n"; GCFfileclass
								 * += "<Start>"; GCFfileclass += startline;
								 * GCFfileclass += "</Start>\r\n"; GCFfileclass
								 * += "<End>"; GCFfileclass += endline;
								 * GCFfileclass += "</End>\r\n"; GCFfileclass +=
								 * "</Fragment>\r\n"; GCFfileclass +=
								 * "</Clone>\r\n";
								 */
								// System.out.println(GCFfile);

								// System.out.println(endline+" "+startline);

							}
							// //////////////////////////////////

							strindex++;
							numf--;
						}// /while fragments

						if (findclass && endclass && fragmentcountofclass >= 2) {

							sbGCFfile.append(sb.toString());
							sbGCFfile.append("</CloneClass>\r\n");

							// GCFfile += GCFfileclass;
							// GCFfile += "</CloneClass>\r\n";
						}

						classIndex = endclassIndex; // /////////
					} // if</class>
					else {
						endclassIndex++;
					}

				}
				// findclass=true;

			}
			/*
			 * if(findclass) { classIndex=endclassIndex+1; } else
			 */

			classIndex++;

			// System.out.println("ok");
		}
		// System.out.println("end convert");
		// GCFfile += "</CloneClasses>\r\n";
		sbGCFfile.append("</CloneClasses>\r\n");
		// System.out.print("minimumLines:");
		System.out.println(minimumLines);

		return sbGCFfile.toString();

	}
}
