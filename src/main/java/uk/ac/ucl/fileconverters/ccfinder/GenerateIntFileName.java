package main.java.uk.ac.ucl.fileconverters.ccfinder;

public class GenerateIntFileName {
	private String postfix;
	private String baseName;
	private String ccfxprepdir;

	public GenerateIntFileName(String postFix, String ccfxprepdir) {
		this.postfix = postFix;
		this.baseName = "";
		this.ccfxprepdir = ccfxprepdir;
	}

	String generate(String str) {
		String target = str + postfix;
		// System.out.println(target);
		return target;
	}

	String change(String str, String substr) {
		str = str.replaceAll("\\\\", "/");
		substr = substr.replaceAll("\\\\", "/");
		String strsplit[] = str.split(substr);
		if (strsplit.length == 2) {
			String strtemp = strsplit[1].replaceAll("/", "\\\\");
			return strtemp;
		} else {
			str = str.replaceAll("/", "\\\\");
			return str;
		}
	}

}
