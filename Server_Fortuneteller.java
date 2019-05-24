
import java.util.ArrayList;

public class Server_Fortuneteller extends Server_Job{
	Server_Fortuneteller(Server_ClientManager client){
		super(client);
		jobname = "占い師";
		isWerewolf = false;
		explanation = "占い師は，毎晩１人に対して占いを行い，その人物が人狼かどうかを調べることができます．";
		nightMessage = "占いたい相手を選択してください";
	}
	
	@Override
	public void doNightWork() {
		ArrayList<Server_ClientManager> temp = new ArrayList<Server_ClientManager>(Server_GameSystem.aliveClientList);
		int i = 0;
		
		while(i < temp.size()) {
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
	
	public void select(String clientName) {
		for (Server_ClientManager client : Server_GameSystem.aliveClientList) {
			if(client.username.equals(clientName)) {
				String insIsWerewolf = client.job.isWerewolf ? "黒" : "白";
				this.client.updateOtherClientData(clientName, insIsWerewolf);
				this.client.controlMessage.sendMessage(new Message("msg", client.username + "は" + insIsWerewolf + "です．"));
				break;
			}
		}
	}
}
