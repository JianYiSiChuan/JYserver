package com.ZhtxServer.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ZhtxServer.dao.BackGroundDaoImpl;
import com.ZhtxServer.dao.UserDaoImpl;
import com.ZhtxServer.entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Servlet implementation class BackGroundServlet
 */
@WebServlet("/BackGroundServlet")
public class BackGroundServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BackGroundServlet() {
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
		// 处理后台程序发来的请求 
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("utf-8");
		response.setHeader("content-type","text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		String action=request.getParameter("action");
		String db=request.getParameter("db");
		BackGroundDaoImpl dao=new BackGroundDaoImpl();
		switch (action){
		//在account_real表中插入一条新纪录 
		case "typein_begin":{
			String class1=request.getParameter("class1");
			String class2=request.getParameter("class2");
			String num=request.getParameter("num");
			String proid=request.getParameter("proid");
			String debt=request.getParameter("debt");
			SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd");
			Date date;
			try {
				date=formatter.parse(request.getParameter("date"));
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				out.println("0");
				break;
			}
			java.sql.Date sqlDate=new java.sql.Date(date.getTime());
			if (dao.Typein_Begin(db,class1,class2,sqlDate,num,proid,debt)){
				out.println("1");
			}else{
				out.println("0");
			}
			break;
		}
		//case结束
		}
	}

}
