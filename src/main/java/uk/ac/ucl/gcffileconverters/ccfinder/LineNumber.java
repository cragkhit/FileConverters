package uk.ac.ucl.gcffileconverters.ccfinder;

import java.io.*;
import java.util.StringTokenizer;

public class LineNumber {
    private String postfix;
    private String ccfxprepdir;

    public LineNumber(String postfix, String ccfxprepdir) {
        this.postfix = postfix;
        this.ccfxprepdir = ccfxprepdir;
    }

    /***
     * Get line numbers from reading ccfxprepdir
     * @param fname -- name of the clone file
     * @param n -- the selected nth line
     * @param prefix1 -- prefix of the path to remove
     * @param prefix2 -- prefix (2nd) of the path to remove
     * @return the line number
     * @throws IOException
     */
    int getLineNumber(String fname, int n, String prefix1, String prefix2) throws IOException {
        GenerateIntFileName gen = new GenerateIntFileName(postfix, ccfxprepdir);
        String target = gen.generate(fname);
        target = target.replace(prefix1, "").replace(prefix2, "");
        BufferedReader in = new BufferedReader(new FileReader(ccfxprepdir + "/" + target));
        int i = 0;
        int lineNumber = 0;
        String str;
        Convert con = new Convert();
        while (true) {
            str = in.readLine();
            if (str == null) {
                break;
            }
            if (i == n) {
                StringTokenizer st = new StringTokenizer(str, ".");
                lineNumber = con.conHexToDec(st.nextToken());
                break;
            }
            i++;
        }
        if(in!=null)
            in.close();
        return lineNumber;
    }
}
