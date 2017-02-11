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

import com.ZhtxServer.dao.ProductDaoImpl;
import com.ZhtxServer.entity.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Servlet implementation class ProductServlet
 */
@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductServlet() {
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
		//添加新商品
		case "insert":{
			String name=request.getParameter("name");
			String type=request.getParameter("type");
			String table=request.getParameter("table");
			double price=Double.parseDouble(request.getParameter("price"));
			String client=request.getParameter("client");
			int account=Integer.parseInt(request.getParameter("account"));
			ProductDaoImpl dao=new ProductDaoImpl();
			int r=dao.insert(db,table,name,type,price,client,account);
			out.println(String.valueOf(r));
			break;
		}
        //得到全部商品信息(商品管理)
		case "allinfo":{
			ProductDaoImpl dao=new ProductDaoImpl();
			List<Product> list=new ArrayList<Product>();
			String table=request.getParameter("table");
			list=dao.allinfo(db,table,1);
			if (list!=null){
			    //以json格式返回
			    Gson gson=new Gson();
			    Type type=new TypeToken<List<Product>>(){}.getType();
			    String json=gson.toJson(list,type);
			    out.print(json);
			}else{
				out.print("0");
			}
			break;
		}
		 //得到全部商品信息(流程商品选择)
		case "ainfo":{
			ProductDaoImpl dao=new ProductDaoImpl();
			List<Product> list=new ArrayList<Product>();
			String table=request.getParameter("table");
			list=dao.allinfo(db,table,0);
			if (list!=null){
			    //以json格式返回
			    Gson gson=new Gson();
			    Type type=new TypeToken<List<Product>>(){}.getType();
			    String json=gson.toJson(list,type);
			    out.print(json);
			}else{
				out.print("0");
			}
			break;
		}
		//删除商品
		case "delete":{
			int id=Integer.parseInt(request.getParameter("id"));
			String table=request.getParameter("table");
			ProductDaoImpl dao=new ProductDaoImpl();
			if (dao.delete(db,table,id)) {
				out.print("1");
			}else{
				out.print("0");
			}
			break;
		}
		//修改商品信息
		case "change":{
			int id=Integer.parseInt(request.getParameter("id"));
			String name=request.getParameter("name");
			String type=request.getParameter("type");
			double price=Double.parseDouble(request.getParameter("price"));
			String client=request.getParameter("client");
			int account=Integer.parseInt(request.getParameter("account"));
			String table=request.getParameter("table");
			ProductDaoImpl dao=new ProductDaoImpl();
			if (dao.change(db,table,id,name,type,price,client,account)) {
				out.print("1");
			}else{
				out.print("0");
			}
			break;
		}
		//添加供应商
		case "addp":{
			String content=request.getParameter("content");
			ProductDaoImpl dao=new ProductDaoImpl();
			if (dao.addp(db,content)) {
				out.print("1");
			}else{
				out.print("0");
			}
			break;
		}
		//删除供应商
		case "deletep":{
			String content=request.getParameter("content");
			ProductDaoImpl dao=new ProductDaoImpl();
			if (dao.deletep(db,content)) {
				out.print("1");
			}else{
				out.print("0");
			}
			break;
		}
		//得到供应商列表
		case "infop":{
			ProductDaoImpl dao=new ProductDaoImpl();
			List<String> list=new ArrayList<String>();
			list=dao.infop(db);
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
		
		}		
	}


}
