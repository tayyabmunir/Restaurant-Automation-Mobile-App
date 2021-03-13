
package msgControl;

public class Msg {
	public char senderPosition;
	public long senderEmpID;
	public char receiverPosition;
	public long receiverEmpID;
	public String content;
	public Msg(char recPos, long recID, String mess){
		senderPosition = '1';
		senderEmpID = -1;
		receiverPosition =recPos;
		receiverEmpID = recID;
		content = mess;
	}
	public Msg(){}
	public String toString(){
		String ans = senderPosition +""+senderEmpID+"*"+receiverPosition+receiverEmpID+"*"+content;
		return ans;
	}
	
	public static Msg fromString(String string){
		if(string==null || string.length()<4){
			return null;
		}
		Msg ans = new Msg();
		ans.senderPosition = string.charAt(0);
		string = string.substring(1);
		int indexStar = string.indexOf('*');
		String id = string.substring(0, indexStar);
		int idN = Integer.parseInt(id);
		ans.senderEmpID = idN;
		string = string.substring(indexStar+1);
		
		ans.receiverPosition = string.charAt(0);
		string = string.substring(1);
		indexStar = string.indexOf('*');
		id = string.substring(0, indexStar);
		idN = Integer.parseInt(id);
		ans.receiverEmpID = idN;
		string = string.substring(indexStar+1);
		ans.content = string;
		return ans;
		
		
	}
}
