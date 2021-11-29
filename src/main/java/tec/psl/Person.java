package tec.psl;

public class Person {
	
	private int id;
	private String fullName;
	private String email;
	private String note;
	
	public Person() {}
	
	public Person(int id, String fullName, String email, String note) {
		// super();
		this.id = id;
		this.fullName = fullName;
		this.email = email;
		this.note = note;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
