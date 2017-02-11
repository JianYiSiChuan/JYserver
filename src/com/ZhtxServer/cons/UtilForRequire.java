package com.ZhtxServer.cons;

public class UtilForRequire {
	//控件81所需要的付款对象对应的sql语句
    public static String payItem(String mode){
    	switch (mode){
    	case "支付应付账款":{
    		return "SELECT DISTINCT(`client`) FROM `product_stock` "
        			+ "union SELECT `content` from `provider` ";
    	}
    	case "支付运费":{
    		return "SELECT `name` FROM `driver`";
    	}	
    	case "银行交易费用":{
    		return "SELECT `content` FROM `loan_object`";
    	}	
    	case "支付工资":{
    		return "SELECT `name` from `attendence_item`";
    	}	
    	case "支付报销款":{
    		return "SELECT `name` from `attendence_item`";
    	}
    	case "借款给他人":{
    		return "SELECT `content` FROM `loan_object`";
    	}
    	case "向他人还款":{
    		return "SELECT `content` FROM `loan_object`";
    	}
    	case "银行贷款利息":{
    		return "SELECT `content` FROM `loan_object`";
    	}
    	case "支付税金":{
    		return "SELECT `content` from `provider`";
    	}
    	case "向客户退款":{
    		return "SELECT DISTINCT(`client`) FROM `product_sell`";
    	}
    	}
    	return null;
    }
    //控件91所需要的付款对象对应的sql语句
    public static String collectItem(String mode){
    	switch (mode){
    	case "收回供应商退款":{
    		return "SELECT DISTINCT(`client`) FROM `product_stock` "
        			+ "union SELECT `content` from `provider` ";
    	}
    	case "银行利息收入":{
    		return "SELECT `content` FROM `loan_object`";
    	}	
    	case "收取应收账款":{
    		return "SELECT DISTINCT(`client`) FROM `product_sell`";
    	}	
    	case "收回借出款":{
    		return "SELECT `content` FROM `loan_object`";
    	}	
    	case "从他人处借款":{
    		return "SELECT `content` FROM `loan_object`";
    	}
    	case "营业外收入":{
    		return "SELECT DISTINCT(`client`) FROM `product_sell`";
    	}
    	}
    	return null;
    }
}
