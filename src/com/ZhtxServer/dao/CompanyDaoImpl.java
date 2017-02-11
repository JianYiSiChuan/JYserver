package com.ZhtxServer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ZhtxServer.cons.ConnectUtil;
import com.ZhtxServer.entity.Company;

//����company��Ĺ�����
public class CompanyDaoImpl {
    
	public Company getInfo(String comid){
		Connection conn=ConnectUtil.open("client_company");
    	String sql="select * from company where id=?";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, comid);
	    	ResultSet rs=pstmt.executeQuery();
	    	if (rs.next()){
	    		Company com=new Company();
	    		com.setId(comid);
	    		com.setName(rs.getString(3));
	    		com.setDb_name(rs.getString(2));
	    		com.setCreate_time(rs.getTimestamp(4));
	    		com.setDba_id(rs.getInt(5));
	    		com.setSign_time(rs.getTime(6));
	    		com.setSigndba_id(rs.getInt(7));
	    		return com;
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

	public List<Boolean> getWork(String db) {
		Connection conn=ConnectUtil.open(db);
    	String sql="select `status` from `workmenu`";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
	    	ResultSet rs=pstmt.executeQuery();
	    	List<Boolean> list=new ArrayList<Boolean>();
	    	while (rs.next()){
	    		list.add(rs.getBoolean(1));
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
