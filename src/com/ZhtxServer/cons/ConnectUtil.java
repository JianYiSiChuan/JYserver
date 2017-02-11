package com.ZhtxServer.cons;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



//锟斤拷锟斤拷锟斤拷锟捷匡拷墓锟斤拷锟斤拷锟�
public class ConnectUtil {
	
	//锟斤拷锟斤拷锟斤拷锟接ｏ拷db锟斤拷要锟斤拷锟斤拷锟斤拷锟斤拷菘锟�
	public static Connection open(String db){
		    String url="jdbc:mysql://rdsn6204xw0q5ek630u5.mysql.rds.aliyuncs.com:3306/"
		 		+db
		 		+ "?useUnicode=true&characterEncoding=utf-8";
		    String driver="com.mysql.jdbc.Driver";
	        String username="zhtx";
	        String password="jianyi_542";
		    try {
				Class.forName(driver);
				return DriverManager.getConnection(url, username, password);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		return null;	
	}
    
	//锟截憋拷锟斤拷锟捷匡拷
    public static void close(Connection conn)  {
    	if (conn!=null){
    	try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			conn=null;
		}
    	}
    }
	
}
