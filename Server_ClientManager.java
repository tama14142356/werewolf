import java.net.Socket;
import java.util.HashMap;
import java.util.Map.Entry;

public class Server_ClientManager{
	Socket socket;
	
	String username;
	Server_Job job;
	boolean isAlive = true;
	
	boolean isTargeted = false;
	boolean isGuarded = false;
	HashMap<String, String> otherClientData;
	String nextVote = "";
	
	Server_ControlMessage controlMessage;
	
	public Server_ClientManager(Socket socket) {
		this.socket = socket;
		
		otherClientData = new HashMap<String, String>();
		
		controlMessage = new Server_ControlMessage(this, socket);
		controlMessage.sendMessage(new Message("rqn",""));
	}
	
	public void killed(String message){
		isAlive = false;
		Server_GameSystem.aliveClientList.remove(this);
		Server_GameSystem.deadClientList.add(this);
		
		if(job.isWerewolf) Server_GameSystem.numberOfWerewolf--;
		else Server_GameSystem.numberOfPeople--;
		
		controlMessage.sendMessage(new Message("kil", message));
		Server_ControlMessage.sendMessage(new Message("msg", username + "が殺されました．"), Server_GameSystem.clientList);
	}
	
	public boolean checkDeadOrAlive() {
		System.out.println(username + ": isTargeted=" + isTargeted + ",isGuarded=" + isGuarded);
		
		if(isTargeted && !isGuarded) {
			killed("あなたは人狼によって殺されました．");
			return false;
		}
		
		isTargeted = false;
		isGuarded = false;
		
		return true;
	}
	
	public void updateOtherClientData(String name, String explanation) {
		otherClientData.put(name, explanation);
		controlMessage.sendMessage(new Message("ocd", otherClientDataToString()));
	}
	
	private String otherClientDataToString() {
		StringBuilder buffer = new StringBuilder();
		
		for(Entry<String, String> entry: otherClientData.entrySet()) {
			buffer.append(entry.getKey() + ",");
			buffer.append(entry.getValue() + " ");
		}
		
		return buffer.toString();
	}
}
