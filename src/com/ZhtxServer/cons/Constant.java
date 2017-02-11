package com.ZhtxServer.cons;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Constant {
    public static final String[] action={"","","","reimbursement","sell","stock","countsalary","payment"
    		,"collection","transmoney","cashsell","reimburse_credit","transinventory","material_inventory"
    		,"product_inventory","transdebt","fixedasset_in","fixedasset_out","accountadjust","returngoods"};
    public static final String savePath="C:\\Program Files (x86)\\apache-tomcat-7.0.40-windows-x64\\apache-tomcat-7.0.40\\webapps\\ZhtxServer\\";
    public static final long appKeyForPush=23490153;
    public static final String accessKeyForPush="LTAIMeMMACt1lq9X";
    public static final String accessKeySecretForPush="coyfFWRCNA4fCEH1HXIH3mFdy6eq0P";
    public static String createProid(){
    	Date date=new Date();
    	DateFormat sdf = new SimpleDateFormat("yyMMddHHmm");
    	String s=sdf.format(date);
    	s.substring(1,8);
    	int x=(int)(Math.random()*8999)+1000;
    	s=s+String.valueOf(x);
		return s;
    }
    
    public static String randomNum(){
    	int x=(int)(Math.random()*8999)+1000;
    	String s=String.valueOf(x);
		return s;
    }
    
    public static boolean deleteFile(String dir,String key){
    	String path=Constant.savePath+dir;
		File csvFiles = new File(path);
		String fileList[]=csvFiles.list();
		for (String filename:fileList){
			if (filename.indexOf(key) != -1){
				File temp= new File(path+filename);
	    		if (!temp.delete()) return false;
			}
		}
		return true;
	}
    
   
}
