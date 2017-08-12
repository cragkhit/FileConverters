package fileconverters.ccfinder;

import java.io.*;
import java.util.*;
import java.util.StringTokenizer;

/***
 * A processor for CCFX clones
 * @author T.T. Wang, maintained by Chaiyong R.
 *
 */
public class CCFXCloneProcessor {

	public String language = "";
	public List<AllEntry> entryList = new ArrayList<AllEntry>();
	public List<CloneClass> resultList = new ArrayList<CloneClass>();
	String fname[] = new String[Parameter.MAX_FILE_NO];
	public List<Clone> representive = new ArrayList<Clone>();

	// Additional parameters are transferred through list
	public String compute(String filePath, ArrayList<String> list) throws FileNotFoundException, IOException {
		// get the ccfxprepdir
		String ccfxprepdir = list.get(3);
		boolean createCloneClass = Boolean.parseBoolean(list.get(5));
		String output = "";
		StringBuffer sboutput = new StringBuffer();
		sboutput.append("<cloneDetectionResult>");
		BufferedReader inputCloneFile = new BufferedReader(new FileReader(filePath));
		String str, str0, str1, str10, str11, str12, str13, str2, str20, str21, str22, str23;
		int check = 0, pass = 0, fcount = 1;

		LineNumberReader lr = new LineNumberReader(new FileReader(new File(filePath)));
		String info = "\n<info tool=\"" + "CCFinder\" ";
		String line = "";
		String postfix = "";
		String pathPrefix1 = "";
		String pathPrefix2 = "";
		
		// process the header lines
		while ((line = lr.readLine()).startsWith("source_files") == false) {
			// split the keys and values
			String tokenizedStr[] = line.split(":");
			info = info + tokenizedStr[0] + "=\"" + tokenizedStr[1].trim() + "\" ";
			// get the postfix extension to locate processed data in ccfxprepdir
			if (tokenizedStr[1].trim().startsWith("-preprocessed_file_postfix")) {
				String s[] = tokenizedStr[1].split("\\s+");
				postfix = s[2];
			}
			
			// extract path prefix of the two clone locations
			if (tokenizedStr[1].trim().startsWith("-n")) {
				String folders[] = tokenizedStr[1].trim().split("-n");
				if (pathPrefix1.equals(""))
					pathPrefix1 = folders[1].trim();
				else
					pathPrefix2 = folders[1].trim();
			}
			
			// extract programming language of the clones
			if (tokenizedStr[0].trim().startsWith("preprocess_sript")) {
				this.language = tokenizedStr[1];
			}
		}

		while (true) {
			AllEntry entry = new AllEntry();
			Clone clone1 = new Clone();
			Clone clone2 = new Clone();

			final int i = 9;
			char c2 = i;
			str = inputCloneFile.readLine();
			if (str.equals("source_files {")) {
				pass = 1;
				str = inputCloneFile.readLine();
			}

			if (pass == 0) {
				continue;
			}

			if (str.equals("}")) {
				if (check == 1) {
					break;
				} else {
					check = 1;
					str = inputCloneFile.readLine();
					str = inputCloneFile.readLine();
					str = inputCloneFile.readLine();
					continue;
				}
			}

			if (check == 1) {
				StringTokenizer st = new StringTokenizer(str, "" + c2);
				str0 = st.nextToken();
				entry.setId(Integer.parseInt(str0));
				str1 = st.nextToken();
				str2 = st.nextToken();
				StringTokenizer st1 = new StringTokenizer(str1, ".");
				str10 = st1.nextToken();
				clone1.setFileId(Integer.parseInt(str10));
				str11 = st1.nextToken();
				StringTokenizer st11 = new StringTokenizer(str11, "-");
				str12 = st11.nextToken();
				clone1.setStartLine(Integer.parseInt(str12));
				str13 = st11.nextToken();
				clone1.setEndLine(Integer.parseInt(str13));
				StringTokenizer st2 = new StringTokenizer(str2, ".");
				str20 = st2.nextToken();
				clone2.setFileId(Integer.parseInt(str20));
				str21 = st2.nextToken();
				StringTokenizer st22 = new StringTokenizer(str21, "-");
				str22 = st22.nextToken();
				clone2.setStartLine(Integer.parseInt(str22));
				str23 = st22.nextToken();
				clone2.setEndLine(Integer.parseInt(str23));

				// skip self-clones
				if (!((clone1.getFileId() == clone2.getFileId()) 
						&& (clone1.getEndLine() > clone2.getStartLine()))) {
//					System.out.println(clone1 + ": " + clone2);
					entry.setC1(clone1);
					entry.setC2(clone2);
					entryList.add(entry);
				}
			} else {
				StringTokenizer stn = new StringTokenizer(str);
				stn.nextToken();
				fname[fcount++] = stn.nextToken();
				stn.nextToken();
			}
		}

		System.out.println("Total no. of fragments (before): " + entryList.size());
		int nFragment = createResult(createCloneClass);
		System.out.println("Total no. of clone classes: " + nFragment);
		 
		try {
			LineNumber ln = new LineNumber(postfix, ccfxprepdir);
			GenerateIntFileName gen = new GenerateIntFileName(postfix, ccfxprepdir);
			int pcid = 1;
			for (CloneClass r : resultList) {
				String a = Integer.toString(r.getId());
				String si = Integer.toString(r.getCloneList().size());
				boolean ch = true;
				for (Clone clone : r.getCloneList()) {
					int sl = ln.getLineNumber(fname[clone.getFileId()], clone.getStartLine(), pathPrefix1, pathPrefix2);
					int fl = ln.getLineNumber(fname[clone.getFileId()], clone.getEndLine(), pathPrefix1, pathPrefix2);
					
					if (ch == true) {
						String stroutput = "\n<class id=\"" + a + "\" nlines=\"" + (fl - sl + 1) + "\" nfragments=\""
								+ si + "\">";
						sboutput.append(stroutput);
						ch = false;
					}
					String filename = fname[clone.getFileId()];
					// filenamem = filenamem.replaceAll("/", "\\\\");
					// String reducedfilename = gen.change(filenamem, folderpath);
					String stroutput = "\n<source file=\"" + filename + "\" startline=\"" + sl
							+ "\" endline=\"" + fl + "\" pcid=\"" + (pcid++) + "\"></source>";
					sboutput.append(stroutput);
				}
				sboutput.append("\n</class>");
			}
			sboutput.append("\n</cloneDetectionResult>");
			// Close the output stream
			if (inputCloneFile != null) {
				inputCloneFile.close();
			}
		} catch (Exception e) {
			System.err.println("Error in grouping: " + e.getMessage());
		}
		output = sboutput.toString();

		return output;
	}

	/**
	 * Create a list of clone classes or clone pairs
	 * @param createCloneClass -- true = create clone classes, false = create clone pairs
	 * @return number of classes or pairs created
	 */
	private int createResult(boolean createCloneClass) {
		System.out.println("create clone class?: " + createCloneClass);
		CloneClass cloneClass = new CloneClass();
		
		if (createCloneClass) {
			for (AllEntry en : entryList) {
				cloneClass = getResultById(en.getId());
				if (cloneClass == null) {
					cloneClass = new CloneClass();
					cloneClass.setId(en.getId());
					List<Clone> newList = new ArrayList<Clone>();
					cloneClass.setCloneList(newList);
					resultList.add(cloneClass);
				}
				if (!searchCloneInResult(en.getId(), en.getC1())) {
					cloneClass.getCloneList().add(en.getC1());
				}
				if (!searchCloneInResult(en.getId(), en.getC2())) {
					cloneClass.getCloneList().add(en.getC2());
				}
			}
			return cloneClass.getCloneList().size();
		} else {
			for (AllEntry en : entryList) {
				cloneClass.setId(en.getId());
				List<Clone> newList = new ArrayList<Clone>();
				cloneClass.setCloneList(newList);
				resultList.add(cloneClass);
				cloneClass.getCloneList().add(en.getC1());
				cloneClass.getCloneList().add(en.getC2());
			}
			return resultList.size();
		}
		
	}

	private CloneClass getResultById(int id) {
		for (CloneClass r : resultList) {
			if (r.getId() == id) {
				return r;
			}
		}
		return null;
	}

	boolean searchCloneInResult(int allEntryId, Clone c) {
		for (CloneClass r : resultList) {
			if (r.getId() == allEntryId) {
				for (Clone clone : r.getCloneList()) {
					if ((clone.getFileId() == c.getFileId()) 
							&& (clone.getStartLine() == c.getStartLine())
							&& (clone.getEndLine() == c.getEndLine())) {
						return true;
					}
				}
			}
		}
		return false;
	}
}

/***
 * A class to store a clone pair
 * @author T.T. Wang, maintained by Chaiyong R.
 *
 */
class Clone {

	private int fileId;
	private int startLine;
	private int endLine;

	public int getFileId() {
		return fileId;
	}

	public int getStartLine() {
		return startLine;
	}

	public int getEndLine() {
		return endLine;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public void setStartLine(int startLine) {
		this.startLine = startLine;
	}

	public void setEndLine(int endLine) {
		this.endLine = endLine;
	}
	
	@Override
	public String toString() {
		return getFileId() + "(" + getStartLine() + "," + getEndLine() + ")";
	}
}

/***
 * An inner class to store all clone entries
 * @author T.T. Wang, maintained by Chaiyong R.
 *
 */
class AllEntry {

	private int id;
	private Clone c1;
	private Clone c2;

	public int getId() {
		return id;
	}

	public Clone getC1() {
		return c1;
	}

	public Clone getC2() {
		return c2;
	}

	public void setId(int i) {
		this.id = i;
	}

	public void setC1(Clone c1) {
		this.c1 = c1;
	}

	public void setC2(Clone c2) {
		this.c2 = c2;
	}
}

/***
 * A class to store clone classes
 * @author T.T. Wang, maintained by Chaiyong R.
 *
 */
class CloneClass {
	private int id;
	private List<Clone> cloneList;

	public int getId() {
		return id;
	}

	public List<Clone> getCloneList() {
		return cloneList;
	}

	public void setId(int i) {
		this.id = i;
	}

	public void setCloneList(List<Clone> c1) {
		this.cloneList = c1;
	}
}