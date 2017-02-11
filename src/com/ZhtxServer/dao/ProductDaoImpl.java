package com.ZhtxServer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ZhtxServer.cons.ConnectUtil;
import com.ZhtxServer.entity.Product;

public class ProductDaoImpl {

	public int insert(String db,String table, String name, String type, double price,String client,int account) {
		Connection conn=ConnectUtil.open(db);
    	String sql="insert `product_"+table+"`(`name`,`type`,`price`,`client`,`account`) values(?,?,?,?,?)";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name); 
			pstmt.setString(2, type);
			pstmt.setDouble(3, price);
			pstmt.setString(4, client);
			if (account==0) pstmt.setBoolean(5, false);
			else pstmt.setBoolean(5, true);
	    	int rs= pstmt.executeUpdate();
	    	if (rs==0) {return 0;}
	    	else {
	    		String sql2="select `id` from `product_"+table+" where `name`=? and `type`=? and `client`=? and account=?";
	    		pstmt = conn.prepareStatement(sql2);
	    		pstmt.setString(1, name); 
				pstmt.setString(2, type);
				pstmt.setString(3, client);
				if (account==0) pstmt.setBoolean(4, false);
				else pstmt.setBoolean(4, true);
				ResultSet rs2=pstmt.executeQuery();
				if (rs2.next())	{
					return rs2.getInt(1);
				}else{
					return 0;
				}
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

	public List<Product> allinfo(String db,String table,int x) {
		Connection conn=ConnectUtil.open(db);
    	String sql="";
    	sql="select `id`,`name`,`type`,`price`,`client`,`account` from `product_"+table+"` order by `client`,`name`";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			List<Product> list=new ArrayList<Product>();
	    	ResultSet rs= pstmt.executeQuery();
	    	while (rs.next()){
	    		Product pr=new Product();
	    		pr.setId(rs.getInt(1));
	    		pr.setName(rs.getString(2));
	    		pr.setType(rs.getString(3));
	    		pr.setPrice(rs.getDouble(4));
	    		pr.setClient(rs.getString(5));
	    		pr.setAccount(rs.getBoolean(6));
	    		list.add(pr);
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

	public boolean delete(String db, String table,int id) {
		Connection conn=ConnectUtil.open(db);
    	String sql="delete from `product_"+table+"` where id=? ";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,id);
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

	public boolean change(String db,String table, int id, String name, String type, double price,String client,int account) {
		Connection conn=ConnectUtil.open(db);
    	String sql="update `product_"+table+"` set name=?,type=?,price=?,client=?,account=? where id=? ";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, type);
			pstmt.setDouble(3, price);
			pstmt.setString(4, client);
			if (account==0) pstmt.setBoolean(5, false);
			else pstmt.setBoolean(5, true);
			pstmt.setInt(6,id);
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

	public boolean addp(String db, String content) {
		Connection conn=ConnectUtil.open(db);
    	String sql="insert `provider`(`content`) values(?)";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, content); 
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

	public boolean deletep(String db, String content) {
		Connection conn=ConnectUtil.open(db);
    	String sql="delete from `provider` where `content`=? ";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,content);
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

	public List<String> infop(String db) {
		Connection conn=ConnectUtil.open(db);
    	String sql="select `content` from `provider`";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			List<String> list=new ArrayList<String>();
	    	ResultSet rs= pstmt.executeQuery();
	    	while (rs.next()){
	    		list.add(rs.getString(1));
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
	

}
