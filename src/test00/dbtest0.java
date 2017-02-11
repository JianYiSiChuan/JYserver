package test00;

import java.util.ArrayList;

import com.ZhtxServer.cons.CSVFileUtil;

public class dbtest0 {


	public static void main(String[] args) {
		try {
			CSVFileUtil u=new CSVFileUtil("E:\\javaweb\\record_sell.csv");
			String l=u.readLine();
			while (l!=null){
				ArrayList<String> list=u.fromCSVLinetoArray(l);
			    for (String s:list){
			    	System.out.print(s+" / ");
			    }
			    System.out.println();
				l=u.readLine();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
