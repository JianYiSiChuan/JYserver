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

import com.ZhtxServer.dao.StaffDaoImpl;
import com.ZhtxServer.entity.Staff;
import com.ZhtxServer.entity.StepRight;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Servlet implementation class StaffServlet
 */
@WebServlet("/StaffServlet")
public class StaffServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StaffServlet() {
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
		switch (action){
		//加入新员工
		case "insert":{
			int id=Integer.parseInt(request.getParameter("id"));
			String name=request.getParameter("name");
			String id_card=request.getParameter("id_card");
			StaffDaoImpl dao=new StaffDaoImpl();
			if (dao.insert(db,id,name,id_card)) {
				out.print("1");
			}else{
				out.print("0");
			}
			break;
		}
		//设置一个员工的信息
		case "setinfo":{
			int id=Integer.parseInt(request.getParameter("id"));
			String bankid=request.getParameter("bankid");
			String platenum=request.getParameter("platenum");
			StaffDaoImpl dao=new StaffDaoImpl();
			if (dao.setinfo(db,id,bankid,platenum)) {
				out.print("1");
			}else{
				out.print("0");
			}
			break;
		}
		//得到一个员工的信息
		case "info":{
			int id=Integer.parseInt(request.getParameter("id"));
			StaffDaoImpl dao=new StaffDaoImpl();
			Staff staff=new Staff();
			staff=dao.getInfo(db,id);
			if (staff!=null){
				Gson gson=new Gson();
				String json=gson.toJson(staff);
				out.print(json);
			}else{
				out.print("0");
			}
			break;
		}
		//得到全部员工的信息
		case "allinfo":{
			StaffDaoImpl dao=new StaffDaoImpl();
			List<Staff> list=dao.allinfo(db);
			if (list!=null){
				Gson gson=new Gson();
				Type type=new TypeToken<List<Staff>>(){}.getType();
				out.println(gson.toJson(list,type));
			}else{
				out.print("0");
			}
			break;
		}
		case "delete":{
			int id=Integer.parseInt(request.getParameter("id"));
			StaffDaoImpl dao=new StaffDaoImpl();
			if (dao.delete(db,id)) {
				out.print("1");
			}else{
				out.print("0");
			}
			break;
		}
		}	
	}

}
