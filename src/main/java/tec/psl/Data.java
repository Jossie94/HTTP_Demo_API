package tec.psl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.microsoft.sqlserver.*;

public class Data {

	private String connStr = "jdbc:sqlserver://VISTI\\SQLEXPRESS;databaseName=personapi";
	private String user = "loginforpersonapi";
	private String password = "123";
	private Connection conn;
	private PreparedStatement stmt;

	public Data() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	private void connect() {
		try {
			conn = DriverManager.getConnection(connStr, user, password);
System.out.println("Connected");
		} catch (SQLException e) {
			System.out.println(e.getErrorCode());
		}
	}

	private void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public Person getPerson(int id) {
		connect();
		Person p = new Person();
		String sql = "select * from person where persid = ?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet res = stmt.executeQuery();
			if(res.next()) {
				p.setId(res.getInt("persid"));
				p.setFullName(res.getString("fullname"));
				p.setEmail(res.getString("email"));
				p.setNote(res.getString("note"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		close();
		return p;
	}

	public ArrayList<Person> getAllPersons() {
		connect();
		String sql = "select * from person";
		
		ArrayList<Person> plist = new ArrayList<>();
		try {
			stmt = conn.prepareStatement(sql);
			ResultSet res = stmt.executeQuery();
			while(res.next()) {
				Person p = new Person();
				p.setId(res.getInt("persid"));
				p.setFullName(res.getString("fullname"));
				p.setEmail(res.getString("email"));
				p.setNote(res.getString("note"));
				plist.add(p);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			System.out.println("Line 84 in getAllPersons() " + e.getMessage());
		}
		close();
		return plist;
	}

	public boolean insertPerson(Person p) {
		connect();
		boolean retVal = false;
		String sql = "insert into person (fullname, email, note) values(?,?,?)";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, p.getFullName());
			stmt.setString(2, p.getEmail());
			stmt.setString(3, p.getNote());
			if(stmt.executeUpdate() == 1) {
				retVal = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		close();
		return retVal;
	}

	public boolean update(int id, Person p) {
		connect();
		boolean retVal = false;
		String sql = "update person set fullname = ?, email = ?, note = ? where persid = ?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, p.getFullName());
			stmt.setString(2, p.getEmail());
			stmt.setString(3, p.getNote());
			stmt.setInt(4, id);
			if(stmt.executeUpdate() == 1) retVal = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		close();
		return retVal;
	}

	public boolean delete(int id) {
		connect();
		boolean retVal = false;
		String sql = "delete from person where persid = ?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			if(stmt.executeUpdate() == 1) {
				retVal = true;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		close();
		return retVal;
	}

}
