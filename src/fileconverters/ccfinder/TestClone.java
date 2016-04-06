package fileconverters.ccfinder;

import java.io.*;
import java.util.*;
import java.util.StringTokenizer;

public class TestClone {

	public String language = "";
	public List<AllEntry> entryList = new ArrayList();
	public List<Result> resultList = new ArrayList();
	String fname[] = new String[Parameter.MAX_FILE_NO];
	public List<Clone> representive = new ArrayList();

	// Additional parameters are transferred through list
	public String compute(String filePath, ArrayList list) throws FileNotFoundException, IOException {
		String ccfxprepdir = "";
		String output = "";
		StringBuffer sboutput = new StringBuffer();
		sboutput.append("<cloneDetectionResult>");

		BufferedReader in = new BufferedReader(new FileReader(filePath));
		String str, str0, str1, str10, str11, str12, str13, str2, str20, str21, str22, str23;
		int group, check = 0, pass = 0, fcount = 1;

		LineNumberReader lr = new LineNumberReader(new FileReader(new File(filePath)));
		String info = "\n<info tool=\"" + "CCFinder\" ";
		String string = "";
		String postfix = "";
		String folderpath = "";

		while ((string = lr.readLine()).startsWith("source_files") == false) {
			String tokenizedStr[] = string.split(":");
			info = info + tokenizedStr[0] + "=\"" + tokenizedStr[1].trim() + "\" ";
			if (tokenizedStr[1].trim().startsWith("-preprocessed_file_postfix")) {
				String s[] = tokenizedStr[1].split("\\s+");
				postfix = s[2];
			}
			if (tokenizedStr[1].trim().startsWith("-n")) {
				int nl = tokenizedStr.length - 1;
				String strpath = tokenizedStr[nl].trim();

				// strpath = strpath.replaceAll("/", "\\\\");
				String folders[] = strpath.split("\\u005C", 2);
				nl = folders.length - 1;
				folderpath = "/";
				folderpath += folders[nl];
			}

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
			str = in.readLine();
			// System.out.println(str);
			if (str.equals("source_files {")) {
				pass = 1;
				str = in.readLine();
			}

			if (pass == 0) {
				continue;
			}

			if (str.equals("}")) {
				if (check == 1) {
					break;
				} else {
					check = 1;
					str = in.readLine();
					str = in.readLine();
					str = in.readLine();
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
				clone1.setFid(Integer.parseInt(str10));

				str11 = st1.nextToken();
				StringTokenizer st11 = new StringTokenizer(str11, "-");
				str12 = st11.nextToken();
				clone1.setSt(Integer.parseInt(str12));
				str13 = st11.nextToken();
				clone1.setFt(Integer.parseInt(str13));

				StringTokenizer st2 = new StringTokenizer(str2, ".");
				str20 = st2.nextToken();
				clone2.setFid(Integer.parseInt(str20));

				str21 = st2.nextToken();
				StringTokenizer st22 = new StringTokenizer(str21, "-");
				str22 = st22.nextToken();
				clone2.setSt(Integer.parseInt(str22));
				str23 = st22.nextToken();
				clone2.setFt(Integer.parseInt(str23));

				if (!((clone1.getFid() == clone2.getFid()) && (clone1.getFt() > clone2.getSt()))) {
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
		int nFragment = createResult();
		// System.out.println(nFragment);
		try {
			LineNumber ln = new LineNumber(postfix, ccfxprepdir);
			GenerateIntFileName gen = new GenerateIntFileName(postfix, ccfxprepdir);
			int pcid = 1;
			for (Result r : resultList) {
				String a = Integer.toString(r.getId());
				String si = Integer.toString(r.getCloneList().size());
				// System.out.println("a="+ a + ", si=" + si);
				boolean ch = true;
				for (Clone clone : r.getCloneList()) {
					// System.out.println(clone.getSt());
					int sl = ln.getLineNumber(fname[clone.getFid()], clone.getSt());
					int fl = ln.getLineNumber(fname[clone.getFid()], clone.getFt());
					// int sl = clone.getSt();
					// int fl = clone.getFt();
					
					if (ch == true) {
						String stroutput = "\n<class id=\"" + a + "\" nlines=\"" + (fl - sl + 1) + "\" nfragments=\""
								+ si + "\">";
						sboutput.append(stroutput);
						ch = false;
					}
					String filename = fname[clone.getFid()];
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
			if (in != null) {
				in.close();
			}
		} catch (Exception e) {
			System.err.println("Error in grouping: " + e.getMessage());
		}
		output = sboutput.toString();

		return output;
	}

	private int createResult() {
		Result res;
		int c = 0;
		for (AllEntry en : entryList) {
			res = getResultById(en.getId());
			if (res == null) {
				res = new Result();
				res.setId(en.getId());
				List<Clone> newList = new ArrayList();
				res.setCloneList(newList);
				resultList.add(res);
			}

			if (!searchCloneInResult(en.getId(), en.getC1())) {
				c++;
				res.getCloneList().add(en.getC1());
			}
			if (!searchCloneInResult(en.getId(), en.getC2())) {
				c++;
				res.getCloneList().add(en.getC2());
			}

		}
		// System.out.println("Number of fragments : " + c);
		return c;
	}

	private Result getResultById(int id) {
		for (Result r : resultList) {
			if (r.getId() == id) {
				return r;
			}
		}
		return null;
	}

	boolean searchCloneInResult(int allEntryId, Clone c) {
		for (Result r : resultList) {
			if (r.getId() == allEntryId) {
				for (Clone clone : r.getCloneList()) {
					if ((clone.getFid() == c.getFid()) && (clone.getSt() == c.getSt())
							&& (clone.getFt() == c.getFt())) {
						return true;
					}
				}
			}
		}
		return false;
	}
}

class Clone {

	private int fid;
	private int st;
	private int ft;

	public int getFid() {
		return fid;
	}

	public int getSt() {
		return st;
	}

	public int getFt() {
		return ft;
	}

	public void setFid(int f) {
		this.fid = f;
	}

	public void setSt(int s) {
		this.st = s;
	}

	public void setFt(int t) {
		this.ft = t;
	}
}

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

class Result {

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