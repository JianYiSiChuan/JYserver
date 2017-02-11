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

import com.ZhtxServer.dao.RequireDaoImpl;
import com.ZhtxServer.dao.StepRightDaoImpl;
import com.ZhtxServer.entity.Require;
import com.ZhtxServer.entity.StepRight;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Servlet implementation class StepRightServlet
 */
@WebServlet("/StepRightServlet")
public class StepRightServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StepRightServlet() {
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
		String db=request.getParameter("db");
		String table=request.getParameter("tab");
		switch (action){
		case "allinfo":{
			StepRightDaoImpl dao=new StepRightDaoImpl();
			List<StepRight> list=dao.allinfo(db, table);
			if (list!=null){
				Gson gson=new Gson();
				Type type=new TypeToken<List<StepRight>>(){}.getType();
				out.print(gson.toJson(list,type));
			}else{
				out.print("0");
			}
			break;
		}
		case "info":{
			int step=Integer.parseInt(request.getParameter("step"));
			StepRightDaoImpl dao=new StepRightDaoImpl();
			StepRight sr=dao.info(db, table,step);
			if (sr!=null){
				Gson gson=new Gson();
				out.print(gson.toJson(sr));
			}else{
				out.print("0");
			}
			break;
		}
		case "change":{
			String staff=request.getParameter("staff");
			int stepid=Integer.parseInt(request.getParameter("stepid"));
			StepRightDaoImpl dao=new StepRightDaoImpl();
			staff.replace(" ", "+");
			if (dao.change(db,table,stepid,staff)){
				out.print("1");
			}else{
				out.print("0");
			}
			break;
		}
	    }
	}
}
