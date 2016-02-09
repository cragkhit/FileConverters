package fileconverters.ccfinder;

import java.io.*;
import java.util.*;
import java.util.StringTokenizer;

public class testClone {

    public String language = "";
    public List<AllEntry> entryList = new ArrayList();
    public List<Result> resultList = new ArrayList();
    String fname[] = new String[Parameter.MAX_FILE_NO];
    public List<Clone> representive = new ArrayList();

    // Additional parameters are transferred through list
    public String compute(String filePath, ArrayList list) throws FileNotFoundException, IOException {
        //String ccfxprepdir = (String)list.get(0);
        String ccfxprepdir = "";
        //String output="<cloneDetectionResult>";
        String output = "";
        StringBuffer sboutput = new StringBuffer();//
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
            //  System.out.println("Tokenizer = "+tokenizedStr[0]);
            if (tokenizedStr[1].trim().startsWith("-preprocessed_file_postfix")) {
                String s[] = tokenizedStr[1].split("\\s+");
                postfix = s[2];
                //  System.out.println(tokenizedStr.length);
                //System.out.println("postfix = "+postfix);
            }
            //////////////wtt/////////////////////////////////

            if (tokenizedStr[1].trim().startsWith("-n")) {

                // System.out.println(tokenizedStr[1].trim());

                // System.out.println("------------------------------");

                int nl = tokenizedStr.length - 1;

                //System.out.println(tokenizedStr.length);
                //  System.out.println(tokenizedStr[nl].trim());
                String strpath = tokenizedStr[nl].trim();

                strpath = strpath.replaceAll("/", "\\\\");
                String folders[] = strpath.split("\\u005C", 2);
                nl = folders.length - 1;
                folderpath = "\\";
                folderpath += folders[nl];
                //System.out.println(folderpath);
            }

            ///////////////////////////////////////

            // System.out.println("OKOKOKOKOK ");
            if (tokenizedStr[0].trim().startsWith("preprocess_sript")) {
                this.language = tokenizedStr[1];
            }
        }
        //    output=output+info+"/>";//wtt
        while (true) {
            AllEntry entry = new AllEntry();
            Clone clone1 = new Clone();
            Clone clone2 = new Clone();

            final int i = 9;
            char c2 = i;
            str = in.readLine();
            //System.out.println("I am here");
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
                // System.out.println(stn.nextToken());
                fname[fcount++] = stn.nextToken();

                stn.nextToken();
            }
        }
        int nFragment = createResult();



        //print result

        /*
         * for(Result r:resultList){ System.out.println(r.getId());
         * System.out.println(r.getCloneList().size()); for(Clone
         * clone:r.getCloneList()){
         * System.out.println(clone.getFid()+"."+clone.getSt()+"-"+clone.getFt());
         * } System.out.println(); }
         */
        try {
            // Create file
            //FileWriter fstream = new FileWriter("/home/parvez/output.txt");
            //BufferedWriter out = new BufferedWriter(fstream);
            //out.write("<clones>");
            //out.newLine();
            //out.write("<systeminfo  system=\""+Parameter.SYS_NAME+"\" granularity=\""+Parameter.GRANULARITY+"\" threshold=\"0%\" minlines=\"3\" maxlines=\"2000\"/>");
            //out.newLine();
            //out.write("<classinfo npcs=\"X\" nclones=\"X\" nfragments=\""+nFragment+"\" npairs=\"X\" nclasses=\""+resultList.size()+"\"/>");
            //out.newLine();
            //out.write("<runinfo ncompares=\"X\" cputime=\"X\"/>");
            //out.newLine();
            //out.newLine();
            //System.out.println("I am here");
            lineNumber ln = new lineNumber(postfix, ccfxprepdir);
            generateIntFileName gen = new generateIntFileName(postfix, ccfxprepdir);
            int pcid = 1;
            for (Result r : resultList) {
                //System.out.println("I am here");

                String a = Integer.toString(r.getId());

                String si = Integer.toString(r.getCloneList().size());

                //System.out.print(si);
                boolean ch = true;
                for (Clone clone : r.getCloneList()) {
                    //out.write(clone.getFid()+"."+clone.getSt()+"-"+clone.getFt());
                    //out.newLine();
                    int sl = ln.getLineNumber(fname[clone.getFid()], clone.getSt());
                    int fl = ln.getLineNumber(fname[clone.getFid()], clone.getFt());
                    //System.out.println("start="+fname[clone.getFid()]);
                    //System.out.println("sl = "+sl +"  fl= "+fl);
                    //int sl=5; int fl =5;
                    if (ch == true) {
                        //   output=output+"\n<class id=\""+a+"\" nlines=\""+(fl-sl+1)+"\" nfragments=\""+si+"\">";					    
                        String stroutput = "\n<class id=\"" + a + "\" nlines=\"" + (fl - sl + 1) + "\" nfragments=\"" + si + "\">";
                        sboutput.append(stroutput);


                        ch = false;
                    }
                    // System.out.println(folderpath+"foldpath");
                    // output = output + "\n<source file=\"" + gen.change(fname[clone.getFid()]) + "\" startline=\"" + sl + "\" endline=\"" + fl + "\" pcid=\"" + (pcid++) + "\"></source>";
                    String filenamem = fname[clone.getFid()];
                    filenamem = filenamem.replaceAll("/", "\\\\");
                    //   System.out.println(filenamem);
                    // System.out.println("okok----");
                    String reducedfilename = gen.change(filenamem, folderpath);
                    //System.out.println(reducedfilename);

                    //output = output + "\n<source file=\"" + gen.change(filenamem,folderpath) + "\" startline=\"" + sl + "\" endline=\"" + fl + "\" pcid=\"" + (pcid++) + "\"></source>";//wtt reduce the parent folder
                    String stroutput = "\n<source file=\"" + gen.change(filenamem, folderpath) + "\" startline=\"" + sl + "\" endline=\"" + fl + "\" pcid=\"" + (pcid++) + "\"></source>";//wtt reduce the parent folder
                    sboutput.append(stroutput);

                    // System.out.println("okok----");
                    //System.out.println(gen.change(filenamem,folderpath));
                    // output = output + "\n<source file=\"" + gen.change(fname[clone.getFid()],folderpath) + "\" startline=\"" + sl + "\" endline=\"" + fl + "\" pcid=\"" + (pcid++) + "\"></source>";//wtt reduce the parent folder

                }
                // output = output+"\n</class>";
                sboutput.append("\n</class>");
            }
            // output=output+"\n</cloneDetectionResult>";
            sboutput.append("\n</cloneDetectionResult>");
            //Close the output stream
            //  System.out.println(output);

            if (in != null) {
                in.close();
            }
        } catch (Exception e) {

            System.err.println("Error in grouping: " + e.getMessage());
        }
        output = sboutput.toString();

        return output;
    }

    int createResult() {
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
        //System.out.println("Number of fragments : "+c);
        return c;
    }

    Result getResultById(int id) {
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
                    if ((clone.getFid() == c.getFid()) && (clone.getSt() == c.getSt()) && (clone.getFt() == c.getFt())) {
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