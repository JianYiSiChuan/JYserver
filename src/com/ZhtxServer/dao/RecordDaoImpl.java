package com.ZhtxServer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ZhtxServer.cons.CompanyType;
import com.ZhtxServer.cons.ConnectUtil;
import com.ZhtxServer.cons.Constant;

public class RecordDaoImpl {
	
	public boolean record(String db,String type,List<String> list){
		Connection conn=ConnectUtil.open(db);
		String table="record_"+type;
		PreparedStatement pstmt;
		Boolean b=false;
		for (String s:CompanyType.companyList)
		if (db.equals(s)){
			b=true;
			break;
		}
		//对于会发生同单号多record的企业
		if ((b)&&((table.equals("record_sell"))||(table.equals("record_cashsell"))||(table.equals("record_stock"))||(table.equals("record_transdebt")))){
			String sql1="select * from `"+table+"` where `单号`=? and `商品种类`=? ";
			if (conn!=null){
				try {
					pstmt = conn.prepareStatement(sql1);
					pstmt.setString(1, list.get(0));
					pstmt.setString(2, list.get(2));
					ResultSet rs= pstmt.executeQuery();
			    	if (rs.next()){
			    		return true;
			    	}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally{
					ConnectUtil.close(conn);
				}
		    }
		//对于不会发生同单号多record的企业
		}else{
			String sql1="select * from `"+table+"` where `单号`=?";
			if (conn!=null){
				try {
					pstmt = conn.prepareStatement(sql1);
					pstmt.setString(1, list.get(0));
					ResultSet rs= pstmt.executeQuery();
			    	if (rs.next()){
			    		return true;
			    	}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally{
					ConnectUtil.close(conn);
				}
		    }
		}
		
		conn=ConnectUtil.open(db);
		String sql="insert "+table+" values(null,CURRENT_TIMESTAMP";
		for (int i=0;i<list.size();i++){
			sql+=",?";
		}
		sql+=")";    
	    if (conn!=null){
			try {
				pstmt = conn.prepareStatement(sql);
				for (int i=0;i<list.size();i++){
						pstmt.setString(i+1, list.get(i));
				}
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

	public String query(String db, String proid, int act) {
		// TODO Auto-generated method stubConnection conn=ConnectUtil.open(db);
		String s="";
		Connection conn=ConnectUtil.open(db);
		PreparedStatement pstmt;
		List<String> fieldList=new ArrayList<String>();
		String sql="show fields from `record_"+Constant.action[act-1]+"`";
		if (conn!=null){
			try {
				pstmt = conn.prepareStatement(sql);
				ResultSet rs= pstmt.executeQuery();
		    	while (rs.next()){
		    		fieldList.add(rs.getString(1));
		    	}		
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		    sql="select * from `record_"+Constant.action[act-1]+"` where `单号`=?";
		    try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, proid);
				ResultSet rs= pstmt.executeQuery();
				//存贮数据，每一个记录由";"分开，没一行由"&"分开
		    	while (rs.next()){
		    		for (int i=2;i<fieldList.size();i++){
		    			s+=fieldList.get(i)+" : "+rs.getString(i+1)+"&";
		    		}
		    		s+=";";
		    	}		
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				ConnectUtil.close(conn);
			}
		}
		return s;
	}

	public boolean delete(String db, String type, String proid) {
		Connection conn=ConnectUtil.open(db);
		PreparedStatement pstmt;
		String sql="delete from `record_"+type+"` where `单号`=?";
		if (conn!=null){
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, proid);
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
