import java.net.Socket;
import java.util.ArrayList;

public class Server_Psychic extends Server_Job{
	Server_Psychic(Server_ClientManager client){
		super(client);
		jobname = "霊媒師";
		isWerewolf = false;
		explanation = "霊媒師は，死んだ人が人狼であったかどうか調べることができます．";
		nightMessage = "調べたい相手を一人選択してください．";
	}
	
	@Override
	public void doNightWork() {
		ArrayList<Server_ClientManager> temp = new ArrayList<Server_ClientManager>(Server_GameSystem.deadClientList);
		int i = 0;
		
		while(i < temp.size())  {
			if (temp.size() == 0) break;
			
			String username = temp.get(i).username;
			if(!this.client.otherClientData.get(username).equals("不明")) {
				temp.remove(i);
			}else {
				i++;
			}
		}
		
		client.controlMessage.sendMessage(new Message("rnv", nightMessage + " " + clientListToString(temp)));
	}
	
	@Override
	public void select(String clientName) {
		for (Server_ClientManager client : Server_GameSystem.deadClientList) {
			if (client.username.equals(clientName)) {
				String insIsWerewolf = client.job.isWerewolf ? "黒" : "白";
				this.client.updateOtherClientData(clientName, insIsWerewolf);
				this.client.controlMessage.sendMessage(new Message("msg", clientName + "は" + insIsWerewolf + "でした．"));
				break;
			}
		}
	}
}
