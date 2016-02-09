package fileconverters.ccfinder;

import java.io.*;
import java.util.StringTokenizer;

public class lineNumber {

    /**
     * @param args
     */
    private String postfix;
    private String ccfxprepdir;

    public lineNumber(String postfix, String ccfxprepdir) {
        this.postfix = postfix;
        this.ccfxprepdir = ccfxprepdir;
    }

    int getLineNumber(String fname, int n) throws IOException {
        generateIntFileName gen = new generateIntFileName(postfix, ccfxprepdir);
        String target = gen.generate(fname);
        //    System.out.println(ccfxprepdir);/////////////////////////
        //    System.out.println("trying to load="+target);/////////////////////
        BufferedReader in = new BufferedReader(new FileReader(target));
        int i = 0;
        int lineNumber = 0;
        String str;
        convert con = new convert();
        while (true) {

            str = in.readLine();
            if (str == null) {
                break;
            }


            if (i == n) {
                //System.out.println(str);
                StringTokenizer st = new StringTokenizer(str, ".");
                lineNumber = con.conHexToDec(st.nextToken());
                break;
                //System.out.println(lineNumber);
            }
            i++;
        }
        // TODO Auto-generated method stub
        if(in!=null)
            in.close();
        return lineNumber;
    }
}
