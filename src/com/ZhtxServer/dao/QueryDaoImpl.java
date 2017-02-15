package com.ZhtxServer.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ZhtxServer.cons.CSVFileUtil;
import com.ZhtxServer.cons.ConnectUtil;
import com.ZhtxServer.cons.Constant;
import com.ZhtxServer.table.Account_stock_balance;
import com.ZhtxServer.dao.AccountReal;

public class QueryDaoImpl {
	public boolean refresh(String token){
		Connection conn=ConnectUtil.open("client_company");
    	String sql="update `token` set `token_value`=? where id=1";
    	PreparedStatement pstmt;
    	if (conn!=null){
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, token); 
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

	//获得一级科目列表,其中参数first_num是会计科目编号的第一个数字
	public List<String> account_class1(String openid,int first_num){
		List<String> result=new ArrayList<String>();
		String db=getDb(openid);
		boolean permit=getPermit(openid,"account_balance");
		if((!db.equals(""))&&permit){
			Connection conn=ConnectUtil.open(db);
			String sql="select distinct `会计科目` from `account_real` where LEFT(`会计科目`,1)="+first_num+" order by `会计科目`";
			PreparedStatement pstmt;
			if(conn!=null){
				try {
					pstmt=conn.prepareStatement(sql);
                    ResultSet rs=pstmt.executeQuery();
					while (rs.next()) {
						String s = rs.getString(1);
						String s2="";
                        String sql2="select `item_name` from `account_item` where `item_num`=?";
						PreparedStatement pstmt2=conn.prepareStatement(sql2);
						pstmt2.setString(1,s);
						ResultSet rs2=pstmt2.executeQuery();
						while (rs2.next()){
							s2=rs2.getString(1);
						}
						result.add(s2);
					}

				}catch (SQLException e){
					e.printStackTrace();
				}finally {
					ConnectUtil.close(conn);
				}
			}
		}else if(!permit){
			result=null;

		}else {
			result=null;
		}
		return result;
	}

	//给定一级科目后，根据输入关键字来获得二级科目的智能提示
	public List<String> account_class2(String openid,String class1,String keyword){
		List<String> temp=new ArrayList<String>();
		List<String> result=new ArrayList<String>();
		String db=getDb(openid);
		boolean permit=getPermit(openid,"account_balance");
		if((!db.equals(""))&&(permit)){
			Connection conn=ConnectUtil.open(db);
			String sql="select `item_num` from `account_item` where `item_name`=?";
			PreparedStatement pstmt;
			if(conn!=null){
				try{
					String s="";
					pstmt=conn.prepareStatement(sql);
					pstmt.setString(1,class1);
					ResultSet rs=pstmt.executeQuery();
					if(rs.next()) s = rs.getString(1);
					String sql2="select distinct `class_2` from `account_real` where `会计科目`=?";
					PreparedStatement pstmt2=conn.prepareStatement(sql2);
					pstmt2.setString(1,s);
					ResultSet rs2=pstmt2.executeQuery();
					while (rs2.next()){
						temp.add(rs2.getString(1));
					}
					for (String data:temp) {
						if (data.contains(keyword)) {
							result.add(data);
						}
					}
				}catch (SQLException e){
					e.printStackTrace();
				}finally {
					ConnectUtil.close(conn);
				}
			}

		}else {
			result=null;
		}
        return result;
	}

	//根据提交的一级科目，二级科目查询指定时间的账户余额
	public List<Double> account_balance(String openid,String date,String class1,String class2) {
		List<Double> result=new ArrayList<Double>();
		String db = getDb(openid);
		boolean permit = getPermit(openid, "account_balance");
		if ((!db.equals("")) && (permit)) {
			Connection conn = ConnectUtil.open(db);
			String sql = "select `item_num` from `account_item` where `item_name`=?";
			PreparedStatement pstmt;
			if (conn != null) {
				try {
					double debit=0;
					double credit=0;
					String s = "";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, class1);
					ResultSet rs = pstmt.executeQuery();
					if (rs.next()) s = rs.getString(1);
					String sql2="select `借贷方`,`金额` from `account_real` where `会计科目`=? and `class_2`=? and `时间`<=?";
					PreparedStatement pstmt2=conn.prepareStatement(sql2);
					pstmt2.setString(1,s);
					pstmt2.setString(2,class2);
					pstmt2.setString(3,date);
					ResultSet rs2=pstmt2.executeQuery();
					while (rs2.next()){
						if(rs2.getString(1)=="借"){
							debit=debit+rs2.getDouble(2);
						}else {
							credit=credit+rs2.getDouble(2);
						}
					}
					result.add(debit);
					result.add(credit);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					ConnectUtil.close(conn);
				}
			}
		}else {
			result=null;
		}
		return result;
	}

	public String accountb(String openid) {
		String result="",db=getDb(openid);
		boolean permit=getPermit(openid,"account_balance");
    	if ((!db.equals(""))&&(permit)){
    		Connection conn=ConnectUtil.open(db);
        	String sql="select * from `account_balance` where LEFT(`会计科目`,1)=1 or LEFT(`会计科目`,1)=2 order by `会计科目`,`class_2`";
        	PreparedStatement pstmt;
        	if (conn!=null){
        		try {
        			pstmt = conn.prepareStatement(sql);
        	    	ResultSet rs= pstmt.executeQuery();
        	    	while (rs.next()){
        	    		String s=rs.getString(2);
        	    		String sql2="select `item_name` from `account_item` where `item_num`=?";
        	    	    PreparedStatement pstmt2=conn.prepareStatement(sql2);
        	    	    pstmt2.setString(1, s);
        	    	    ResultSet rs2= pstmt2.executeQuery();
        	    	    if (rs2.next()) s=rs2.getString(1);
        	    		result+=s+"+"+rs.getString(3)+"+"+rs.getString(5)+";";
        	    	}
        		} catch (SQLException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		} finally{
        			ConnectUtil.close(conn);
        		}	
        	}
    	}
		return result;
	}

	public String getToken() {
		String s="";
		Connection conn=ConnectUtil.open("client_company");
    	String sql="select * from `token` where `id`=1";
    	PreparedStatement pstmt;
    	if (conn!=null){
    		try {
    			pstmt = conn.prepareStatement(sql); 
    	    	ResultSet rs= pstmt.executeQuery();
                if (rs.next()){
                	Timestamp t=rs.getTimestamp(3);
                	Date date=new Date();
                	if (date.getTime()-t.getTime()>3600000){
                	    s="";
                	}else{
                		s=rs.getString(2);
                	}
                }
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} finally{
    			ConnectUtil.close(conn);
    		}	
    	}
		return s;
	}

	public List<AccountReal> record(String openid,  java.sql.Date begin, java.sql.Date end) {
		String db=getDb(openid);
    	if (!db.equals("")){
    		Connection conn=ConnectUtil.open(db);
        	String sql="select * from `account_real` where (left(`会计科目`,4)=5001) and (`时间` >=?) and (`时间` <=?)";
        	PreparedStatement pstmt;
        	if (conn!=null){
        		try {
        			pstmt = conn.prepareStatement(sql);
        			pstmt.setDate(1, begin);
        			pstmt.setDate(2, end);
        	    	ResultSet rs= pstmt.executeQuery();
        	    	List<AccountReal> list=new ArrayList<AccountReal>();
        	    	while (rs.next()){
        	    		AccountReal ar=new AccountReal();
        	    		ar.setId(rs.getInt(1));
        	    		String s=rs.getString(2);
        	    		String sql2="select `item_name` from `account_item` where `item_num`=?";
        	    	    PreparedStatement pstmt2=conn.prepareStatement(sql2);
        	    	    pstmt2.setString(1, s);
        	    	    ResultSet rs2= pstmt2.executeQuery();
        	    		if (rs2.next()){
        	    			ar.setClass_1(rs2.getString(1));
        	    			ar.setClass_2(rs.getString(3));
                            ar.setDate(rs.getDate(4));
                            ar.setNum(rs.getString(5));
            	    		ar.setProid(rs.getString(6));
            	    		ar.setDebt(rs.getString(7));
            	    		list.add(ar);
        	    		}
        	    		
        	    	}
        	    	return list;
        		} catch (SQLException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		} finally{
        			ConnectUtil.close(conn);
        		}	
        	}
    	}
		return null;
	}
	
	public String createcsv(String openid,String table) {
		String db="";
		Connection conn=ConnectUtil.open("client_company");
    	String sql="select `db_name`,`download_permit` from `wechat_openid` where `openid`=?";
    	PreparedStatement pstmt;
    	if (conn!=null){
    		try {
    			pstmt = conn.prepareStatement(sql);
    			pstmt.setString(1, openid); 
    	    	ResultSet rs= pstmt.executeQuery();
    	    	if (rs.next()){
    	    		if (rs.getInt(2)==0) return "1";
    	    		else db=rs.getString(1);
    	    	}
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    			return "0";
    		} finally{
    			ConnectUtil.close(conn);
    		}	
    	}
    	
    	if (!db.equals("")){
    		List<String> dataList=new ArrayList<String>();
    		int tot=0;
    		conn=ConnectUtil.open(db);
    		sql="show fields from `"+table+"`";
    		if (conn!=null){
    			try {
    				pstmt = conn.prepareStatement(sql);
    				ResultSet rs= pstmt.executeQuery();
    		    	while (rs.next()){
    		    		tot++;
    		    		dataList.add("\""+rs.getString(1)+"\",");
    		    	}	  	
    			} catch (SQLException e) {
    				e.printStackTrace();
    				ConnectUtil.close(conn);
    				return "0";
    			} 
    	        String s=dataList.get(tot-1);
    	        s=s.substring(0, s.length()-1)+"\r";
    		    dataList.remove(tot-1);
    		    dataList.add(s);
        	    sql="select * from `"+table+"`";
        		try {
        			pstmt = conn.prepareStatement(sql);
        	    	ResultSet rs= pstmt.executeQuery();
        	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
        	    	while (rs.next()){
        	    		for (int i=1; i<=tot; i++){
        	    			if (i<tot) {
        	    				if (i!=2) {
        	    					dataList.add("\""+rs.getString(i)+"\",");
        	    				}else{
     	    					 
        	    					dataList.add("\""+sdf.format(rs.getTimestamp(i))+"\",");
        	    				}
        	    			}
        	    			else dataList.add("\""+rs.getString(i)+"\"\r");
        	    		}
        	    	}
        		} catch (SQLException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        			return "0";
        		} finally{
        			ConnectUtil.close(conn);
        		}	
        		String path="C:\\Program Files (x86)\\apache-tomcat-7.0.40-windows-x64\\apache-tomcat-7.0.40\\webapps\\ZhtxServer\\Csv Files\\";
        		Date date=new Date();
        		SimpleDateFormat df = new SimpleDateFormat("yyMMdd");
        		String filename=db+"_"+table+df.format(date)+"_"+Constant.randomNum()+".csv";
        		File myFile = new File(path+filename);
        		try {
					myFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					return "0";
				}	
        		if  (exportCsv(myFile,dataList)) return filename;
        		else return "0";
        	}
    	}
		return "0";
	}
	
	public boolean exportCsv(File file, List<String> dataList){
        boolean isSucess=false;
        
        FileOutputStream out=null;
        OutputStreamWriter osw=null;
        BufferedWriter bw=null;
        try {
            out = new FileOutputStream(file);
            osw = new OutputStreamWriter(out);
            bw =new BufferedWriter(osw);
            if(dataList!=null && !dataList.isEmpty()){
                for(String data : dataList){
                    bw.append(data);
                }
            }
            isSucess=true;
        } catch (Exception e) {
            isSucess=false;
        }finally{
            if(bw!=null){
                try {
                    bw.close();
                    bw=null;
                } catch (IOException e) {
                    e.printStackTrace();
                } 
            }
            if(osw!=null){
                try {
                    osw.close();
                    osw=null;
                } catch (IOException e) {
                    e.printStackTrace();
                } 
            }
            if(out!=null){
                try {
                    out.close();
                    out=null;
                } catch (IOException e) {
                    e.printStackTrace();
                } 
            }
        }     
        return isSucess;
    }
	public boolean exportCsv(File file,String data){
        boolean isSucess=false;
        
        FileOutputStream out=null;
        OutputStreamWriter osw=null;
        BufferedWriter bw=null;
        try {
            out = new FileOutputStream(file);
            osw = new OutputStreamWriter(out);
            bw =new BufferedWriter(osw);
            bw.append(data);
            isSucess=true;
        } catch (Exception e) {
            isSucess=false;
        }finally{
            if(bw!=null){
                try {
                    bw.close();
                    bw=null;
                } catch (IOException e) {
                    e.printStackTrace();
                } 
            }
            if(osw!=null){
                try {
                    osw.close();
                    osw=null;
                } catch (IOException e) {
                    e.printStackTrace();
                } 
            }
            if(out!=null){
                try {
                    out.close();
                    out=null;
                } catch (IOException e) {
                    e.printStackTrace();
                } 
            }
        }     
        return isSucess;
	}
	
	public boolean insertFromCsv(String db,String table,String path){

		Connection conn=ConnectUtil.open(db);
		if (conn== null) return false;
		PreparedStatement pstmt;	
		CSVFileUtil u;
		try {
			u = new CSVFileUtil(path);
			String l=u.readLine();
			while (l!=null){
				ArrayList<String> list=CSVFileUtil.fromCSVLinetoArray(l);
				int tot=list.size();
				String sql="insert "+table+" values(null,";
				for (int i=0;i<tot-1;i++){
					if (i<tot-2) sql += "?,";
					else sql +="?)";
				}
				try {
					pstmt = conn.prepareStatement(sql);
					for (int i=0;i<tot-1;i++){
							pstmt.setString(i+1, list.get(i+1));
					}
			    	int rs= pstmt.executeUpdate();
			    	if (rs==0) {return false;}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				} finally{
					ConnectUtil.close(conn);
				}
				l=u.readLine();
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}	
		return true;
	}
	
	public String getcompany(String openid) {
		Connection conn=ConnectUtil.open("client_company");
    	String sql="select `company` from `wechat_openid` where `openid`=?";
    	PreparedStatement pstmt;
    	if (conn!=null){
    		try {
    			pstmt = conn.prepareStatement(sql);
    			pstmt.setString(1, openid); 
    	    	ResultSet rs= pstmt.executeQuery();
    	    	if (rs.next()){
    	    		return rs.getString(1);
    	    	}
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} finally{
    			ConnectUtil.close(conn);
    		}	
    	}
		return "0";
	}

	public String getDb(String openid) {
		Connection conn=ConnectUtil.open("client_company");
    	String sql="select `db_name` from `wechat_openid` where `openid`=?";
    	PreparedStatement pstmt;
    	if (conn!=null){
    		try {
    			pstmt = conn.prepareStatement(sql);
    			pstmt.setString(1, openid); 
    	    	ResultSet rs= pstmt.executeQuery();
    	    	if (rs.next()){
    	    		return rs.getString(1);
    	    	}
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} finally{
    			ConnectUtil.close(conn);
    		}	
    	}
		return "";
	}
	
	public String deletecsv(String openid) {
		String db="";
		Connection conn=ConnectUtil.open("client_company");
    	String sql="select `db_name`,`download_permit` from `wechat_openid` where `openid`=?";
    	PreparedStatement pstmt;
    	if (conn!=null){
    		try {
    			pstmt = conn.prepareStatement(sql);
    			pstmt.setString(1, openid); 
    	    	ResultSet rs= pstmt.executeQuery();
    	    	if (rs.next()){
    	    		if (rs.getInt(2)==0) return "1";
    	    		else db=rs.getString(1);
    	    	}
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    			return "0";
    		} finally{
    			ConnectUtil.close(conn);
    		}	
    	}
    	if (!db.equals("")){
    		if (Constant.deleteFile("Csv Files\\", db)) return "2";
    		else return "0";
    	}
		return "0";
	}
    
	
	
	public String createcsv_account(String openid, String class1, String class2) {
		String db="";
		Connection conn=ConnectUtil.open("client_company");
    	String sql="select `db_name`,`download_permit` from `wechat_openid` where `openid`=?";
    	PreparedStatement pstmt;
    	if (conn!=null){
    		try {
    			pstmt = conn.prepareStatement(sql);
    			pstmt.setString(1, openid); 
    	    	ResultSet rs= pstmt.executeQuery();
    	    	if (rs.next()){
    	    		if (rs.getInt(2)==0) return "1";
    	    		else db=rs.getString(1);
    	    	}
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    			return "0";
    		} finally{
    			ConnectUtil.close(conn);
    		}	
    	}
    	
    	if (!db.equals("")){
    		List<String> itemNameList=new ArrayList<String>();
    		List<String> itemNumList=new ArrayList<String>();
    		List<String> dataList=new ArrayList<String>();
    		int tot=7;
    		conn=ConnectUtil.open(db);
    		if (conn!=null){
    		    String s="\"id\",\"会计科目\",\"二级科目\",\"时间\",\"金额\",\"原始单号\",\"借贷方\"\r";
    		    dataList.add(s);
    		    sql="select * from `account_item`";
    		    try {
        			pstmt = conn.prepareStatement(sql);
        			ResultSet rs= pstmt.executeQuery();
        	    	while (rs.next()){
        	    		itemNameList.add(rs.getString(2));
        	    		itemNumList.add(rs.getString(3));
        	    	}  	
        		} catch (SQLException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        			return "0";
        		}
        		try {
        			sql="select * from `account_real` where `会计科目`=? and `class_2`=? ";
        			pstmt = conn.prepareStatement(sql);
        	    	pstmt.setString(1, class1);
        	    	pstmt.setString(2, class2);
        	    	if (class2.equals("")) {
        	    		sql="select * from `account_real` where `会计科目`=?";
        	    		pstmt = conn.prepareStatement(sql);
                	    pstmt.setString(1, class1);					
        	    	}
        	        if (class1.equals("")) {
             	    	sql="select * from `account_real`";
     					pstmt = conn.prepareStatement(sql);
             	    }
        	    	ResultSet rs= pstmt.executeQuery();
        	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
        	    	while (rs.next()){
        	    		for (int i=1; i<=tot; i++){
        	    			if (i<tot) {
        	    				if (i!=4) {
        	    					if (i!=2) dataList.add("\""+rs.getString(i)+"\",");
        	    					else{
        	    						String s1,s2="";
        	    						s1=rs.getString(2);
        	    						for (int j=0; j<itemNumList.size();j++)
        	    						if (itemNumList.get(j).equals(s1)){
        	    							s2=itemNameList.get(j);
        	    							break;
        	    						}
        	    						dataList.add("\""+s2+"\",");
        	    					}
        	    				}else{
     	    					 
        	    					dataList.add("\""+sdf.format(rs.getTimestamp(i))+"\",");
        	    				}
        	    			}
        	    			else dataList.add("\""+rs.getString(i)+"\"\r");
        	    		}
        	    	}
        		} catch (SQLException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        			return "0";
        		} finally{
        			ConnectUtil.close(conn);
        		}	
        		String path=Constant.savePath+"Csv Files\\";
        		Date date=new Date();
        		SimpleDateFormat df = new SimpleDateFormat("yyMMdd");
        		String filename=db+"_account_real"+df.format(date)+"_"+Constant.randomNum()+".csv";
        		File myFile = new File(path+filename);
        		try {
					myFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					return "0";
				}	
        		if  (exportCsv(myFile,dataList)) return filename;
        		else return "0";
        	}
    	}
		return "0";
	}
	
	public List<Account_stock_balance> accountsb(String openid){
		String db=getDb(openid);
		boolean permit=getPermit(openid,"account_stock_balance");
    	if ((!db.equals(""))&&(permit)){
    		Connection conn=ConnectUtil.open(db);
        	String sql="select * from `account_stock_balance`";
        	PreparedStatement pstmt;
        	if (conn!=null){
        		try {
        			pstmt = conn.prepareStatement(sql);
        	    	ResultSet rs= pstmt.executeQuery();
        	    	List<Account_stock_balance> list=new ArrayList<Account_stock_balance>();
        	    	while (rs.next()){
        	    		Account_stock_balance asb=new Account_stock_balance();
        	    		asb.setName(rs.getString(3));
            	    	asb.setType(rs.getString(4));	
            	    	asb.setModel(rs.getString(5));
            	    	asb.setAmount(rs.getDouble(6));
            	    	asb.setCost(rs.getDouble(7));
        	    		list.add(asb);
        	    	}
        	    	if (list.size()<1) return null;
        	    	else return list;
        		} catch (SQLException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		} finally{
        			ConnectUtil.close(conn);
        		}	
        	}
    	}
		return null;
	}
	
	//得到是否有权限
	private boolean getPermit(String openid, String type){
		Connection conn=ConnectUtil.open("client_company");
    	String sql="select `"+type+"` from `wechat_openid` where `openid`=?";
    	PreparedStatement pstmt;
    	if (conn!=null){
    		try {
    			pstmt = conn.prepareStatement(sql);
    			pstmt.setString(1, openid); 
    	    	ResultSet rs= pstmt.executeQuery();
    	    	if (rs.next()){
    	    		if  (rs.getInt(1)==1) return true;
    	    	}
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
