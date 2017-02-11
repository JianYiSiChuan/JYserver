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

import com.ZhtxServer.dao.DriverDaoImpl;
import com.ZhtxServer.dao.UserDaoImpl;
import com.ZhtxServer.entity.Driver;
import com.ZhtxServer.entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Servlet implementation class DriverDaoServlet
 */
@WebServlet("/DriverDaoServlet")
public class DriverDaoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	DriverDaoImpl dao=new DriverDaoImpl();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DriverDaoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("utf-8");
		response.setHeader("content-type","text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		String action=request.getParameter("action");
		switch (action){
		//根据id获取司机信息
		case "info":{
			int id=Integer.parseInt( request.getParameter("id"));
			String db= request.getParameter("db");
			Driver d=dao.info(db, id);
			if (d!=null){
				Gson gson=new Gson();
				String json=gson.toJson(d);
				out.print(json);
			}else{
				out.print("0");
			}
		    break;
		}
		//根据车牌号获取司机信息
		case "infobyplatenum":{
			String platenum=request.getParameter("platenum");
			String db= request.getParameter("db");
			Driver d=dao.info(db, platenum);
			if (d!=null){
				Gson gson=new Gson();
				String json=gson.toJson(d);
				out.print(json);
			}else{
				out.print("0");
			}
		    break;
		}		
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
