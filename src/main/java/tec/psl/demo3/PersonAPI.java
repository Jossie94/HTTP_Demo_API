package tec.psl.demo3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import tec.psl.AnalyzeReq;
import tec.psl.AnalyzeResult;
import tec.psl.Data;
import tec.psl.Person;

/**
 * Servlet implementation class PersonAPI
 */
@WebServlet("/api/*")
public class PersonAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	
	private ArrayList<Person> personList;
	Data dbh;
	
	public PersonAPI() {
		super();
		dbh = new Data();
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String endpoint = request.getPathInfo();

		AnalyzeReq analyze = new AnalyzeReq(endpoint);

		ObjectMapper mapper = new ObjectMapper();

		switch (analyze.getResult()) {
		case noMatch:
			send(response, 404, "No match");
			break;
		case personMatch:
			personList = dbh.getAllPersons();
			String json = mapper.writeValueAsString(personList);
			send(response, 200, json);
			break;
		case personIDMatc:
			
			Person p = dbh.getPerson(analyze.getPersId());
			send(response, 200, mapper.writeValueAsString(p));
			break;
		}
	}
 
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String endpoint = request.getPathInfo();

		AnalyzeReq analyze = new AnalyzeReq(endpoint);
		
		if(analyze.getResult() != AnalyzeResult.personMatch) {
			send(response, 404, "");
			return;
		}
		// Hvis jeg er her er der fundet et gyldigt endpoint
		ObjectMapper mapper = new ObjectMapper();
		Person person = new Person();
		String input = read(request);
		person = mapper.readValue(input, Person.class);
		
		if(dbh.insertPerson(person)) {
			send(response, 201, mapper.writeValueAsString(person));
			return;
		}
		send(response, 200, "Insert failed");
		
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String endpoint = request.getPathInfo();

		AnalyzeReq analyze = new AnalyzeReq(endpoint);
		
		if(analyze.getResult() != AnalyzeResult.personIDMatc) {
			send(response, 404, "");
			return;
		}
		String input = read(request);
		ObjectMapper mapper = new ObjectMapper();
		Person p = mapper.readValue(input, Person.class);
		if(dbh.update(analyze.getPersId(), p)) {
			send(response, 200, "Updated");
			return;
		}
		send(response, 200, "Person med id = " + analyze.getPersId() +" findes ikke");		
		// Hvis jeg er her er der ikke fundet en person med det requestede ID
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String endpoint = request.getPathInfo();

		AnalyzeReq analyze = new AnalyzeReq(endpoint);
		
		
		if(analyze.getResult() != AnalyzeResult.personIDMatc) {
			send(response, 404, "");
			return;
		}
		if(dbh.delete(analyze.getPersId())) {
			send(response, 200, "Person med id = " + analyze.getPersId() +" Blev slettet");	
			return;
		}
		send(response, 200, "Person med id = " + analyze.getPersId() +" findes ikke");
	}
	
	private String read(HttpServletRequest request) {
		String input = "";
		
		try {
			BufferedReader in = request.getReader();
			String s;
			while((s = in.readLine()) != null) {
				input += s;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return input;
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
