 package fileconverters.ccfinder;


public class generateIntFileName {
        private String postfix;
        private String baseName;
        private String ccfxprepdir;

        public generateIntFileName(String postFix, String ccfxprepdir){
            this.postfix = postFix;
            this.baseName = "";
            this.ccfxprepdir = ccfxprepdir;
        }
	String generate(String str)
	{
        //    System.out.println(str);/////////////////////////////////////////
          //  System.out.println("------------------------");
	/*	char address[]=str.toCharArray();
		
		String base="",rest="";
		int i=0,count=0;
		while(i<address.length)
		{
			if(address[i]=='\\' || address[i]=='/')
			{
				count++;
                                char s[] = System.getProperty("file.separator").toCharArray();
			        address[i]= s[0];

				
			}
			//base=base+address[i];
			if(count>1)
			{
				rest=rest+address[i];
			}
			i++;
		}
		//System.out.println(base);//////////////////////////
		// This is the previousbase=Parameter.BASE;
                base = this.ccfxprepdir;
	
		String target="";
		
                char t[]= System.getProperty("file.separator").toCharArray();
                String temp = rest.substring(1);

                
                target = base+ temp.substring(temp.indexOf(t[0]))+postfix;*/
                String target=str+postfix;


	//	System.out.println(target);////
          //      System.out.println("t------------------------");
		return target;
	}
	
	/*String change(String str)
	{
		char address[]=str.toCharArray();
		
		String base="",rest="";
		int i=0,count=0;
		while(i<address.length)
		{
			if(address[i]=='\\')
			{
				count++;
				address[i]='/';
				
			}
			//base=base+address[i];
			if(count>1)
			{
				rest=rest+address[i];
			}
			i++;
		}
		//System.out.println(base);
		//base="ArgoUML-0.32.BETA_2-src";
	
		String target="";
		//if(Parameter.LANGUAGE.equals("java"))
		//target =base+rest;
                target =rest;

		//else if (Parameter.LANGUAGE.equals("cpp"))
		
		//target =base+".ccfxprepdir\\"+rest+".cpp.2_0_0_2.default.ccfxprep";
			//target =base+".ccfxprepdir\\"+rest+".csharp.2_0_0_0.default.ccfxprep";
		//System.out.println(target);
		return target;
	}*/
        ///////////////////wtt////////////////////////////////////
	String change(String str,String substr)///////////wtt//////////////
	{
	// System.out.println("invocake");
        str= str.replaceAll("\\\\","/");
        substr= substr.replaceAll("\\\\","/");        
          //      System.out.println(str); 
            //   System.out.println(substr);
               
               
            String strsplit[]=str.split(substr);
          
            
            //System.out.println(strsplit[0]);
            
            if(strsplit.length==2)
            {
                String strtemp=strsplit[1].replaceAll("/","\\\\");
              return strtemp;
            }
            else
            {
                str=str.replaceAll("/","\\\\");
                return str;
            }
        }
          //////////////////////////////////////////////////////////////



}
	