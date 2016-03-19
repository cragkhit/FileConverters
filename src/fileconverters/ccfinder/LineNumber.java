package fileconverters.ccfinder;

import java.io.*;
import java.util.StringTokenizer;

public class LineNumber {
    private String postfix;
    private String ccfxprepdir;

    public LineNumber(String postfix, String ccfxprepdir) {
        this.postfix = postfix;
        this.ccfxprepdir = ccfxprepdir;
    }

    int getLineNumber(String fname, int n) throws IOException {
    	// System.out.println("fname = " + fname);
        GenerateIntFileName gen = new GenerateIntFileName(postfix, ccfxprepdir);
        String target = gen.generate(fname);

        BufferedReader in = new BufferedReader(new FileReader(target));
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
