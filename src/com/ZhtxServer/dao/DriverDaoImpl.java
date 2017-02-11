package com.ZhtxServer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ZhtxServer.cons.ConnectUtil;
import com.ZhtxServer.entity.Driver;

public class DriverDaoImpl {
    public Driver info(String db,int id){
    	Connection conn=ConnectUtil.open(db);
    	String sql="select * from `driver` where `id`=?";
    	PreparedStatement pstmt;
    	if (conn!=null){
    		try {
    			pstmt = conn.prepareStatement(sql);
    			pstmt.setInt(1, id);
    	    	ResultSet rs=pstmt.executeQuery();
    	    	if (rs.next()){
    	    		Driver d=new Driver();
    	    		d.setId(id);
    	    		d.setName(rs.getString(2));
    	    		d.setPhone(rs.getString(3));
    	    		d.setRemark(rs.getString(4));
    	    		return d;
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
    
    public Driver info(String db,String platenum){
    	Connection conn=ConnectUtil.open(db);
    	String sql="select `driver_id` from `platenum` where `platenum`=?";
    	PreparedStatement pstmt;
    	int id=0;
    	if (conn!=null){
    	    try {
    			pstmt = conn.prepareStatement(sql);
    			pstmt.setString(1, platenum);
    	    	ResultSet rs=pstmt.executeQuery();
    	    	if (rs.next()){
    	    		id =rs.getInt(1);
    	    	}
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    			ConnectUtil.close(conn);
    		} 
    	}
    	if (id != 0){
    		return info(db,id);
        }else ConnectUtil.close(conn);
       return null;
    }
}
