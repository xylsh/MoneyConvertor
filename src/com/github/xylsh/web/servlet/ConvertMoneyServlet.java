package com.github.xylsh.web.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.xylsh.web.model.MoneyConvertor;

//@WebServlet("/ConvertMoney")
public class ConvertMoneyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ConvertMoneyServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String money = request.getParameter("money");

		try{
			MoneyConvertor moneyConvertor = new MoneyConvertor(money);
			String result = moneyConvertor.convert();
		    
			request.setAttribute("money",money);
			request.setAttribute("result",result);

		}catch(Exception e){
			//response.sendRedirect("index.jsp");    //这种做法在openshift上会转到非https的网址(即普通网址),而普通网址目前被墙(2013-8-29)
		}finally{
			RequestDispatcher view = request.getRequestDispatcher("./index.jsp");
			view.forward(request,response);
		}
	}

}
