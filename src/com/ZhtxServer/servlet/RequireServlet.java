package com.ZhtxServer.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ZhtxServer.cons.Constant;
import com.ZhtxServer.dao.HandSignDaoImpl;
import com.ZhtxServer.dao.RequireDaoImpl;
import com.ZhtxServer.dao.RequireStatusIOS;
import com.ZhtxServer.dao.UserDaoImpl;
import com.ZhtxServer.entity.AccountType;
import com.ZhtxServer.entity.Platenum;
import com.ZhtxServer.entity.Require;
import com.ZhtxServer.entity.RequireIOS;
import com.ZhtxServer.entity.RequireStatus;
import com.ZhtxServer.table.Account_stock_balance;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Servlet implementation class RequireServlet
 */
@WebServlet("/RequireServlet")
public class RequireServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RequireServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
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
		String db=request.getParameter("db");
		switch (action){
	    // insert 加入新请求
		case "insert":{
			int sender=Integer.parseInt(request.getParameter("sender"));
			int reciver=Integer.parseInt(request.getParameter("reciver"));
			int previd=Integer.parseInt(request.getParameter("previd"));
			String proid=request.getParameter("proid");
			int act=Integer.parseInt(request.getParameter("act"));
			String data=request.getParameter("data");
			RequireDaoImpl dao=new RequireDaoImpl();
			if (dao.insert(db,sender,reciver,previd,act,proid,data)) {
				out.print("1");
				if (sender != reciver){
					UserDaoImpl dao2=new UserDaoImpl();
					String did=dao2.getDeviceId(reciver);
					if (!did.equals("")) {
						String last=did.substring(did.length()-3, did.length());
						if (!last.equals("IOS")){
							dao.pushNotification(did);
						}else{
							dao.pushNotificationIOS(did.substring(0,did.length()-3));
						}
					}
				}	
			}else{
				out.print("0");
			}
			break;
		}
		
		//检查待审批内容总数
		case "checkCount":{
			int userid=Integer.parseInt(request.getParameter("userid"));
			int act=Integer.parseInt(request.getParameter("type"));
			String filter=request.getParameter("filter");
			RequireDaoImpl dao=new RequireDaoImpl();
			out.print(dao.checkCount(db, userid, act, filter));
			break;
		}
		//检查待审批内容（按页码)
		case "checkPage":{
			int userid=Integer.parseInt(request.getParameter("userid"));
			int act=Integer.parseInt(request.getParameter("type"));
			int page=Integer.parseInt(request.getParameter("page"));
			String filter=request.getParameter("filter");
			RequireDaoImpl dao=new RequireDaoImpl();
			List<Require> list=new ArrayList<Require>();
			list=dao.checkPage(db, userid,act,page,filter);
			if (list!=null){
			    //以json格式返回
			    Gson gson=new Gson();
			    Type type=new TypeToken<List<Require>>(){}.getType();
			    String json=gson.toJson(list,type);
			    out.print(json);
			}else{
				out.print("0");
			}
			break;
		}
		//检查待审批内容(IOS)
		case "checkIOS":{
			int userid=Integer.parseInt(request.getParameter("userid"));
			int act=Integer.parseInt(request.getParameter("type"));
			RequireDaoImpl dao=new RequireDaoImpl();
			List<RequireIOS> list=new ArrayList<RequireIOS>();
			list=dao.checkIOS(db, userid,act);
			if (list!=null){
			    //以json格式返回
			    Gson gson=new Gson();
			    Type type=new TypeToken<List<RequireIOS>>(){}.getType();
			    String json=gson.toJson(list,type);
			    out.print(json);
			}else{
				out.print("0");
			}
			break;
		}		
		//检查待审批内容（按页码IOS)
		case "checkPageIOS":{
			int userid=Integer.parseInt(request.getParameter("userid"));
			int act=Integer.parseInt(request.getParameter("type"));
			int page=Integer.parseInt(request.getParameter("page"));
			String filter=request.getParameter("filter");
			RequireDaoImpl dao=new RequireDaoImpl();
			List<RequireIOS> list=new ArrayList<RequireIOS>();
			list=dao.checkPageIOS(db, userid,act,page,filter);
			if (list!=null){
			    //以json格式返回
			    Gson gson=new Gson();
			    Type type=new TypeToken<List<RequireIOS>>(){}.getType();
			    String json=gson.toJson(list,type);
			    out.print(json);
			}else{
				out.print("0");
			}
			break;
		}		
		//检查自己发出过的审批总数
		case "mrequireCount":{
			int userid=Integer.parseInt(request.getParameter("userid"));
			String filter=request.getParameter("filter");
			RequireDaoImpl dao=new RequireDaoImpl();
			int  num=dao.mrequireCount(db, userid,filter);
			if (num==0){
				out.print("0");
			}else{
				out.print(String.valueOf(num));
			}
			
			break;
		}		
		//检查自己发出过的审批按页码
		case "mrequirePage":{
			int userid=Integer.parseInt(request.getParameter("userid"));
			int page=Integer.parseInt(request.getParameter("page"));
			String filter=request.getParameter("filter");
			RequireDaoImpl dao=new RequireDaoImpl();
			List<RequireStatus> list=new ArrayList<RequireStatus>();
			list=dao.mrequirePage(db, userid,page,filter);
			if (list!=null){
			    //以json格式返回
			    Gson gson=new Gson();
			    Type type=new TypeToken<List<RequireStatus>>(){}.getType();
			    String json=gson.toJson(list,type);
			    out.print(json);
			}else{
				out.print("0");
			}			
			break;
		}
		//检查自己发出过的审批(IOS)
		case "mrequireIOS":{
			int userid=Integer.parseInt(request.getParameter("userid"));
			int day=Integer.parseInt(request.getParameter("day"));
			RequireDaoImpl dao=new RequireDaoImpl();
			List<RequireStatusIOS> list=new ArrayList<RequireStatusIOS>();
			list=dao.mrequireIOS(db, userid,day);
			if (list!=null){
			    //以json格式返回
			    Gson gson=new Gson();
			    Type type=new TypeToken<List<RequireStatusIOS>>(){}.getType();
			    String json=gson.toJson(list,type);
			    out.print(json);
			}else{
				out.print("0");
			}			
			break;
		}
		//检查自己发出过的审批按页码(IOS)
		case "mrequirePageIOS":{
			int userid=Integer.parseInt(request.getParameter("userid"));
			int page=Integer.parseInt(request.getParameter("page"));
			String filter=request.getParameter("filter");
			RequireDaoImpl dao=new RequireDaoImpl();
			List<RequireStatusIOS> list=new ArrayList<RequireStatusIOS>();
			list=dao.mrequirePageIOS(db, userid,page,filter);
			if (list!=null){
			    //以json格式返回
			    Gson gson=new Gson();
			    Type type=new TypeToken<List<RequireStatusIOS>>(){}.getType();
			    String json=gson.toJson(list,type);
			    out.print(json);
			}else{
				out.print("0");
			}			
			break;
		}
		//查询单号对应的流程进行到哪一步
		case "proidstatus":{
			String proid=request.getParameter("proid");
			RequireDaoImpl dao=new RequireDaoImpl();
			String result=dao.proidStatus(db,proid);
			if (result.equals("")){
				out.print("0");
			}else{
				out.print(result);
			}
			break;
		}
		//删除审批(按id)
		case "delete":{
			int requireid=Integer.parseInt(request.getParameter("id"));
			RequireDaoImpl dao=new RequireDaoImpl();
			if (dao.setStatus(db, requireid, 4)){
				out.print("1");
			}else{
				out.print("0");
			}
			break;
		}
		//删除审批(按单号和步骤)
		case "delete2":{
			String proid=request.getParameter("proid");
			int act=Integer.parseInt(request.getParameter("act"));
			RequireDaoImpl dao=new RequireDaoImpl();
			if (dao.setStatus(db,proid,act,4)){
				out.print("1");
			}else{
				out.print("0");
			}
			break;
		}
		//删除审批(按id,数据库中不保留记录)
		case "deleter":{
			int requireid=Integer.parseInt(request.getParameter("id"));
			RequireDaoImpl dao=new RequireDaoImpl();
			if (dao.delete(db, requireid)){
				out.print("1");
			}else{
				out.print("0");
			}
			break;		
		}
		//同意审批
		case "approve":{
			int requireid=Integer.parseInt(request.getParameter("id"));
			RequireDaoImpl dao=new RequireDaoImpl();
			if (dao.setStatus(db, requireid, 1)){
				out.print("1");
			}else{
				out.print("0");
			}
			break;
		}
		//驳回审批
		case "reject":{
			int requireid=Integer.parseInt(request.getParameter("id"));
			String info=request.getParameter("info");
			RequireDaoImpl dao=new RequireDaoImpl();
			if (dao.setStatus(db, requireid, 2,info)){
				out.print("1");
			}else{
				out.print("0");
			}
			break;
		}
		//再激活审批
		case "active":{
			int requireid=Integer.parseInt(request.getParameter("id"));
			RequireDaoImpl dao=new RequireDaoImpl();
			if (dao.setStatus(db, requireid, 0)){
				out.print("1");
			}else{
				out.print("0");
			}
			break;
		}
		//结束审批
		case "end":{
			int requireid=Integer.parseInt(request.getParameter("id"));
			RequireDaoImpl dao=new RequireDaoImpl();
			if (dao.setStatus(db, requireid, 3)){
				out.print("1");
			}else{
				out.print("0");
			}
			break;
		}
		//获取打印信息
		case "print":{
			String id=request.getParameter("id");
			RequireDaoImpl dao=new RequireDaoImpl();
			List<String> list=new ArrayList<String>();
			list=dao.printinfo(db,id);
			if (list!=null){
			    //以json格式返回
			    Gson gson=new Gson();
			    Type type=new TypeToken<List<String>>(){}.getType();
			    String json=gson.toJson(list,type);
			    out.print(json);
			}else{
				out.print("0");
			}
			break;
		}
		//设置信息为已读
		case "read":{
			int requireid=Integer.parseInt(request.getParameter("id"));
			RequireDaoImpl dao=new RequireDaoImpl();
			if (dao.setRead(db, requireid)){
				out.print("1");
			}else{
				out.print("0");
			}
			break;
		}
		//app后台询问新消息
		case "che":{
			int userid=Integer.parseInt(request.getParameter("userid"));
			RequireDaoImpl dao=new RequireDaoImpl();
			String s=dao.che(db, userid);
			if (s.equals("")){
			    out.print("0");
			}else{
				out.print(s);
			}
			break;
		}
		//app统计消息数目
		case "getnum":{
			int userid=Integer.parseInt(request.getParameter("userid"));
			RequireDaoImpl dao=new RequireDaoImpl();
			HandSignDaoImpl dao2=new HandSignDaoImpl();
			int ss=dao2.getStatus(db, userid);
			String s=dao.getnum(db, userid);
			if (s.equals("")){
			    out.print(String.valueOf(ss)+"#"+"0");
			}else{
				out.print(String.valueOf(ss)+"#"+s);
			}
			break;
		}
		//获取会计科目表
		case "atype":{
			RequireDaoImpl dao=new RequireDaoImpl();
            List<AccountType> list=new ArrayList<AccountType>();
            list=dao.getAccountType(db);
			if (list!=null){
				Gson gson=new Gson();
				Type type=new TypeToken<List<AccountType>>(){}.getType();
				out.println(gson.toJson(list,type));
			}else{
				out.print("0");
			}
			break;
		}
		//获取司机表
		case "act55":{
			RequireDaoImpl dao=new RequireDaoImpl();
            List<Platenum> list=new ArrayList<Platenum>();
            list=dao.getAct55(db);
			if (list!=null){
				Gson gson=new Gson();
				Type type=new TypeToken<List<Platenum>>(){}.getType();
				out.println(gson.toJson(list,type));
			}else{
				out.print("0");
			}
			break;
		}	
		//获取商品列表account_stock_balance
		case "act57":{
			RequireDaoImpl dao=new RequireDaoImpl();
            List<Account_stock_balance> list=new ArrayList<Account_stock_balance>();
            list=dao.getAct57(db);
			if (list!=null){
				Gson gson=new Gson();
				Type type=new TypeToken<List<Account_stock_balance>>(){}.getType();
				out.println(gson.toJson(list,type));
			}else{
				out.print("0");
			}
			break;
		}		
		//获取手动签到员工表
		case "act72":{
			RequireDaoImpl dao=new RequireDaoImpl();
            List<String> list=new ArrayList<String>();
            list=dao.getAct72(db);
			if (list!=null){
				Gson gson=new Gson();
				Type type=new TypeToken<List<String>>(){}.getType();
				out.println(gson.toJson(list,type));
			}else{
				out.print("0");
			}
			break;
		}		
		//获取付款对象表
		case "act81":{
			String mode=request.getParameter("mode");
			RequireDaoImpl dao=new RequireDaoImpl();
            List<String> list=new ArrayList<String>();
            if (mode==null) list=dao.getAct81(db);
            else list=dao.getAct81(db,mode);
			if (list!=null){
				Gson gson=new Gson();
				Type type=new TypeToken<List<String>>(){}.getType();
				out.println(gson.toJson(list,type));
			}else{
				out.print("0");
			}
			break;
		}
		//获取收款对象表
		case "act91":{
			String mode=request.getParameter("mode");
			RequireDaoImpl dao=new RequireDaoImpl();
            List<String> list=new ArrayList<String>();
            if (mode==null) list=dao.getAct91(db);
            else list=dao.getAct91(db,mode);
			if (list!=null){
				Gson gson=new Gson();
				Type type=new TypeToken<List<String>>(){}.getType();
				out.println(gson.toJson(list,type));
			}else{
				out.print("0");
			}
			break;
		}
		//获取付款方式表
		case "act82":{
			RequireDaoImpl dao=new RequireDaoImpl();
            List<String> list=new ArrayList<String>();
            list=dao.getAct82(db);
			if (list!=null){
				Gson gson=new Gson();
				Type type=new TypeToken<List<String>>(){}.getType();
				out.println(gson.toJson(list,type));
			}else{
				out.print("0");
			}
			break;
		}	
		//获取供应商表
		case "act83":{
			RequireDaoImpl dao=new RequireDaoImpl();
            List<String> list=new ArrayList<String>();
            list=dao.getAct83(db);
			if (list!=null){
				Gson gson=new Gson();
				Type type=new TypeToken<List<String>>(){}.getType();
				out.println(gson.toJson(list,type));
			}else{
				out.print("0");
			}
			break;
		}	
		//获取进货商品表中的供应商
		case "act61":{
			RequireDaoImpl dao=new RequireDaoImpl();
            List<String> list=new ArrayList<String>();
            list=dao.getAct61(db);
			if (list!=null){
				Gson gson=new Gson();
				Type type=new TypeToken<List<String>>(){}.getType();
				out.println(gson.toJson(list,type));
			}else{
				out.print("0");
			}
			break;
		}	
		//获取仓库
	    case "act58":{
	    	RequireDaoImpl dao=new RequireDaoImpl();
            List<String> list=new ArrayList<String>();
            list=dao.getAct58(db);
			if (list!=null){
				Gson gson=new Gson();
				Type type=new TypeToken<List<String>>(){}.getType();
				out.println(gson.toJson(list,type));
			}else{
				out.print("0");
			}
			break;
		}	
	    //获取交易对象
	    case "act63":{
	    	RequireDaoImpl dao=new RequireDaoImpl();
            List<String> list=new ArrayList<String>();
            list=dao.getAct63(db);
			if (list!=null){
				Gson gson=new Gson();
				Type type=new TypeToken<List<String>>(){}.getType();
				out.println(gson.toJson(list,type));
			}else{
				out.print("0");
			}
			break;
		}	
	    //获取不重复的新单号
	    case "newproid":{
	    	RequireDaoImpl dao=new RequireDaoImpl();
            out.print(dao.getNewProid(db));
			break;
		}	
	    //根据base64编码,输入图片文件
	    case "uploadPic":{
	    	String data=request.getParameter("data");
	    	String proid=request.getParameter("proid");
	    	String num=request.getParameter("num");
	    	RequireDaoImpl dao=new RequireDaoImpl();
            out.print(dao.exportPic(db,proid,num,data));
            break;
	    }
	    //删除指定单号指定步骤的所有图片
	    case "deletePic":{
	    	String step=request.getParameter("step");
	    	String proid=request.getParameter("proid");
	    	if (Constant.deleteFile("pictures\\"+db+"\\", proid+"_"+step)) out.print("1");
	    	else out.print("0");
            break;
	    }
		//end
		}			
	}

}
