
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Server_Werewolf extends Server_Job{
	static HashMap<String, Integer> vote = new HashMap<String, Integer>();
	
	Server_Werewolf(Server_ClientManager client){
		super(client);
		jobname = "人狼";
		isWerewolf = true;
		explanation = "人狼は，村人を人狼の人数以下にすることが目標です．";
		nightMessage = "殺害したい市民を選択してください";
	}
	
	public static void initWerewolfWork() {
		synchronized(vote) {
			vote.clear();
		}
	}
	
	public static void finishWerewolfWork() {
		//投票数をソートする
		ArrayList<Map.Entry<String, Integer>> sortList = new ArrayList<Map.Entry<String, Integer>>(vote.entrySet());
		Collections.sort(sortList, new Comparator<Map.Entry<String, Integer>>() {	
			@Override
			public int compare(Map.Entry<String, Integer> v1, Map.Entry<String, Integer> v2) {
				return (Integer) v2.getValue() - (Integer) v1.getValue();
			}
		});

		//最大値と一致するクライアントにキルのフラグを立てる
		for (Server_ClientManager client : Server_GameSystem.aliveClientList) {
			if (sortList.size() == 0) break;
			
			String target = sortList.get(0).getKey();
			try {
				if (client.username.equals(target)) {
					client.isTargeted = true;	
					break;
				}
			}catch(IndexOutOfBoundsException ignore) {}
		}
	}
	
	@Override
	public void doNightWork() {
		//生きているクライアントから，人狼陣営のクライアントを除いたリストを作成する．
		ArrayList<Server_ClientManager> temp = new ArrayList<Server_ClientManager>(Server_GameSystem.aliveClientList);
		int i = 0;
		while(i < temp.size()) {
			if(temp.get(i).job.isWerewolf) {
				temp.remove(i);
			}else {
				i++;
			}
		}
		
		client.controlMessage.sendMessage(new Message("rnv", nightMessage + " " + clientListToString(temp)));
	}
	
	public void select(String clientName) {
		int count;
		
		synchronized(vote) {
			try {
				count = Objects.requireNonNull(vote.get(clientName));
			}catch(NullPointerException e) {
				count = 0;
			}
			
			vote.put(clientName, count + 1);
		}
	}
}
