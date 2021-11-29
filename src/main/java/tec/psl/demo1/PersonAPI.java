package tec.psl.demo1;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tec.psl.AnalyzeReq;


/**
 * Servlet implementation class PersonAPI
 */
// @WebServlet("/api/*")
public class PersonAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PersonAPI() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		String endpoint = request.getPathInfo();
		
		AnalyzeReq analyze = new AnalyzeReq(endpoint);
		
		switch(analyze.getResult()) {
		case noMatch: 
			send(response, 404, "No match");
			break;
		case personMatch:
			send(response, 200, "Match person");
			break;
		case personIDMatc:
			send(response, 200, "Match person med id " + analyze.getPersId());
			break;
		}
		
		   
		
//		PrintWriter out = response.getWriter();
		
		
//		out.print("Hello World " + endpoint);
//		out.close();
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	private void send(HttpServletResponse response, int status, String msg) {
		response.setStatus(status);
		
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(msg);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
