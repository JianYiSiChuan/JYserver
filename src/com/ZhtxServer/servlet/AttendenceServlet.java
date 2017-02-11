package com.ZhtxServer.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ZhtxServer.dao.HandSignDaoImpl;
import com.ZhtxServer.dao.RequireDaoImpl;
import com.ZhtxServer.dao.SignLocationDaoImpl;
import com.ZhtxServer.entity.SignLocation;


/**
 * Servlet implementation class AttendenceServlet
 */
@WebServlet("/AttendenceServlet")
public class AttendenceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AttendenceServlet() {
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
		
		case "sign":{
			String name=request.getParameter("name");
			double latitude=Double.parseDouble(request.getParameter("la"));
			double longitude=Double.parseDouble(request.getParameter("lo"));
			HandSignDaoImpl dao=new HandSignDaoImpl();
			int sta=dao.getStatus(db,name);
			if (sta==-1) {
				out.print("0");
				break;
			}else if (sta!=0) {
				out.print("5");
				break;
			}
			SignLocationDaoImpl dao2=new SignLocationDaoImpl();
			List<SignLocation> llist=dao2.info(db);
			if (llist==null) {
				out.print("0");
				break;
			}
			Date date=new Date();
			DateFormat sdf = new SimpleDateFormat("HHmm");
			int nowTime=Integer.parseInt(sdf.format(date).substring(0, 2))*3600
					+Integer.parseInt(sdf.format(date).substring(2))*60;
			
			boolean signable=false;
			for (SignLocation i:llist){
				double dis=Distance(longitude,latitude,i.getLongitude(),i.getLatitude());
				if ((dis<200)){
					signable=true;
					int hour=Integer.parseInt(i.getTime().substring(0, 2));
					int minute=Integer.parseInt(i.getTime().substring(2, 4));
					int signTime=hour*3600+minute*60;
					if (signTime<3600) {
						if ((nowTime<signTime)||(23*3600+signTime<nowTime)){
							if (dao.sign(db, name, 1)) out.print("4");
							else out.print("0");
							break;
						}else out.print("1");
					}else{
						if ((nowTime>signTime-3600)&&(nowTime<=signTime)){		
							if (dao.sign(db, name, 1)) out.print("4");
							else out.print("0");
						}else out.print("1");
					}
					break;
				}
			}
			if (!signable) out.print("3");
			break;
		}
	    }
	}
	public static double Distance(double long1, double lat1, double long2,  
	        double lat2) {  
	    double a, b, R;  
	    R = 6378137; // 地球半径  
	    lat1 = lat1 * Math.PI / 180.0;  
	    lat2 = lat2 * Math.PI / 180.0;  
	    a = lat1 - lat2;  
	    b = (long1 - long2) * Math.PI / 180.0;  
	    double d;  
	    double sa2, sb2;  
	    sa2 = Math.sin(a / 2.0);  
	    sb2 = Math.sin(b / 2.0);  
	    d = 2  
	            * R  
	            * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1)  
	                    * Math.cos(lat2) * sb2 * sb2));  
	    return d;  
	}
}
