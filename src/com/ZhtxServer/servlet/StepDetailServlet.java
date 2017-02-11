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

import com.ZhtxServer.dao.StepDetailDaoImpl;
import com.ZhtxServer.entity.StepDetail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Servlet implementation class StepDetailServlet
 */
@WebServlet("/StepDetailServlet")
public class StepDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StepDetailServlet() {
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
		String table=request.getParameter("tab");
		switch (action){
		//获得对应步骤的详细信息
		case "info":{
			StepDetailDaoImpl dao=new StepDetailDaoImpl();
			List<StepDetail> list=new ArrayList<StepDetail>();
			list=dao.info(db,table);
			if (list!=null){
			    //以json格式返回
			    Gson gson=new Gson();
			    Type type=new TypeToken<List<StepDetail>>(){}.getType();
			    String json=gson.toJson(list,type);
			    out.print(json);
			}else{
				out.print("0");
			}
			break;
		}
		
		}			
	}


}
