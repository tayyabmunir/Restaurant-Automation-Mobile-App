package dbB;
public class Table {
	
	public int tableNumber;
	public char status;
	public String waiter;
	public int maxOccupancy;
	public char type;
	public Table(int tableNumber, int maxOccupancy, char type){
		this.tableNumber=tableNumber;
		status='r';
		this.maxOccupancy=maxOccupancy;
		this.type=type;
	}
	
	public boolean seat(String waiter){
		if(status!='r'){
			return false;
		}
		this.waiter=waiter;
		this.status='s';
		return true;
	}
	public boolean changeStatus(char status){
		if(status=='r'||status=='p'||status=='s'){
			this.status=status;
			return true;
		}
		return false;
	}
}
