package com.ZhtxServer.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ZhtxServer.cons.ConnectUtil;
import com.ZhtxServer.cons.Constant;
import com.ZhtxServer.cons.UtilForRequire;
import com.ZhtxServer.entity.AccountType;
import com.ZhtxServer.entity.Platenum;
import com.ZhtxServer.entity.Require;
import com.ZhtxServer.entity.RequireIOS;
import com.ZhtxServer.entity.RequireStatus;
import com.ZhtxServer.table.Account_stock_balance;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.push.model.v20150827.PushNoticeToAndroidRequest;
import com.aliyuncs.push.model.v20150827.PushNoticeToAndroidResponse;
import com.aliyuncs.push.model.v20150827.PushNoticeToiOSRequest;
import com.aliyuncs.push.model.v20150827.PushNoticeToiOSResponse;
import com.aliyuncs.push.model.v20150827.PushRequest;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

//操作require类的工具类
public class RequireDaoImpl {

	public boolean insert(String db,int sender,int reciver,int previd,int action,String proid,String data){
		Connection conn=ConnectUtil.open(db);
		PreparedStatement pstmt;
		//防止一条记录因为网络原因重复插入
		String sql="select * from `require` where `proid`=? and `action`=? and `data`=? and `status`=0";
		if (conn!=null){
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, proid);
				pstmt.setInt(2, action);
				pstmt.setString(3, data);
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
		
		//执行插入操作
	    conn=ConnectUtil.open(db);
    	sql="insert `require`(sender,reciver,previd,`action`,`proid`,`data`,`status`) values(?,?,?,?,?,?,0)";  	
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, sender); 
			pstmt.setInt(2, reciver);
			pstmt.setInt(3, previd);
			pstmt.setInt(4, action);
			pstmt.setString(5, proid);
			pstmt.setString(6, data);
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
	
	public int checkCount(String db,int userid,int act,String filter){
		Connection conn=ConnectUtil.open(db);
		String sql="";
    	if (act !=2) {
    		sql="select count(*) from `require` where locate(?,`proid`) and (MOD(`action`,100)=?)"
    				+ " and ((reciver=? and `status`=0) or (sender=? and (`status`=1 or `status`=2)))";
    	}else{
    		sql="select count(*) from `require` where (`action`=3 or `action`=?) "
    				+ "and ((reciver=? and `status`=0) or (sender=? and (`status`=1 or `status`=2)))";
    	}
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			if (act!=2){
				pstmt.setString(1, filter);
				pstmt.setInt(2, act); 
				pstmt.setInt(3, userid);
				pstmt.setInt(4, userid);
			}else{
				pstmt.setInt(1, act); 
				pstmt.setInt(2, userid);
				pstmt.setInt(3, userid);
			}	
	    	ResultSet rs= pstmt.executeQuery();
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
		return -1;
    }
	
	public List<Require> checkPage(String db,int userid,int act,int page, String filter){
		Connection conn=ConnectUtil.open(db);
		String sql="";
		if (act !=2) {
    		sql="select * from `require` where locate(?,`proid`) and (MOD(`action`,100)=?)"
    				+ " and ((reciver=? and `status`=0) or (sender=? and (`status`=1 or `status`=2))) "
    				+ "order by `id` desc limit ?,10";
    	}else{
    		sql="select * from `require` where (`action`=3 or `action`=?) "
    				+ "and ((reciver=? and `status`=0) or (sender=? and (`status`=1 or `status`=2))) "
    				+ "order by `id` desc limit ?,10";
    	}
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			if (act!=2){
				pstmt.setString(1, filter);
				pstmt.setInt(2, act); 
				pstmt.setInt(3, userid);
				pstmt.setInt(4, userid);
				pstmt.setInt(5, page*10-10);
			}else{
				pstmt.setInt(1, act); 
				pstmt.setInt(2, userid);
				pstmt.setInt(3, userid);
				pstmt.setInt(4, page*10-10);
			}	
			List<Require> list=new ArrayList<Require>();
	    	ResultSet rs= pstmt.executeQuery();
	    	while (rs.next()){
	    		Require re=new Require();
	    		re.setId(rs.getInt(1));
	    		re.setSender(rs.getInt(2));
	    		re.setReciver(rs.getInt(3));
	    		re.setPrevid(rs.getInt(4));
	    		re.setAction(rs.getInt(5));
	    		re.setData(rs.getString(6));
	    		re.setStatus(rs.getInt(7));
	    		re.setTime(rs.getTimestamp(8));
	    		re.setProid(rs.getString(9));
	    		if (!rs.getBoolean(10)) {
	    			setRead(db,re.getId());
	    		}
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
	
	public List<RequireIOS> checkIOS(String db,int userid,int act){
		Connection conn=ConnectUtil.open(db);
		String sql="";
    	if (act !=2) {
    		sql="select * from `require` where (MOD(`action`,100)=?) and ((reciver=? and `status`=0) or (sender=? and (`status`=1 or `status`=2))) order by `id` desc";
    	}else{
    		sql="select * from `require` where (`action`=3 or `action`=?) and ((reciver=? and `status`=0) or (sender=? and (`status`=1 or `status`=2))) order by `id` desc";
    	}
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, act); 
			pstmt.setInt(2, userid);
			pstmt.setInt(3, userid);
			List<RequireIOS> list=new ArrayList<RequireIOS>();
	    	ResultSet rs= pstmt.executeQuery();
	    	while (rs.next()){
	    		RequireIOS re=new RequireIOS();
	    		re.setId(rs.getInt(1));
	    		re.setSender(rs.getInt(2));
	    		re.setReciver(rs.getInt(3));
	    		re.setPrevid(rs.getInt(4));
	    		re.setAction(rs.getInt(5));
	    		re.setData(rs.getString(6));
	    		re.setStatus(rs.getInt(7));
	    		re.setTime(String.valueOf(rs.getTimestamp(8).getTime()));
	    		re.setProid(rs.getString(9));
	    		if (!rs.getBoolean(10)) {
	    			setRead(db,re.getId());
	    		}
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
	
	public List<RequireIOS> checkPageIOS(String db,int userid,int act,int page, String filter){
		Connection conn=ConnectUtil.open(db);
		String sql="";
		if (act !=2) {
    		sql="select * from `require` where locate(?,`proid`) and (MOD(`action`,100)=?)"
    				+ " and ((reciver=? and `status`=0) or (sender=? and (`status`=1 or `status`=2))) "
    				+ "order by `id` desc limit ?,10";
    	}else{
    		sql="select * from `require` where (`action`=3 or `action`=?) "
    				+ "and ((reciver=? and `status`=0) or (sender=? and (`status`=1 or `status`=2))) "
    				+ "order by `id` desc limit ?,10";
    	}
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			if (act!=2){
				pstmt.setString(1, filter);
				pstmt.setInt(2, act); 
				pstmt.setInt(3, userid);
				pstmt.setInt(4, userid);
				pstmt.setInt(5, page*10-10);
			}else{
				pstmt.setInt(1, act); 
				pstmt.setInt(2, userid);
				pstmt.setInt(3, userid);
				pstmt.setInt(4, page*10-10);
			}	
			List<RequireIOS> list=new ArrayList<RequireIOS>();
	    	ResultSet rs= pstmt.executeQuery();
	    	while (rs.next()){
	    		RequireIOS re=new RequireIOS();
	    		re.setId(rs.getInt(1));
	    		re.setSender(rs.getInt(2));
	    		re.setReciver(rs.getInt(3));
	    		re.setPrevid(rs.getInt(4));
	    		re.setAction(rs.getInt(5));
	    		re.setData(rs.getString(6));
	    		re.setStatus(rs.getInt(7));
	    		re.setTime(String.valueOf(rs.getTimestamp(8).getTime()));
	    		re.setProid(rs.getString(9));
	    		if (!rs.getBoolean(10)) {
	    			setRead(db,re.getId());
	    		}
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
	
	public boolean setStatus(String db,int requireid,int status){
		Connection conn=ConnectUtil.open(db);
		int x;
		if (status==3) x=1;
		else x=0;	
    	String sql="update `require` set `status`=?,`new`=? where id=?";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, status); 
			pstmt.setInt(2, x);
			pstmt.setInt(3, requireid);
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
	public boolean setStatus(String db,int requireid,int status,String info){
		Connection conn=ConnectUtil.open(db);
    	String sql="update `require` set `status`=?,`data`=?,`new`=0 where id=?";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, status); 
			pstmt.setString(2, info);
			pstmt.setInt(3, requireid);
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
	
	public boolean setStatus(String db, String proid, int act, int i) {
		Connection conn=ConnectUtil.open(db);
    	String sql="update `require` set `status`=? where `proid`=? and `action`=?";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, i);
			pstmt.setString(2, proid); 
			pstmt.setInt(3, act);		
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
	
	public List<String> printinfo(String db, String id) {
		Connection conn=ConnectUtil.open(db);
    	String sql="select `content` from print_"+id;
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
	
	public String che(String db,int userid){
		Connection conn=ConnectUtil.open(db);
		String sql="select * from `require` where  ((`new`=0)and((reciver=? and `status`=0) or (sender=? and (`status`=1 or `status`=2)))) order by `id` desc";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setInt(1, userid);
			pstmt.setInt(2, userid);
			List<Require> list=new ArrayList<Require>();
	    	ResultSet rs= pstmt.executeQuery();
	    	String s="";
	    	while (rs.next()){
                setRead(db,rs.getInt(1));
	    		s+=String.valueOf(rs.getInt(5))+"+";
	    	}
	    	return s;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			ConnectUtil.close(conn);
		}
    	}
		return "";
    }
	
	public String getnum(String db,int userid){
		Connection conn=ConnectUtil.open(db);
		String sql="select * from `require` where  ((reciver=? and `status`=0) or (sender=? and (`status`=1 or `status`=2))) order by `id` desc";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setInt(1, userid);
			pstmt.setInt(2, userid);
			List<Require> list=new ArrayList<Require>();
	    	ResultSet rs= pstmt.executeQuery();
	    	String s="";
	    	while (rs.next()){
                if (rs.getInt(10)==0) setRead(db,rs.getInt(1));
	    		s+=String.valueOf(rs.getInt(5))+"+";
	    	}
	    	return s;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			ConnectUtil.close(conn);
		}
    	}
		return "";
    }
	
	public boolean setRead(String db, int requireid) {
		Connection conn=ConnectUtil.open(db);
    	String sql="update `require` set `new`=1 where id=?";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, requireid);
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

	public List<AccountType> getAccountType(String db) {
		Connection conn=ConnectUtil.open(db);
    	String sql="select * from `costitem` order by `cost_1`,`cost_2`";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
	    	ResultSet rs=pstmt.executeQuery();
	    	List<AccountType> list=new ArrayList<AccountType>();
	    	while (rs.next()){
	    		
	    		AccountType at=new AccountType();
	    		at.setId(rs.getInt(1));
	    		at.setClass1(rs.getString(2));
	    		at.setClass2(rs.getString(3));
	    		list.add(at);
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

	public List<Platenum> getAct55(String db) {
		Connection conn=ConnectUtil.open(db);
    	String sql="select * from `platenum` ";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
	    	ResultSet rs=pstmt.executeQuery();
	    	List<Platenum> list=new ArrayList<Platenum>();
	    	while (rs.next()){
	    		Platenum pn=new Platenum();
	    		pn.setId(rs.getInt(1));
	    		pn.setPlatenum(rs.getString(2));
	    		pn.setDriver(rs.getInt(3));
	    		list.add(pn);
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
	
	public List<String> getAct81(String db) {
		Connection conn=ConnectUtil.open(db);
    	String sql="SELECT `content` FROM `driver` union SELECT DISTINCT(`client`) "
    			+ " FROM `product_stock` union SELECT `name` from `attendence_item` "
    			+ "union SELECT `content` from `provider` ";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
	    	ResultSet rs=pstmt.executeQuery();
	    	List<String> list=new ArrayList<String>();
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
	public List<String> getAct81(String db, String mode) {
		Connection conn=ConnectUtil.open(db);
    	String sql=UtilForRequire.payItem(mode);
    	if (sql==null) return null;
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
	    	ResultSet rs=pstmt.executeQuery();
	    	List<String> list=new ArrayList<String>();
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
	public List<String> getAct91(String db) {
		Connection conn=ConnectUtil.open(db);
    	String sql="SELECT DISTINCT(`client`)  FROM `product_sell`  ";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
	    	ResultSet rs=pstmt.executeQuery();
	    	List<String> list=new ArrayList<String>();
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

	public List<String> getAct91(String db, String mode) {
		Connection conn=ConnectUtil.open(db);
    	String sql=UtilForRequire.collectItem(mode);
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
	    	ResultSet rs=pstmt.executeQuery();
	    	List<String> list=new ArrayList<String>();
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

	public List<String> getAct82(String db) {
		Connection conn=ConnectUtil.open(db);
    	String sql="SELECT `content`  FROM `payway`";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
	    	ResultSet rs=pstmt.executeQuery();
	    	List<String> list=new ArrayList<String>();
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

	

	public int mrequireCount(String db, int userid,String filter) {
		Connection conn=ConnectUtil.open(db);
		String sql="select count(*) from `require` where (`sender`=?) and (MOD(`action`,100)>3) "
	    		+ " and (`action`<300) and (`status`<> 4) and locate(?,`proid`)"; 
    	PreparedStatement pstmt;
    	if (conn!=null){
    		try {
    			pstmt = conn.prepareStatement(sql);
    			pstmt.setInt(1, userid);
    			pstmt.setString(2,filter);
    	    	ResultSet rs= pstmt.executeQuery();
    	    	if (rs.next()){
    	    		return rs.getInt(1);  	    		
    	    	}
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}    
		return 0;
	}
	
	public List<RequireStatus> mrequirePage(String db, int userid, int page,String filter) {
		Connection conn=ConnectUtil.open(db);
		String sql="select * from `require` where (`sender`=?) and (MOD(`action`,100)>3)"
	    			+ " and (`action`<300) and (`status`<> 4) and locate(?,`proid`) order by `id` desc limit ?,10 "; 
		List<String> proidList=new ArrayList<String>();
		List<Integer> actList=new ArrayList<Integer>();
		List<Timestamp> timeList=new ArrayList<Timestamp>();
		List<RequireStatus> List=new ArrayList<RequireStatus>();
    	PreparedStatement pstmt;
    	if (conn!=null){
    		try {
    			pstmt = conn.prepareStatement(sql);
    			pstmt.setInt(1, userid); 
    			pstmt.setString(2, filter);
    			pstmt.setInt(3, (page-1)*10);
    			
    	    	ResultSet rs= pstmt.executeQuery();
    	    	while (rs.next()){
    	    			String s=rs.getString(9);
        	    		Integer i=rs.getInt(5) % 100;
        	    		proidList.add(s);
        	    		actList.add(i);
        	    		timeList.add(rs.getTimestamp(8));	
    	    	}
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		//之后查询每个单号是否在record中有记录，若有则为已完成，若没有则为进行中
    		for (int i=0;i<proidList.size();i++){
    			int act=actList.get(i);
    			String proid=proidList.get(i),table="record_";
    		    table+=Constant.action[act-1];
    		    String sql2="select * from `"+table+"` where `单号`="+proid;
    		    PreparedStatement pstmt2;
    			try {
    				pstmt2 = conn.prepareStatement(sql2);	
    		    	ResultSet rs= pstmt2.executeQuery();
    		    	if (rs.next()){
    		    		RequireStatus r=new RequireStatus();
    		    		r.setStatus(4);
    		    		r.setProid(proid);
    		    		r.setAct(act);
    		    		r.setTime(timeList.get(i));
    		    		List.add(r);
    		    	}else{
    		    		RequireStatus r=new RequireStatus();
    		    		r.setStatus(3);
    		    		r.setProid(proid);
    		    		r.setAct(act);
    		    		r.setTime(timeList.get(i));
    		    		List.add(r);
    		    	}
    			} catch (SQLException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    	    	}finally{
    	    		if (i==proidList.size()-1){
    	    		
    	    			ConnectUtil.close(conn);
    	    			
    	    		}
    	    	}
    		}
    		return List;
    	}    
		return null;
	}
	
	public List<RequireStatusIOS> mrequireIOS(String db, int userid, int day) {
		Connection conn=ConnectUtil.open(db);
		Date date=new Date();
		int t=0;
		//判断是查询一天内，还是一周内
		if (day==1) t=(int)(date.getTime()/1000)-86400-8*3600;
		else  t=(int)(date.getTime()/1000)-86400*31-8*3600;
		
		//首先查找时间段内用户发起的所有审批单号及其类型
		String sql="select * from `require` where (`sender`=?) and (MOD(`action`,100)>3)"
	    			+ " and (`action`<300) and (UNIX_TIMESTAMP(`time`)>?)"; 
		List<String> proidList=new ArrayList<String>();
		List<Integer> actList=new ArrayList<Integer>();
		List<Timestamp> timeList=new ArrayList<Timestamp>();
		List<RequireStatusIOS> List=new ArrayList<RequireStatusIOS>();
    	PreparedStatement pstmt;
    	if (conn!=null){
    		try {
    			pstmt = conn.prepareStatement(sql);
    			pstmt.setInt(1, userid); 
    			pstmt.setInt(2, t);		
    	    	ResultSet rs= pstmt.executeQuery();
    	    	while (rs.next()){
    	    		if (rs.getInt(7)!=4){
    	    			String s=rs.getString(9);
        	    		Integer i=rs.getInt(5) % 100;
        	    		proidList.add(s);
        	    		actList.add(i);
        	    		timeList.add(rs.getTimestamp(8));	
    	    		}
    	    		
    	    	}
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		//之后查询每个单号是否在record中有记录，若有则为已完成，若没有则为进行中
    		for (int i=0;i<proidList.size();i++){
    			int act=actList.get(i);
    			String proid=proidList.get(i),table="record_";
    		    table+=Constant.action[act-1];
    		    String sql2="select * from `"+table+"` where `单号`="+proid;
    		    PreparedStatement pstmt2;
    			try {
    				pstmt2 = conn.prepareStatement(sql2);	
    		    	ResultSet rs= pstmt2.executeQuery();
    		    	if (rs.next()){
    		    		RequireStatusIOS r=new RequireStatusIOS();
    		    		r.setStatus(4);
    		    		r.setProid(proid);
    		    		r.setAct(act);
    		    		r.setTime(String.valueOf(timeList.get(i).getTime()));
    		    		List.add(r);
    		    	}else{
    		    		RequireStatusIOS r=new RequireStatusIOS();
    		    		r.setStatus(3);
    		    		r.setProid(proid);
    		    		r.setAct(act);
    		    		r.setTime(String.valueOf(timeList.get(i).getTime()));
    		    		List.add(r);
    		    	}
    			} catch (SQLException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    	    	}finally{
    	    		if (i==proidList.size()-1){
    	    		
    	    			ConnectUtil.close(conn);
    	    			
    	    		}
    	    	}
    		}
    		return List;
    	}    
		return null;
	}
	
	public List<RequireStatusIOS> mrequirePageIOS(String db, int userid, int page,String filter) {
		Connection conn=ConnectUtil.open(db);
		String sql="select * from `require` where (`sender`=?) and (MOD(`action`,100)>3)"
	    			+ " and (`action`<300) and (`status`<> 4) and locate(?,`proid`) order by `id` desc limit ?,10 "; 
		List<String> proidList=new ArrayList<String>();
		List<Integer> actList=new ArrayList<Integer>();
		List<Timestamp> timeList=new ArrayList<Timestamp>();
		List<RequireStatusIOS> List=new ArrayList<RequireStatusIOS>();
    	PreparedStatement pstmt;
    	if (conn!=null){
    		try {
    			pstmt = conn.prepareStatement(sql);
    			pstmt.setInt(1, userid); 
    			pstmt.setString(2, filter);
    			pstmt.setInt(3, (page-1)*10);
    			
    	    	ResultSet rs= pstmt.executeQuery();
    	    	while (rs.next()){
    	    			String s=rs.getString(9);
        	    		Integer i=rs.getInt(5) % 100;
        	    		proidList.add(s);
        	    		actList.add(i);
        	    		timeList.add(rs.getTimestamp(8));	
    	    	}
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		//之后查询每个单号是否在record中有记录，若有则为已完成，若没有则为进行中
    		for (int i=0;i<proidList.size();i++){
    			int act=actList.get(i);
    			String proid=proidList.get(i),table="record_";
    		    table+=Constant.action[act-1];
    		    String sql2="select * from `"+table+"` where `单号`="+proid;
    		    PreparedStatement pstmt2;
    			try {
    				pstmt2 = conn.prepareStatement(sql2);	
    		    	ResultSet rs= pstmt2.executeQuery();
    		    	if (rs.next()){
    		    		RequireStatusIOS r=new RequireStatusIOS();
    		    		r.setStatus(4);
    		    		r.setProid(proid);
    		    		r.setAct(act);
    		    		r.setTime(String.valueOf(timeList.get(i).getTime()));
    		    		List.add(r);
    		    	}else{
    		    		RequireStatusIOS r=new RequireStatusIOS();
    		    		r.setStatus(3);
    		    		r.setProid(proid);
    		    		r.setAct(act);
    		    		r.setTime(String.valueOf(timeList.get(i).getTime()));
    		    		List.add(r);
    		    	}
    			} catch (SQLException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    	    	}finally{
    	    		if (i==proidList.size()-1){
    	    		
    	    			ConnectUtil.close(conn);
    	    			
    	    		}
    	    	}
    		}
    		return List;
    	}    
		return null;
	}
	
	public boolean delete(String db, int requireid) {
		Connection conn=ConnectUtil.open(db);
		PreparedStatement pstmt;
		String sql="delete from `require` where `id`=?";
		if (conn!=null){
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, requireid);
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

	public String proidStatus(String db, String proid) {
		Connection conn=ConnectUtil.open(db);
		PreparedStatement pstmt;
		String sql="select * from `require` where `proid`=? order by `action` desc";
		int step=0;
		int reciver=0;
		int act=0;
		String result="";
		if (conn!=null){
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, proid);
				ResultSet rs= pstmt.executeQuery();
		    	if (rs.next()){
		    		int action=rs.getInt(5);
		    		step=action / 100;
		    		reciver=rs.getInt(3);
		    		act=action % 100;
		    	}else{
		    		ConnectUtil.close(conn);
		    		return result;
		    		
		    	}
			} catch (SQLException e) {
				e.printStackTrace();
			} 
			sql="select `describe` from `right_"+Constant.action[act-1]+"` where step=?";
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, step);
				ResultSet rs= pstmt.executeQuery();
		    	if (rs.next()){
		    		result+=rs.getString(1)+"&";
		    	}
			} catch (SQLException e) {
				e.printStackTrace();
			} 
			sql="select `name` from `staff_personal_info` where id=?";
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, reciver);
				ResultSet rs= pstmt.executeQuery();
		    	if (rs.next()){
		    		result+=rs.getString(1);
		    	}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally{
				ConnectUtil.close(conn);
			}
	    }
           
		return result;
	}

	public List<String> getAct83(String db) {
		Connection conn=ConnectUtil.open(db);
    	String sql="SELECT `content`  FROM `provider`";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
	    	ResultSet rs=pstmt.executeQuery();
	    	List<String> list=new ArrayList<String>();
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

	public List<String> getAct72(String db) {
		Connection conn=ConnectUtil.open(db);
    	String sql="SELECT `name`  FROM `attendence_item` ";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
	    	ResultSet rs=pstmt.executeQuery();
	    	List<String> list=new ArrayList<String>();
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

	public List<Account_stock_balance> getAct57(String db) {
		Connection conn=ConnectUtil.open(db);
    	String sql="SELECT `商品种类`,`型号`,`仓库名称`,`数量`,`当前存货成本`  FROM `account_stock_balance` order by `商品种类`,`型号` ";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
	    	ResultSet rs=pstmt.executeQuery();
	    	List<Account_stock_balance> list=new ArrayList<Account_stock_balance>();
	    	while (rs.next()){
	    		Account_stock_balance asb=new Account_stock_balance();
	    		asb.setName(rs.getString(3));
	    		asb.setType(rs.getString(1));
	    		asb.setModel(rs.getString(2));
	    		asb.setAmount(rs.getDouble(4));
	    		asb.setCost(rs.getDouble(5));
	    		list.add(asb);
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

	public List<String> getAct61(String db) {
		Connection conn=ConnectUtil.open(db);
    	String sql="SELECT DISTINCT(`client`)  FROM `product_stock`  ";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
	    	ResultSet rs=pstmt.executeQuery();
	    	List<String> list=new ArrayList<String>();
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

	public List<String> getAct58(String db) {
		Connection conn=ConnectUtil.open(db);
    	String sql="SELECT `仓库名称`  FROM `warehouse` ";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
	    	ResultSet rs=pstmt.executeQuery();
	    	List<String> list=new ArrayList<String>();
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
	
	public String getNewProid(String db){
		Connection conn=ConnectUtil.open(db);
		PreparedStatement pstmt;
		int count=0;
		if (conn!=null){
			while (count<10) {
				count++;
				String proid=Constant.createProid();
				String sql="SELECT `id`  FROM `require` where `proid`=? ";
				try {
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, proid);
			    	ResultSet rs=pstmt.executeQuery();
			    	if (!(rs.next())) {
			    		ConnectUtil.close(conn);
			    		return proid;
			    	}	
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			ConnectUtil.close(conn);
			return "0";
		}else{
			return "0";
		}
	}

	public String exportPic(String db, String proid, String num, String data) {
		byte[] image = Base64.decode(data);  
		String filePath=Constant.savePath+"pictures\\"+db+"\\";
	    File directory=new File(filePath);
	    if  (!directory.exists()  && !directory.isDirectory())      
	        directory.mkdir();    
	    String fileName=proid+"_"+num+".png"; 
	    File picFile=new File(filePath+fileName);
	    try {
	    	picFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return "0";
		}	
		try {
			OutputStream out = new FileOutputStream(picFile);
			out.write(image);  
			out.close();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return"0";
		}   
		return fileName;
	}

	public List<String> getAct63(String db) {
		Connection conn=ConnectUtil.open(db);
    	String sql="SELECT DISTINCT(`client`) FROM `product_sell`  "
    			+ "union SELECT DISTINCT(`client`) FROM `product_stock` "
    			+ "union SELECT `content` from `provider` ";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
	    	ResultSet rs=pstmt.executeQuery();
	    	List<String> list=new ArrayList<String>();
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

	public void pushNotification(String did) {
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", Constant.accessKeyForPush, Constant.accessKeySecretForPush);
		DefaultAcsClient client = new DefaultAcsClient(profile);
		PushNoticeToAndroidRequest androidRequest  = new PushNoticeToAndroidRequest();
		androidRequest.setAppKey(Constant.appKeyForPush);
		androidRequest.setTarget("device");
		androidRequest.setTargetValue(did);
		androidRequest.setTitle("减一办公");
		androidRequest.setSummary("您有新的待审消息");
		PushNoticeToAndroidResponse pushNoticeToAndroidResponse;
		try {
			pushNoticeToAndroidResponse = client.getAcsResponse(androidRequest);
		//	System.out.println(pushNoticeToAndroidResponse.getResponseId());
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void pushNotificationIOS(String did) {
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", Constant.accessKeyForPush, Constant.accessKeySecretForPush);
		DefaultAcsClient client = new DefaultAcsClient(profile);
		PushNoticeToiOSRequest iosRequest  = new PushNoticeToiOSRequest();
		iosRequest.setAppKey(Constant.appKeyForPush);
		iosRequest.setTarget("device");
		iosRequest.setTargetValue(did);
		iosRequest.setEnv("PRODUCT");
		iosRequest.setSummary("您有新的待审消息");
		iosRequest.setExt("{\"sound\":\"default\", \"badge\":\"42\"}");
		PushNoticeToiOSResponse pushNoticeToIOSResponse;
		try {
			pushNoticeToIOSResponse = client.getAcsResponse(iosRequest);
		//	System.out.println(pushNoticeToIOSResponse .getResponseId());
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
