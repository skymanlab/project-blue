package Servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import Developer.DeveloperManager;
import Enums.PrintType;
import Enums.RequestType;
import HttpNetwork.HttpConnector;
import HttpNetwork.HttpManager;
import HttpRequest.HttpParameterData;
import HttpResponse.ResponseTokenData;
import HttpResponse.ResponseUserInfoData;


@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public LoginServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// step 0. html encoding setting / request, response : UTF-8
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");

		// step 1. Query String parsing - Response
		String code = request.getParameter("code");
		String error_description = request.getParameter("error_description");
		String error = request.getParameter("error");

		// Access Token Request ( 카카오 로그인 / REST API / 인증 코드 받기 )
		// ===============================================================================
		// step 2. Request - Post Method : Access Token
		HttpConnector connector_request_1 = new HttpConnector(
				HttpParameterData.mappingQueryStringAccessTokenReceive(code), RequestType.POST, "Json");
		HttpManager manager_request_1 = new HttpManager(connector_request_1);

		// step 3. Json Object Receive - Response : Access Token
		String access_token_data = manager_request_1.startManager();
		DeveloperManager.printDeveloperMessage(access_token_data);

		// step 4. Json Object parsing : Access Token
		Gson gson = new Gson();
		ResponseTokenData responseTokenData = gson.fromJson(access_token_data, ResponseTokenData.class);
		responseTokenData.printParameter();

		// step 5. session - ResponseTokenData object : Access Token
		request.getSession().setAttribute("session_token", responseTokenData);

		// User Info Request ( 카카오 로그인 / REST API / 사용자 관리 )
		// ===============================================================================
		// step 6. Request - setting HashMap to make : User Info
		HashMap<String, String> setting = new HashMap<String, String>();
		setting.put("Authorization", "Bearer " + responseTokenData.getAccess_token());
		setting.put("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		// step 7. Request - GET of POST method :User Info
		HttpConnector connector_request_2 = new HttpConnector(HttpParameterData.mappingQueryStringUserInfoReceive(),
				RequestType.GET, "Json", setting);
		HttpManager manager_request_2 = new HttpManager(connector_request_2);

		// step 8. Json Object Receive - Response : User Info
		String user_info_data = manager_request_2.startManager();
		DeveloperManager.printDeveloperMessage(user_info_data);

		// step 9. Json Object parsing : User Info
		ResponseUserInfoData responseUserInfoData = gson.fromJson(user_info_data, ResponseUserInfoData.class);
		responseUserInfoData.printParameter();

		// step 10. session - ResponseUserInfoData object : User Info
		request.getSession().setAttribute("session_user_info", responseUserInfoData);
		DeveloperManager.printDeveloperMessage("LoginServlet : " + responseUserInfoData.getId());

		// step 11. next page - main.jsp
		// ===============================================================================
		response.sendRedirect("index.jsp");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
