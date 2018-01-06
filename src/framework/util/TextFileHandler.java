package framework.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TextFileHandler {
	
	private static final char[] intChars={'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','1','2','3','4','5','6','7','8','9','0','!','@','#','$','%','^','&','*','(',')',';','_','+','=','[',']','{','}','?','~'};
	
	public static void writeFile(String newFile, String filename) {
		try {
			BufferedWriter b=new BufferedWriter(new FileWriter(filename));
			b.write(newFile);
			b.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeFile(int[][] newFile, String filename) {
		String stringFile="";
		for (int i=0; i<newFile.length; i++) {
			for (int j=0; j<newFile[i].length; j++) {
				if (newFile[i][j]<0) {
					stringFile+=intChars[-newFile[i][j]];
					stringFile+='-';
				} else {
					stringFile+=intChars[newFile[i][j]];
				}
			}
			if (i<newFile.length-1) {
				stringFile+="/";
			}
		}
		writeFile(stringFile, filename);
	}
	
	public static String readFileLine(int line, String filename) {
		String result="";
		try {
			InputStream in=null;
			BufferedReader bReader;
			if (filename.equals("MapMakerOutput")) {
				bReader=new BufferedReader(new FileReader(filename));
			} else {
				in=TextFileHandler.class.getResourceAsStream("/GameData/"+filename);
				bReader=new BufferedReader(new InputStreamReader(in));
			}
			for (int i=0; i<line; i++) {
				bReader.readLine();
			}
			result=bReader.readLine();
			bReader.close();
			if (in!=null) {
				in.close();
			}
			if (result!=null&&result.length()>0&&result.charAt(0)=='*') {
				result=result.substring(1);
				result=result.substring(result.indexOf('*')+1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static int[][] readFileInt(int line, String filename) {
		String result=readFileLine(line,filename);
		if (result==null) {return new int[0][0];}
		String[] splitResult=result.split("/");
		int[][] ints=new int[splitResult.length][];
		for (int j=0; j<ints.length; j++) {
			ints[j]=new int[splitResult[j].length()];
			for (int i=0; i<splitResult[j].length(); i++) {
				ints[j][i]=getInt(splitResult[j].charAt(i));
				if (splitResult[j].charAt(Math.min(i+1, splitResult[j].length()-1))=='-') {
					ints[j][i]*=-1;
					splitResult[j]=splitResult[j].replaceFirst("-","");
				}
			}
		}
		return ints;
	}
	
	private static int getInt(char c) {
		for (int i=0; i<intChars.length; i++) {
			if (intChars[i]==c) {
				return i;
			}
		}
		return 0;
	}
}
