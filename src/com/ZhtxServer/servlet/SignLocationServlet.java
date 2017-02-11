package com.ZhtxServer.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ZhtxServer.dao.CompanyDaoImpl;
import com.ZhtxServer.dao.SignLocationDaoImpl;
import com.ZhtxServer.entity.Company;
import com.ZhtxServer.entity.Require;
import com.ZhtxServer.entity.SignLocation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Servlet implementation class SignLocationServlet
 */
@WebServlet("/SignLocationServlet")
public class SignLocationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignLocationServlet() {
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
		case "info":{
		    SignLocationDaoImpl dao=new SignLocationDaoImpl();
			List<SignLocation> sl=dao.info(db);
			if (sl!=null){
				Gson gson=new Gson();
				Type type=new TypeToken<List<SignLocation>>(){}.getType();
				String json=gson.toJson(sl,type);
				out.print(json);
			}else{
				out.print("0");
			}
			break;
		}
		case "insert":{
			double latitude=Double.parseDouble(request.getParameter("la"));
			double longitude=Double.parseDouble(request.getParameter("lo"));
			String describe=request.getParameter("de");
			String time=request.getParameter("time");
			SignLocationDaoImpl dao=new SignLocationDaoImpl();
			if (dao.insert(db,latitude,longitude,describe,time)){
				out.print("1");
			}else{
				out.print("0");
			}
			break;
		}
		case "delete":{
			int id=Integer.parseInt(request.getParameter("id"));
			SignLocationDaoImpl dao=new SignLocationDaoImpl();
			if (dao.delete(db,id)){
				out.print("1");
			}else{
				out.print("0");
			}
			break;
		}
		}
	}

}
