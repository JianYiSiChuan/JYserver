package com.ZhtxServer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import com.ZhtxServer.cons.ConnectUtil;

public class BackGroundDaoImpl {

	public boolean Typein_Begin(String db, String class1, String class2, java.sql.Date date, String num, String proid,
			String debt) {
		Connection conn=ConnectUtil.open(db);
		PreparedStatement pstmt;
		String sql="insert `account_real`(`会计科目`,`class_2`,`时间`,`金额`,`原始单号`,`借贷方`) values(?,?,?,?,?,?)";  	
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, class1); 
			pstmt.setString(2, class2);
			pstmt.setDate(3, date);
			pstmt.setString(4, num);
			pstmt.setString(5, proid);
			pstmt.setString(6, debt);
	    	int rs= pstmt.executeUpdate();
	    	if (rs==0) {return false;}
	    	else {return true;}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			ConnectUtil.close(conn);
		}
    	}
		return false;
	}

}
