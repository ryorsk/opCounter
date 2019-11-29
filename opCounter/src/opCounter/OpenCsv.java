package opCounter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

public class OpenCsv {
	public File openCsvFile( String fileName ) throws FileNotFoundException{
		File input = null;
		input = new File( fileName );
		return input;
	}
	
	public String[] readFirstLine( String fileName ) throws IOException{
		//FileReader inputFile = new FileReader(openCsvFile(fileName));
		BufferedReader lineBuffer = null;
		try {
			lineBuffer = new BufferedReader(new InputStreamReader(new FileInputStream(openCsvFile(fileName)),"SJIS"));
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "CSVが開けません：" + fileName);
			e.printStackTrace();
		}
		ArrayList<String> firstLine = new ArrayList<String>();
		
		//1行目だけ取得
		String line;
		StringTokenizer token = null;
		
		if( (line = lineBuffer.readLine()) != null ){
			token = new StringTokenizer(line, ",");
			while (token.hasMoreTokens()) {
				//System.out.println(token.nextToken());
				firstLine.add( token.nextToken() );
			}
		}
		
		lineBuffer.close();
		
		//ArrayList -> List
		String[] firstLineList = (String[])firstLine.toArray(new String[0]);
		return firstLineList;
	}
	
	public String[] readOperatorColumn( String fileName, int column ) throws IOException{
		BufferedReader lineBuffer = null;
		try {
			lineBuffer = new BufferedReader(new InputStreamReader(new FileInputStream(openCsvFile(fileName)),"SJIS"));
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			if( fileName == null ){
				JOptionPane.showMessageDialog(null, "CSVファイルを開いてください");
			}else{
				JOptionPane.showMessageDialog(null, "CSVが開けません：" + fileName);
			}
			e.printStackTrace();
		}
		ArrayList<String> OpColumn = new ArrayList<String>();
		
		String line;
		StringTokenizer token = null;
		while( (line = lineBuffer.readLine() ) != null) {
			int i = 0;
			token = new StringTokenizer(line, ",");
			while (token.hasMoreTokens()) {
				//System.out.println(token.nextToken());
				if( i == column ){
					OpColumn.add( token.nextToken() );
				}else {
					token.nextToken();
				}
				i++;
			}
		}
		
		lineBuffer.close();
		String[] OpColumnList = (String[])OpColumn.toArray(new String[0]);
		return OpColumnList;
	}
}
