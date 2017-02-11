package com.ZhtxServer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ZhtxServer.cons.ConnectUtil;
import com.ZhtxServer.entity.User;

// 操作用户类的工具类
public class UserDaoImpl {
	
	//登陆的方法
    public User login(String username,String password,String phoneid){
    	Connection conn=ConnectUtil.open("client_company");
    	String sql="select * from user where username=? and password=? and (phoneid=? or ASCII(phoneid)=0 )";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
	    	pstmt.setString(2, password);
	    	pstmt.setString(3, phoneid);
	    	ResultSet rs=pstmt.executeQuery();
	    	if (rs.next()){
	    		if (rs.getInt(5)==0) return null;
	    		User user=new User();
	    		user.setId(rs.getInt(1));
	    		user.setUsername(username);
	    		user.setPassword(password);
	    		user.setCompany_id(rs.getString(4));
	    		user=getDbName(user);
	    		user.setDeviceId(rs.getString(7));
	    		return user;
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
    //取得数据库名方法
	private User getDbName(User user) {
		Connection conn=ConnectUtil.open("client_company");
    	String sql="select * from company where id=?";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user.getCompany_id()); 	
	    	ResultSet rs=pstmt.executeQuery();
	    	if (rs.next()){
	    		user.setDb(rs.getString(2));
	    		user.setCompany_name(rs.getString(3));
	    	}
	    	return user;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			ConnectUtil.close(conn);
		}
    	}
		return null;
	}
	//修改密码方法
	public boolean changePassword(int id,String password){
		Connection conn=ConnectUtil.open("client_company");
    	String sql="update user set password=? where id=?";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, password); 
			pstmt.setInt(2, id);
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
	
	//建立新用户
    public int create(String comid,String username,String password,String phoneid){
    	Connection conn=ConnectUtil.open("client_company");
    	String sql="insert user(username,password,company_id,`status`,phoneid) values(?,?,?,0,?)";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username); 
			pstmt.setString(2, password);
			pstmt.setString(3, comid);
			pstmt.setString(4, phoneid);
	    	int rs= pstmt.executeUpdate();
	    	if (rs==0) {return 0;}
	    	else {return getuserid(username);}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			ConnectUtil.close(conn);
		}
    	}
		return 0;
    }
    
    //查找新用户的id
	private int getuserid(String username) {
		Connection conn=ConnectUtil.open("client_company");
    	String sql="select `id` from user where username=?";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
	    	ResultSet rs=pstmt.executeQuery();
	    	if (rs.next()){
	    		return rs.getInt(1);
	    	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			ConnectUtil.close(conn);
		}
    	}
    	return 0;
	}
	
	//删除用户
	public boolean delete(int id){
		Connection conn=ConnectUtil.open("client_company");
    	String sql="delete from user where id=?";
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
	
	//设置用户通过审核
	public boolean pass(int id){
		Connection conn=ConnectUtil.open("client_company");
    	String sql="update user set `status`=1 where id=?";
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
	public boolean checkName(String username,String uid,String comid) {
		Connection conn=ConnectUtil.open("client_company");
    	String sql="select * from `user` where `username`=? or (`phoneid`=? and `company_id`=? and ASCII(phoneid)<>0) ";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username); 
			pstmt.setString(2, uid); 
			pstmt.setString(3,comid);
	    	ResultSet rs= pstmt.executeQuery();
	    	if (rs.next()){

	    		return false;
	    	}
	    	return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			ConnectUtil.close(conn);
		}
    	}
		return false;
	}
	public boolean refreshDeviceId(int id, String deviceId) {
		Connection conn=ConnectUtil.open("client_company");
    	String sql="update user set `deviceId`=? where id=?";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(2, id); 
			pstmt.setString(1, deviceId);
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
	public String getDeviceId(int id) {
		Connection conn=ConnectUtil.open("client_company");
    	String sql="select `deviceId` from `user` where `id`=? ";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id); 
	    	ResultSet rs= pstmt.executeQuery();
	    	if (rs.next()){
	    		return rs.getString(1);
	    	}
	    	return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			ConnectUtil.close(conn);
		}
    	}
		return null;
	}
	public boolean isRightPhonid(String username, String phoneid) {
		Connection conn=ConnectUtil.open("client_company");
    	String sql="select * from user where username=? and (phoneid=? or ASCII(phoneid)=0 )";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
	    	pstmt.setString(2, phoneid);
	    	ResultSet rs=pstmt.executeQuery();
	    	if (rs.next()){
	    		return false;
	    	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			ConnectUtil.close(conn);
		}
    	}
    	return true;
	}
}
