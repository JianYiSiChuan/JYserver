package com.ZhtxServer.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ZhtxServer.dao.HandSignDaoImpl;
import com.ZhtxServer.dao.RequireDaoImpl;
import com.ZhtxServer.entity.AccountType;
import com.ZhtxServer.entity.Require;
import com.ZhtxServer.entity.RequireStatus;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 手动签到servlet
 */
@WebServlet("/HandSignServlet")
public class HandSignServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HandSignServlet() {
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
		String db=request.getParameter("db");
		switch (action){
	    //在考勤名单中加入新成员
		case "insert":{
			String name=request.getParameter("name");
			HandSignDaoImpl dao=new HandSignDaoImpl();
			if (dao.insert(db,name)) {
				out.print("1");
			}else{
				out.print("0");
			}
			break;
		}
		//在考勤名单中删除成员
		case "delete":{
			String name=request.getParameter("name");
			HandSignDaoImpl dao=new HandSignDaoImpl();
			if (dao.delete(db,name)) {
				out.print("1");
			}else{
				out.print("0");
			}
			break;
		}
		//得到考勤名单
		case "signitem":{
			HandSignDaoImpl dao=new HandSignDaoImpl();
			List<String> list=new ArrayList<String>();
			list=dao.signitem(db);
			if (list!=null){
				Gson gson=new Gson();
			    Type type=new TypeToken<List<String>>(){}.getType();
			    String json=gson.toJson(list,type);
			    out.print(json);
			}else{
				out.print("0");
			}
			break;
		}
		//得到考勤名单及考勤状态
		case "signlist":{
			HandSignDaoImpl dao=new HandSignDaoImpl();
			List<String> list=new ArrayList<String>();
			list=dao.signlist(db);
			if (list!=null){
				Gson gson=new Gson();
			    Type type=new TypeToken<List<String>>(){}.getType();
			    String json=gson.toJson(list,type);
			    out.print(json);
			}else{
				out.print("0");
			}
			break;
		}
		//设置考勤状态
		case "sign":{
			String name=request.getParameter("name");
			int status=Integer.parseInt(request.getParameter("status"));
			HandSignDaoImpl dao=new HandSignDaoImpl();
			if (dao.sign(db,name,status)) {
				out.print("1");
			}else{
				out.print("0");
			}
			break;
		}
		//end
		}			
	}
		

}
