package com.ZhtxServer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ZhtxServer.cons.ConnectUtil;
import com.ZhtxServer.entity.Staff;

//操作staff类的工具类
public class StaffDaoImpl {
	
	public boolean insert(String db,int id,String name,String id_card){
		Connection conn=ConnectUtil.open(db);
    	String sql="insert staff_personal_info (`id`,`name`,`id_card`) values(?,?,?)";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id); 
			pstmt.setString(2, name);
			pstmt.setString(3, id_card);
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
	
	public Staff getInfo(String db,int id){
		Connection conn=ConnectUtil.open(db);
    	String sql="select `id`, `name`, `id_card`,`bankid`,`platenum` from staff_personal_info where id=?";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
	    	ResultSet rs=pstmt.executeQuery();
	    	if (rs.next()){
	    		Staff staff=new Staff();
	    		staff.setId(id);
	    		staff.setName(rs.getString(2));
	    		staff.setId_card(rs.getString(3));
	    		staff.setBankid(rs.getString(4));
	    		staff.setPlatenum(rs.getString(5));
	    		return staff;
	    	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			ConnectUtil.close(conn);
		}
    	}
		return null;
	}

	public List<Staff> allinfo(String db) {
		Connection conn=ConnectUtil.open(db);
        String sql="select * from staff_personal_info ";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			List<Staff> list=new ArrayList<Staff>();
	    	ResultSet rs= pstmt.executeQuery();
	    	while (rs.next()){
	    		Staff s=new Staff();
	    		s.setId(rs.getInt(1));
	    		s.setName(rs.getString(2));
	    		s.setId_card(rs.getString(3));
	    		s.setBankid(rs.getString(4));
	    		s.setPlatenum(rs.getString(5));
	    		list.add(s);
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

	public boolean setinfo(String db, int id, String bankid, String platenum) {
		Connection conn=ConnectUtil.open(db);
    	String sql="update staff_personal_info set `bankid`=?,`platenum`=? where `id`=?";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bankid); 
			pstmt.setString(2, platenum);
			pstmt.setInt(3, id);
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

	public boolean delete(String db, int id) {
		Connection conn=ConnectUtil.open(db);
    	String sql="delete from `staff_personal_info` where `id`=?";
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

}
