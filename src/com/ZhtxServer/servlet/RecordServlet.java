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

import com.ZhtxServer.dao.RecordDaoImpl;
import com.ZhtxServer.dao.RequireDaoImpl;
import com.ZhtxServer.entity.Require;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Servlet implementation class RecordServlet
 */
@WebServlet("/RecordServlet")
public class RecordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RecordServlet() {
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
	    // insert 加入新请求
		case "record":{
			List<String> list=new ArrayList<String>();
			Gson gson=new Gson();
			Type type1=new TypeToken<List<String>>(){}.getType();
			list=gson.fromJson(request.getParameter("list"), type1);
			String type=request.getParameter("type");
			RecordDaoImpl dao=new RecordDaoImpl();
			if (dao.record(db,type,list)) {
				out.print("1");
			}else{
				out.print("0");
			}
			break;
		}
		case "delete":{
			String proid=request.getParameter("proid");
			String type=request.getParameter("type");
			RecordDaoImpl dao=new RecordDaoImpl();
			if (dao.delete(db,type,proid)) {
				out.print("1");
			}else{
				out.print("0");
			}
			break;
		}
		case "recordtest":{
			List<String> list=new ArrayList<String>();
			Gson gson=new Gson();
			Type type1=new TypeToken<List<String>>(){}.getType();
			out.println(request.getParameter("list"));
			break;
		}
		case "query":{
			String proid=request.getParameter("proid");
			int act=Integer.parseInt(request.getParameter("act"));
			RecordDaoImpl dao=new RecordDaoImpl();
			String s=dao.query(db,proid,act);
			if (s.equals("")){
				out.print("0");
			}else{
				out.print(s);
			}
			break;
		}
		}			
	
	}

}
