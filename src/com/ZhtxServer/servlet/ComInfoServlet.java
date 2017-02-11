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
import com.ZhtxServer.entity.Company;
import com.ZhtxServer.entity.Staff;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Servlet implementation class ComInfoServlet
 */
@WebServlet("/ComInfoServlet")
public class ComInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ComInfoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
		String comid=request.getParameter("comid");
		switch (action){
		case "info":{
		    CompanyDaoImpl dao=new CompanyDaoImpl();
			Company com=dao.getInfo(comid);
			if (com!=null){
				Gson gson=new Gson();
				String json=gson.toJson(com);
				out.print(json);
			}else{
				out.print("0");
			}
			break;
		}
		case "work":{
			String db=request.getParameter("db");
			CompanyDaoImpl dao=new CompanyDaoImpl();
			List<Boolean> list=dao.getWork(db);
			if (list!=null){
				Gson gson=new Gson();
				Type type=new TypeToken<List<Boolean>>(){}.getType();
				out.println(gson.toJson(list,type));
			}else{
				out.print("0");
			}
			break;
		}
		}
	}

}
