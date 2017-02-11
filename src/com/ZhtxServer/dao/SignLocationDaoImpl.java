package com.ZhtxServer.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ZhtxServer.cons.ConnectUtil;
import com.ZhtxServer.entity.SignLocation;

public class SignLocationDaoImpl {

	public boolean insert(String db,double latitude,double longitude,String describe,String time){
		Connection conn=ConnectUtil.open(db);
    	String sql="insert `sign_location`(`latitude`,`longitude`,`describe`,`time`) values(?,?,?,?)";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setDouble(1, latitude);
			pstmt.setDouble(2, longitude);
			pstmt.setString(3, describe);
			pstmt.setString(4, time);
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
	public boolean delete(String db,int id){
		Connection conn=ConnectUtil.open(db);
    	String sql="delete from `sign_location` where `id`=?";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
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
	public List<SignLocation> info(String db){
		Connection conn=ConnectUtil.open(db);
    	String sql="select * from `sign_location`";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
	    	ResultSet rs=pstmt.executeQuery();
	    	List<SignLocation> sl=new ArrayList<SignLocation>();
	    	while (rs.next()){
	    		SignLocation s=new SignLocation();
	    		s.setId(rs.getInt(1));
	    		s.setLatitude(rs.getDouble(2));
	    		s.setLongitude(rs.getDouble(3));
	    		s.setDescribe(rs.getString(4));
	    		s.setTime(rs.getString(5));
	    		sl.add(s);
	    	}
	    	return sl;
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
