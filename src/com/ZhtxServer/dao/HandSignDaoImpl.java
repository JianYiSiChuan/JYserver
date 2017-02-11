package com.ZhtxServer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ZhtxServer.cons.ConnectUtil;

public class HandSignDaoImpl {

	//得到当前月份的奇偶性
	private String month(){
		String s="";
		Date date=new Date();
		DateFormat sdf = new SimpleDateFormat("MM");
		if (Integer.parseInt(sdf.format(date)) % 2==0) {
			s="even";
		}else{
			s="odd";
		}
		return s;
	}
	//得到当前日数
	private String day(){
		String s="";
		Date date=new Date();
		DateFormat sdf = new SimpleDateFormat("dd");
	    return String.valueOf(Integer.parseInt(sdf.format(date)));
	}
	//那考勤名单中的成员全部加入签到表
	private boolean insertAll(String db){
		Connection conn=ConnectUtil.open(db);
		PreparedStatement pstmt;
		if (conn!=null){
			List<String> list=new ArrayList<String>();
			String sql="select `name` from `attendence_item`"; 
			try {
				pstmt = conn.prepareStatement(sql);
				ResultSet rs= pstmt.executeQuery();
		    	while (rs.next()){
		    		list.add(rs.getString(1));
		    	}
			} catch (SQLException e) {
				e.printStackTrace();
			} 
			for (String name:list){
				sql="insert into `attendence_"+month()+"`(`name`) values(?)"; 
				try {
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, name);
					int rs= pstmt.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				} 
			}
			ConnectUtil.close(conn);
			return true;
		}		
		return false;
	}
	
	//在考勤名单中插入
	public boolean insert(String db, String name) {
		Connection conn=ConnectUtil.open(db);
		PreparedStatement pstmt;
		if (conn!=null){
			String sql="insert into `attendence_item`(`name`) values (?)";
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, name);
				int rs= pstmt.executeUpdate();
		    	if (rs==0) {
		    		ConnectUtil.close(conn);
		    		return false;
		    	}
			} catch (SQLException e) {
				e.printStackTrace();
			} 
			//判断当前签到表的状态，如果签到表是空的，说明这个月刚开始，首先把所有名单上的成员都加入签到表，如果不为空，则只加入新的这个成员
		    boolean empty=false;
			sql="select count(*) from `attendence_"+month()+"`"; 
			try {
				pstmt = conn.prepareStatement(sql);
				ResultSet rs= pstmt.executeQuery();
		    	if (rs.next()) {
                    if (rs.getInt(1)==0) empty=true;
		    	}else{
		    		ConnectUtil.close(conn);
		    		return false;
		    	}
			} catch (SQLException e) {
				e.printStackTrace();
			} 
			
			if (empty) {
				ConnectUtil.close(conn);
				return insertAll(db);
			}
			else{
				//查询表中是否已经有同样名字的成员，有则不插入
				sql="select `name` from `attendence_"+month()+"` where `name`=?"; 
				try {
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1,name);
					ResultSet rs= pstmt.executeQuery();
			    	if (rs.next()) {
			    		ConnectUtil.close(conn);
						return true;
			    	}
				} catch (SQLException e) {
					e.printStackTrace();
				} 
				sql="insert into `attendence_"+month()+"`(`name`) values(?)"; 
				try {
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, name);
					int rs= pstmt.executeUpdate();
			    	if (rs>0) {
			    		return true;
			    	}else{
			    		return false;
			    	}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally{
					ConnectUtil.close(conn);
				}
			}
	    }
		return false;
	}

	public boolean delete(String db, String name) {
		Connection conn=ConnectUtil.open(db);
		PreparedStatement pstmt;
		if (conn!=null){
			String sql="delete from `attendence_item` where `name`=?";
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, name);
				int rs= pstmt.executeUpdate();
		    	if (rs>0) {
		    		return true;
		    	}else{
		    		return false;
		    	}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally{
				ConnectUtil.close(conn);
			}
		}
		return false;
	}

	public List<String> signitem(String db) {
		Connection conn=ConnectUtil.open(db);
		PreparedStatement pstmt;
		if (conn!=null){
			String sql="select `name` from `attendence_item`";
			try {
				pstmt = conn.prepareStatement(sql);
				ResultSet rs= pstmt.executeQuery();
		    	List<String> list=new ArrayList<String>();
		    	while (rs.next()){
		    		list.add(rs.getString(1));
		    	}
		    	return list;
			} catch (SQLException e) {
				e.printStackTrace();
			} finally{
				ConnectUtil.close(conn);
			}
		}
		return null;
	}

	public List<String> signlist(String db) {
		Connection conn=ConnectUtil.open(db);
		//判断当前签到表的状态，如果签到表是空的，说明这个月刚开始，首先把所有名单上的成员都加入签到表
	    boolean empty=false;
	    List<String> rlist=new ArrayList<String>();
		String sql="select count(*) from `attendence_"+month()+"`"; 
		PreparedStatement pstmt;
		if (conn!=null){
			try {
				pstmt = conn.prepareStatement(sql);
				ResultSet rs= pstmt.executeQuery();
		    	if (rs.next()) {
	                if (rs.getInt(1)==0) empty=true;
		    	}else{
		    		ConnectUtil.close(conn);
		    		return null;
		    	}
			} catch (SQLException e) {
				e.printStackTrace();
			} 
			if (empty) {
			    insertAll(db);
			}
			List<String> list=new ArrayList<String>();
			sql="select `name` from `attendence_item`"; 
			try {
				pstmt = conn.prepareStatement(sql);
				ResultSet rs= pstmt.executeQuery();
		    	while (rs.next()){
		    		list.add(rs.getString(1));
		    	}
			} catch (SQLException e) {
				e.printStackTrace();
			} 
			for (String name:list){
				sql="select `day"+day()+"` from `attendence_"+month()+"` where `name`=?"; 
				try {
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, name);
					ResultSet rs= pstmt.executeQuery();
					if (rs.next()){
						rlist.add(name+"&"+String.valueOf(rs.getInt(1)));
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				} 
			}
			ConnectUtil.close(conn);
			return rlist;
		}
		return null;
	}
	
	//得到签到表中指定员工的考勤状态
	public int getStatus(String db,String name){
		Connection conn=ConnectUtil.open(db);
		int r=-1;
		//判断当前签到表的状态，如果签到表是空的，说明这个月刚开始，首先把所有名单上的成员都加入签到表
	    boolean empty=false;
	    List<String> rlist=new ArrayList<String>();
		String sql="select count(*) from `attendence_"+month()+"`"; 
		PreparedStatement pstmt;
		if (conn!=null){
			try {
				pstmt = conn.prepareStatement(sql);
				ResultSet rs= pstmt.executeQuery();
		    	if (rs.next()) {
	                if (rs.getInt(1)==0) empty=true;
		    	}
			} catch (SQLException e) {
				e.printStackTrace();
			} 
			if (empty) {
			    insertAll(db);
			}
			sql="select `day"+day()+"` from `attendence_"+month()+"` where `name`=?"; 
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, name);
				ResultSet rs= pstmt.executeQuery();
				if (rs.next()){
					r=rs.getInt(1);
				}	
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				ConnectUtil.close(conn);
			}		
		}
		return r;
	}
	//得到签到表中指定员工的考勤状态
	public int getStatus(String db,int userid){
		Connection conn=ConnectUtil.open(db);
		int r=-1;
		//判断当前签到表的状态，如果签到表是空的，说明这个月刚开始，首先把所有名单上的成员都加入签到表
	    boolean empty=false;
	    List<String> rlist=new ArrayList<String>();
		String sql="select count(*) from `attendence_"+month()+"`"; 
		PreparedStatement pstmt;
		if (conn!=null){
			try {
				pstmt = conn.prepareStatement(sql);
				ResultSet rs= pstmt.executeQuery();
		    	if (rs.next()) {
	                if (rs.getInt(1)==0) empty=true;
		    	}
			} catch (SQLException e) {
				e.printStackTrace();
			} 
			if (empty) {
			    insertAll(db);
			}
			String name="";
			sql="select `name` from `staff_personal_info` where `id`=?"; 
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, userid);
				ResultSet rs= pstmt.executeQuery();
				if (rs.next()){
					name=rs.getString(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			sql="select `day"+day()+"` from `attendence_"+month()+"` where `name`=?"; 
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, name);
				ResultSet rs= pstmt.executeQuery();
				if (rs.next()){
					r=rs.getInt(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				ConnectUtil.close(conn);
			}
		}
		return r;
	}	
	public boolean sign(String db, String name, int status) {
		Connection conn=ConnectUtil.open(db);
		PreparedStatement pstmt;
		if (conn!=null){
			String sql="update `attendence_"+month()+"` set `day"+day()+"`=? where `name`=?";
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, status);
				pstmt.setString(2, name);
				int rs= pstmt.executeUpdate();
		    	if (rs>0){
		    		return true;
		    	}else{
		    		return false;
		    	}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally{
				ConnectUtil.close(conn);
			}
		}
		return false;
	}

	
}
