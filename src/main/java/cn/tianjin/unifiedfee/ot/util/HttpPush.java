package cn.tianjin.unifiedfee.ot.util;

import javax.servlet.http.HttpServletResponse;

public class HttpPush {
	//跨域访问
	public static void responseInfo(HttpServletResponse response){
//		response.setContentType("text/html;charset=UTF-8;");
		response.addHeader("Access-Control-Allow-Origin","*");
		response.addHeader("Access-Control-Allow-Methods","POST, GET, OPTIONS,DELETE,PUT");
	}
}
