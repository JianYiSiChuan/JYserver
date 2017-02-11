package com.ZhtxServer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ZhtxServer.cons.ConnectUtil;
import com.ZhtxServer.entity.StepDetail;

public class StepDetailDaoImpl {

	public List<StepDetail> info(String db, String table) {
		Connection conn=ConnectUtil.open(db);
    	String sql="select * from "+table+" order by `id`";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			List<StepDetail> list=new ArrayList<StepDetail>();
	    	ResultSet rs= pstmt.executeQuery();
	    	while (rs.next()){
	    		StepDetail sd=new StepDetail();
	    		sd.setId(rs.getInt(1));
	    		sd.setType_1(rs.getString(2));
	    		sd.setType_2(rs.getString(3));
	    		sd.setIsnew(rs.getInt(4));
	    		sd.setRecord(rs.getString(5));
	    		list.add(sd);
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
