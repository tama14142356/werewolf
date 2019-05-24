import java.net.Socket;
import java.util.ArrayList;

public class Server_Job{
	String jobname;
	boolean isWerewolf;
	String explanation;
	String nightMessage;
	
	Socket socket;
	
	Server_ClientManager client;
	
	Server_Job(Server_ClientManager client){
		this.socket = client.socket;
		this.client = client;
	}
	
	public void doNightWork() {
	}
	
	public void select(String clientName) {}
	
	String clientListToString(ArrayList<Server_ClientManager> clientList) {
		StringBuilder buffer = new StringBuilder();
		
		for(Server_ClientManager client : clientList) {
			buffer.append(client.username);
			buffer.append(" ");
		}
		
		return buffer.toString();
	}
	
	public static void initWerewolfWork() {}
	public static void finishWerewolfWork() {}
}
