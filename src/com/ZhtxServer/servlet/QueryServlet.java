package com.ZhtxServer.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ZhtxServer.cons.Constant;
import com.ZhtxServer.dao.AccountReal;
import com.ZhtxServer.dao.BackGroundDaoImpl;
import com.ZhtxServer.dao.CompanyDaoImpl;
import com.ZhtxServer.dao.QueryDaoImpl;
import com.ZhtxServer.entity.Company;
import com.ZhtxServer.table.Account_stock_balance;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Servlet implementation class QueryServlet
 */
@WebServlet("/QueryServlet")
public class QueryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QueryServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("utf-8");
		response.setHeader("content-type","text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		String action=request.getParameter("action");
		switch (action){
		//更新token
		case "rtoken":{
		    String token=request.getParameter("token");
		    QueryDaoImpl dao= new QueryDaoImpl();
			if (dao.refresh(token)) {
				out.print("1");
			}else{
				out.print("0");
			}
			break;
		}
		//得到token
		case "gtoken":{
		    QueryDaoImpl dao= new QueryDaoImpl();
		    String s=dao.getToken();
			if (s.equals("")) {
				out.print("0");
			}else{
				out.print(s);
			}
			break;
		}
		//查询时间段内的account_real数据
		case "accountr":{
			SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd");
			String openid=request.getParameter("openid");
			Date begin,end;
			try {
				begin=formatter.parse(request.getParameter("begin"));
				end=formatter.parse(request.getParameter("end"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				out.println("0");
				break;
			}
			java.sql.Date sqlBegin=new java.sql.Date(begin.getTime());
			java.sql.Date sqlEnd=new java.sql.Date(end.getTime());
		    QueryDaoImpl dao= new QueryDaoImpl();
		    List<AccountReal> list=dao.record(openid,sqlBegin,sqlEnd);
			if (list==null) {
				out.print("0");
			}else{
				Gson gson=new Gson();
				Type t=new TypeToken<List<AccountReal>>(){}.getType();
				out.print(gson.toJson(list,t));
			}
			break;
		}
		//查询科目的余额 (account_balance)
		case "accountb":{
			String openid=request.getParameter("openid");
			QueryDaoImpl dao= new QueryDaoImpl();
			String s=dao.accountb(openid);
	        if (!s.equals("")) {
					out.print(s);
				}else{
					out.print("0");
				}
			break;
		}

		//获得一级会计科目列表，其中参数first_num是会计科目编号的第一个数字
		case "account_class1":{
			String openid=request.getParameter("openid");
			int first_num=request.getParameter("first_num");
			QueryDaoImpl dao= new QueryDaoImpl();
			List<String> list=dao.account_class1(openid,first_num);
			if (list == null){
				out.print("0");
			}else{
				Gson gson=new Gson();
				Type t=new TypeToken<List<String>>(){}.getType();
				out.print(gson.toJson(list,t));
			}
			break;
		}

		//给定一级科目后，根据输入关键字来获得二级科目的智能提示
			case "account_class2":{
				String openid=request.getParameter("openid");
				String class1=Integer.parseInt(request.getParameter("class1"));
				String keyword=request.getParameter("keyword");
				QueryDaoImpl dao=new QueryDaoImpl();
				List<String> list=dao.account_class2(openid,class1,keyword);
				if(list==null){
					out.print("0");
				}else {
					Gson gson=new Gson();
					Type t= new TypeToken<List<String>>(){}.getType();
					out.print(gson.toJson(list,t));
				}
				break;
			}
		//根据提交的一级科目，二级科目查询指定时间的账户余额
			case "account_balance":{
				String openid=request.getParameter("openid");
				String date=request.getParameter("date");
				String class1=request.getParameter("class1");
				String class2=request.getParameter("class2");
				QueryDaoImpl dao=new QueryDaoImpl();
				List<Double> list=dao.account_balance(openid,date,class1,class2);
				if(list==null){
					out.print("0");
				}else {
					Gson gson=new Gson();
					Type t= new TypeToken<List<String>>(){}.getType();
					out.print(gson.toJson(list,t));
				}
				break;
			}
				
		//根据提交的一级科目和二级科目查询指定时间段的账户发生额
			case "account_accumulation":{
				String openid=request.getParameter("openid");
				String start=request.getParameter("start");
				String end=request.getParameter("end");
				String class1=request.getParameter("class1");
				String class2=request.getParameter("class2");
				QueryDaoImpl dao=new QueryDaoImpl();
				List<Double> list=dao.account_accumulation(openid,start,end,class1,class2);
				if(list==null){
					out.print("0");
				}else {
					Gson gson=new Gson();
					Type t= new TypeToken<List<String>>(){}.getType();
					out.print(gson.toJson(list,t));
				}
				break;
			}
		
		//生成指定record表的csv文件
		case "createcsv":{
			String openid=request.getParameter("openid");
			String table=request.getParameter("table");
			QueryDaoImpl dao= new QueryDaoImpl();
			out.print(dao.createcsv(openid, table));
			break;
		}
		//生成指定record表的csv文件
		case "createcsv_account":{
			String openid=request.getParameter("openid");
			String class1=request.getParameter("class1");
			String class2=request.getParameter("class2");
			QueryDaoImpl dao= new QueryDaoImpl();
			out.print(dao.createcsv_account(openid, class1,class2));
			break;
		}		
		//删除指定公司的文件
		case "deletecsv":{
			String openid=request.getParameter("openid");
			QueryDaoImpl dao= new QueryDaoImpl();
		    out.print(dao.deletecsv(openid));
			break;
		}
		//上传csv文件并把该文件插入数据库
		case "uploadandwriteCsv":{
			String db=request.getParameter("db");
			String table=request.getParameter("table");
			String data=request.getParameter("data");
			QueryDaoImpl dao= new QueryDaoImpl();
			String path="C:\\Program Files (x86)\\apache-tomcat-7.0.40-windows-x64\\apache-tomcat-7.0.40\\webapps\\ZhtxServer\\Csv Files\\";
    		Date date=new Date();
    		SimpleDateFormat df = new SimpleDateFormat("yyMMdd");
    		String filename=db+"_"+table+df.format(date)+"_"+Constant.randomNum()+".csv";
    		File myFile = new File(path+filename);
    		try {
				myFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				out.print("0");
			}	
    		if  (!dao.exportCsv(myFile, data))out.print("0");
    		else {
    			if (dao.insertFromCsv(db, table, path+filename)) out.print("1");
    			else out.print("0");
    		}
			break;
		}
		//获得openid对应的公司编号
		case "getcompany":{
			String openid=request.getParameter("openid");
			QueryDaoImpl dao= new QueryDaoImpl();
			out.print(dao.getcompany(openid));
			break;
		}
		//获取account_stock_balance表信息
		case "accountsb":{
			String openid=request.getParameter("openid");
			QueryDaoImpl dao= new QueryDaoImpl();
			List<Account_stock_balance> list=dao.accountsb(openid);
			if (list == null){
				out.print("0");
			}else{
				Gson gson=new Gson();
				Type t=new TypeToken<List<Account_stock_balance>>(){}.getType();
				out.print(gson.toJson(list,t));
			}
			break;
		}
		
		}
		
	}

}
