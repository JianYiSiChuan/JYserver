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

import com.ZhtxServer.dao.UserDaoImpl;
import com.ZhtxServer.entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/UserDaoServlet")
public class UserDaoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserDaoServlet() {
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
	//�����û���һ���͵�servlet
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//�õ���������
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("utf-8");
		response.setHeader("content-type","text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		String action=request.getParameter("action");
		switch (action){
		//登录
		case "login":{
			String username=request.getParameter("username");
			String password=request.getParameter("password");
			String phoneid=request.getParameter("uid");
			UserDaoImpl dao=new UserDaoImpl();
			User user=dao.login(username, password,phoneid);
			if (user!=null){
				Gson gson=new Gson();
				String json=gson.toJson(user);
				out.print(json);
			}else{
				if (dao.isRightPhonid(username,phoneid)) out.print("-2");
				else out.print("-1");
			}
		    break;
		}
		//建立新用户
		case "create":{		
			String username=request.getParameter("username");
			String password=request.getParameter("password");
			String phoneid=request.getParameter("uid");
			String comid=request.getParameter("comid");
			UserDaoImpl dao=new UserDaoImpl();
			out.print(String.valueOf(dao.create(comid, username, password,phoneid)));
			break;
		}
		//改变密码
		case "changepw":{
			int id=Integer.parseInt(request.getParameter("id"));
			String password=request.getParameter("password");
			UserDaoImpl dao=new UserDaoImpl();
			if (dao.changePassword(id, password)) {
				out.print("1");
			}else{
				out.print("0");
			}
			break;
		}
		//删除用户
		case "delete":{
			int id=Integer.parseInt(request.getParameter("id"));
			UserDaoImpl dao=new UserDaoImpl();
			if (dao.delete(id)) {
				out.print("1");
			}else{
				out.print("0");
			}
			break;
		}
		//设置用户状态为通过验证
		case "pass":{
			int id=Integer.parseInt(request.getParameter("id"));
			UserDaoImpl dao=new UserDaoImpl();
			if (dao.pass(id)) {
				out.print("1");
			}else{
				out.print("0");
			}
			break;
		}
		//检查用户名和机器码是否重复
		case "check":{
			String username=request.getParameter("username");
			String phoneid=request.getParameter("uid");
			String comid=request.getParameter("comid");
			UserDaoImpl dao=new UserDaoImpl();
			if (dao.checkName(username,phoneid,comid)) {
				out.print("1");
			}else{
				out.print("0");
			}
			break;
		}
		//更新deviceId
		case "refreshDeviceId":{
			int id=Integer.parseInt(request.getParameter("id"));
			String deviceId=request.getParameter("deviceId");
			UserDaoImpl dao=new UserDaoImpl();
			if (dao.refreshDeviceId(id,deviceId)){
				out.print("1");
			}else{
				out.print("0");
			}
			break;
		}
		case "ver":{
			double version=Double.parseDouble(request.getParameter("version"));
			if (version<1.128){
				List<String> list=new ArrayList<String>();
				list.add("1128");
				list.add("m");
				//list.add("本次更新非必须,请有需要的公司更新 ");
				Gson gson=new Gson();
				Type type=new TypeToken<List<String>>(){}.getType();
				out.println(gson.toJson(list,type));
			}else{
				out.print("1");
			}
			break;
		}
		}
		
				
	}
    
}
