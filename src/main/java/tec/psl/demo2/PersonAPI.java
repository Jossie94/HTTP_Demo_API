package tec.psl.demo2;

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
	
	public PersonAPI() {
		super();
		// TODO Auto-generated constructor stub
		createTestData();
	}

	private void createTestData() {
		
		Person person1 = new Person(1001, "Anders And", "aa@andeby.dk", "Hidsigprop");
		Person person2 = new Person(1002, "Rip And", "rip@andeby.dk", "Spasmager");
		Person person3 = new Person(1003, "Rap And", "rap@andeby.dk", "Spasmager");
		Person person4 = new Person(1004, "Rup And", "rap@andeby.dk", "Spasmager");
		Person person5 = new Person(1005, "Anders And", "aa@andeby.dk", "Hidsigprop");
		Person person6 = new Person(1006, "Ziegelveit B. Zhonk", "zbz@andeby.dk", "Overborgmester");
		
		personList = new ArrayList<>();
		personList.add(person1);
		personList.add(person2);
		personList.add(person3);
		personList.add(person4);
		personList.add(person5);
		personList.add(person6);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());

		String endpoint = request.getPathInfo();

		AnalyzeReq analyze = new AnalyzeReq(endpoint);

		

		ObjectMapper mapper = new ObjectMapper();

		switch (analyze.getResult()) {
		case noMatch:
			send(response, 404, "No match");
			break;
		case personMatch:
			String json = mapper.writeValueAsString(personList);
			send(response, 200, json);
			break;
		case personIDMatc:
			// Finde object der har id = 1002
			for (Person p : personList) {
				if (p.getId() == analyze.getPersId()) {
					send(response, 200, mapper.writeValueAsString(p));
					return;
				}
			}
//			send(response, 200, "Match person med id " + analyze.getPersId());
			send(response, 200, "Person med id = " + analyze.getPersId() +" findes ikke");

			break;
		}

//		PrintWriter out = response.getWriter();

//		out.print("Hello World " + endpoint);
//		out.close();

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
		
		personList.add(person);
		
		send(response, 201, mapper.writeValueAsString(person));
		
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
		
		for(Person p: personList) {
			if(p.getId() == analyze.getPersId()) {
				mapper.readerForUpdating(p).readValue(input);
				send(response, 200, "Updated");
				return;
			}
		}
		// Hvis jeg er her er der ikke fundet en person med det requestede ID
		send(response, 200, "Person med id = " + analyze.getPersId() +" findes ikke");
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String endpoint = request.getPathInfo();

		AnalyzeReq analyze = new AnalyzeReq(endpoint);
		
		if(analyze.getResult() != AnalyzeResult.personIDMatc) {
			send(response, 404, "");
			return;
		}
		for(Person p: personList) {
			if(p.getId() == analyze.getPersId()) {
				if(personList.remove(p)) {
					send(response, 200, "Removed person with id = " + analyze.getPersId());
					return;
				}
			}
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
