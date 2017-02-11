package com.ZhtxServer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ZhtxServer.cons.ConnectUtil;
import com.ZhtxServer.entity.StepRight;

public class StepRightDaoImpl {
	public List<StepRight> allinfo(String db,String table){
		Connection conn=ConnectUtil.open(db);
        String sql="select * from "+table+" order by `step`";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			List<StepRight> list=new ArrayList<StepRight>();
	    	ResultSet rs= pstmt.executeQuery();
	    	while (rs.next()){
	    		StepRight re=new StepRight();
	    		re.setId(rs.getInt(1));
	    		re.setDescribe(rs.getString(2));
	    		re.setStaff(rs.getString(3));
	    		re.setBtn1(rs.getString(4));
	    		re.setBtn2(rs.getString(5));
	    		list.add(re);
	    	}
	    	return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			ConnectUtil.close(conn);
		}
    	}
		return null;
    }
	
	public boolean change(String db,String table,int stepid,String staff){
		Connection conn=ConnectUtil.open(db);
        String sql="update "+table+" set `staff`=? where `step`=?";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, staff);
			pstmt.setInt(2, stepid);
	    	int rs=pstmt.executeUpdate();
	    	if (rs==0){return false;}
	    	else{
	    		return true;
	    	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			ConnectUtil.close(conn);
		}
    	}
    	return false;
	}

	public StepRight info(String db, String table, int step) {
		Connection conn=ConnectUtil.open(db);
        String sql="select * from "+table+" where `step`=?";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, step);
			StepRight sr=new StepRight();
	    	ResultSet rs= pstmt.executeQuery();
	    	if (rs.next()){
	    		sr.setId(rs.getInt(1));
	    		sr.setDescribe(rs.getString(2));
	    		sr.setStaff(rs.getString(3));
	    		sr.setBtn1(rs.getString(4));
	    		sr.setBtn2(rs.getString(5));
	    	}
	    	return sr;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			ConnectUtil.close(conn);
		}
    	}
		return null;
	}
}
