 package main.java.uk.ac.ucl.fileconverters.ccfinder;


public class Convert {
	int conHexToDec(String hex)
	{
		int n=0,j=0,digit=0;
		char ch[]=hex.toCharArray();
		for(int i=ch.length-1;i>=0;i--)
		{
			if(ch[i]=='a')
			{
				digit=10;
			}
			else if(ch[i]=='b')
			{
				digit=11;
			}
			else if(ch[i]=='c')
			{
				digit=12;
			}
			else if(ch[i]=='d')
			{
				digit=13;
			}
			else if(ch[i]=='e')
			{
				digit=14;
			}
			else if(ch[i]=='f')
			{
				digit=15;
			}
			else
			{
				digit=ch[i]-48;
			}
			
			n+=digit*Math.pow(16, (double)j);
			j++;
		}
		return n;
	}

}
