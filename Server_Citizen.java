
public class Server_Citizen extends Server_Job{
	Server_Citizen(Server_ClientManager client){
		super(client);
		jobname = "市民";
		isWerewolf = false;
		explanation = "市民は，特別な能力を持たない，ただの村人です．";
		nightMessage = "市民には，特別な能力はありません．朝までお待ちください．";
	}
}
