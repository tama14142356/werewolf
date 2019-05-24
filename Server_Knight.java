
import java.net.Socket;

public class Server_Knight extends Server_Job {
	Server_Knight(Server_ClientManager client) {
		super(client);
		jobname = "騎士";
		isWerewolf = false;
		explanation = "毎晩，選択した人を人狼の襲撃から守ることができる．";
		nightMessage = "守りたい人を選択してください";
	}
	
	@Override
	public void doNightWork() {
		client.controlMessage.sendMessage(new Message("rnv", nightMessage + " " + clientListToString(Server_GameSystem.aliveClientList)));
	}
	
	@Override
	public void select(String clientName) {
		for(Server_ClientManager client : Server_GameSystem.aliveClientList) {
			if(client.username.equals(clientName)) {
				client.isGuarded = true;
				break;
			}
		}
	}
}
