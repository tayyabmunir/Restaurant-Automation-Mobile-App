package dbA;
public class Employee {
	
	public String name;
	public Long id;
	public char position;
	public boolean loggedIn;
	public Employee(String name, long id, char position){
		this.name= name;
		this.id=id;
		this.position=position;
	}

}
