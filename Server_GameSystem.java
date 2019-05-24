
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class Server_GameSystem {
	private static Server_GameSystem shared;
	
	static ArrayList<Server_ClientManager> clientList;
	static ArrayList<Server_ClientManager> aliveClientList;
	static ArrayList<Server_ClientManager> deadClientList;
	
	static int numberOfPeople;
	static int numberOfWerewolf;
	
	private Server_GameSystem() {
		clientList = new ArrayList<Server_ClientManager>();
		aliveClientList = new ArrayList<Server_ClientManager>();
		deadClientList = new ArrayList<Server_ClientManager>();
	}
	
	public static synchronized Server_GameSystem getInstance() {
		if (shared == null) shared = new Server_GameSystem();
		return shared;
	}
	
	public void startGame() {
		System.out.println("Start Game");
		
		//ユーザーネームの受け取りを待機する．
		while (true) {
			boolean ready = true;
			for(Server_ClientManager client: clientList) {
				if(client.username == null) {
					ready = false;
					try {
						Thread.sleep(50);
					}catch(Exception ignore) {}
					
					break;
				}
			}
			
			if (ready) break;
		}
		
		//職業を割り振る
		assignJob();
		
		//クライアントの他のクライアントの情報を初期化
		clientList.forEach(client -> {
			ArrayList<Server_ClientManager> temp = new ArrayList<Server_ClientManager>(clientList);
			temp.remove(client);
			
			temp.forEach(otherClient -> {
				if (client.job.isWerewolf == true && otherClient.job.isWerewolf == true) {
					client.updateOtherClientData(otherClient.username, "人狼");
				}else {
					client.updateOtherClientData(otherClient.username, "不明");
				}
			});
			
			client.updateOtherClientData(client.username,client.job.jobname);
		});
		
		//各クライアントに，生死情報を送信する
		Server_ControlMessage.sendMessage(new Message("doa", doaToString()), clientList);
		
		//各クライアントに，ゲームの開始を通知する．
		Server_ControlMessage.sendMessage(new Message("day",""), clientList);
		
		//ゲームシステムを起動する
		timeCycleController();
	}
	
	private void timeCycleController() {
		System.out.println("Start Time cycle");
		while(true) {
			day();
			if(evening() == 1) break;
			if(night() == 1) break;
		}
		
		finish();
	}
	
	private void day() {
		System.out.println("Start day");
		Server_ControlMessage.sendMessage(new Message("day", ""), clientList);
		timer(15);
		
		try {
			Thread.sleep(15000);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return;
	}
	
	private int evening() {
		System.out.println("Start evening");
		
		//投票を行う
		vote();
		
		//各クライアントに，生死情報を送信する
		Server_ControlMessage.sendMessage(new Message("doa", doaToString()), clientList);
		
		//ゲームの終了条件を確認する
		if(checkEndOfGame()) return 1;
		else return 0;
	}
	
	private int night() {
		System.out.println("Start night");
		
		//人狼の処理を初期化する
		Server_Werewolf.initWerewolfWork();
		
		//全てのクライアントに，夜の仕事を行うよう要請する
		aliveClientList.forEach(client -> {
			client.controlMessage.sendMessage(new Message("ngt", "夜になりました. " + client.job.nightMessage));
			new Thread(() -> {
				client.job.doNightWork();
			}).start();
		});
		
		//死んだクライアントにメッセージを送信する
		deadClientList.forEach(client ->{
			client.controlMessage.sendMessage(new Message("ngt", "夜になりました．"));
		});
		
		timer(60);
		
		try {
			Thread.sleep(60000);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		//人狼の業務を終了する
		Server_Werewolf.finishWerewolfWork();
		
		//全てのクライアントに対して，生きているかどうか確認する
		int i = 0;
		while(i < aliveClientList.size()) {
			if(aliveClientList.get(i).checkDeadOrAlive()) i++;
		}
		
		//各クライアントに，生死情報を送信する
		Server_ControlMessage.sendMessage(new Message("doa", doaToString()), clientList);
		
		//ゲームの終了条件を確認する．
		if (checkEndOfGame()) return 1;
		else return 0;
	}
	
	private void finish() {
		clientList.clear();
		aliveClientList.clear();
		deadClientList.clear();
		
		Server_Main.getInstance().waitForConnection();
	}
	
	private void assignJob() {
		System.out.println("Assign job");
		
		//人数を設定
		numberOfPeople = 4;
		numberOfWerewolf = 2;
		
		//一意な乱数の生成
		ArrayList<Integer> random = new ArrayList<Integer>() {
			{
				for(int i = 0; i < 6; i++) add(i);
			}
		};
		Collections.shuffle(random);
		
		//職業の割り振り
		for(int i = 0; i < 6; i++) {
			switch(random.get(i)) {
			case 0:
				clientList.get(i).job = new Server_Citizen(clientList.get(i));
				break;
				
			case 1:
				clientList.get(i).job = new Server_Werewolf(clientList.get(i));
				break;
				
			case 2:
				clientList.get(i).job = new Server_Psychic(clientList.get(i));
				break;
				
			case 3:
				clientList.get(i).job = new Server_Fortuneteller(clientList.get(i));
				break;
				
			case 4:
				clientList.get(i).job = new Server_Knight(clientList.get(i));
				break;
				
			case 5:
				clientList.get(i).job = new Server_Werewolf(clientList.get(i));
				break;
				
			default:
				System.err.println("Invalid Job: Job number=" + random.get(i));
				System.exit(1);
			}
		}
		
		clientList.forEach(client -> {
			client.controlMessage.sendMessage(new Message("job", client.job.jobname + " " + client.job.explanation));
		});
	}
	
	private void vote() {
		System.out.println("Start vote");
		
		//各クライアントに，投票を要求
		aliveClientList.forEach(client -> {
			ArrayList<Server_ClientManager> temp = new ArrayList<Server_ClientManager>(aliveClientList);
			temp.remove(client);
			client.controlMessage.sendMessage(new Message("rqv", "吊りたい相手に投票してください. " + clientListToString(temp)));
		});
		
		timer(30);
		try {
			Thread.sleep(30000);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		//投票先と，投票数のマップを作成
		HashMap<String, Integer> votes = new HashMap<String, Integer>();
		aliveClientList.forEach(client -> {
			int count;
			
			//投票をしたことを確認
			if(!client.nextVote.equals("")) {
				try {
					count = Objects.requireNonNull(votes.get(client.nextVote));
				}catch(NullPointerException e) {
					count = 0;
				}
				
				votes.put(client.nextVote, count + 1);
			}
		});
		
		//投票数によって，マップをソート
		ArrayList<Map.Entry<String, Integer>> sortList = new ArrayList<Map.Entry<String, Integer>>(votes.entrySet());
		Collections.sort(sortList, new Comparator<Map.Entry<String, Integer>>(){
			@Override
			public int compare(Map.Entry<String, Integer> v1, Map.Entry<String, Integer> v2) {
				return (Integer) v2.getValue() - (Integer) v1.getValue();
			}
		});
		
		//誰も投票をしていなければリターン
		if (sortList.size() == 0) return;
		
		//1番目の投票を得たクライアントをkillする
		for(Server_ClientManager client : aliveClientList) {
			if (client.username.equals(sortList.get(0).getKey())) {
				System.out.println(client.username + "is killed");
				client.killed("あなたは，投票によって殺されました");
				break;
			}
		}
	}
	
	private String clientListToString(ArrayList<Server_ClientManager>clientList) {
		StringBuilder buffer = new StringBuilder();
		
		clientList.forEach(client ->{
			buffer.append(client.username);
			buffer.append(" ");
		});
		
		return buffer.toString();
	}
	
	private String doaToString() {
		StringBuilder buffer = new StringBuilder();
		
		clientList.forEach(client -> {
			String deadOrAlive = client.isAlive ? "生存" : "死亡";
			buffer.append(client.username + ",");
			buffer.append(deadOrAlive + " ");
		});
		
		return buffer.toString();
	}
	
	private boolean checkEndOfGame() {
		//人狼の勝利の場合
		if(numberOfPeople <= numberOfWerewolf) {
			System.out.println("Werewolf win the game");
			
			clientList.forEach(client ->{
				String result = client.job.isWerewolf ? "勝ち" :  "負け";
				client.controlMessage.sendMessage(new Message("fin", result));
			});
			
			Server_ControlMessage.sendMessage(new Message("msg", "人狼陣営の勝利です"), clientList);
			
			return true;
		}
		
		//村人の勝利の場合
		if (numberOfWerewolf <= 0) {
			System.out.println("Villeger win the game");
			clientList.forEach(client ->{
				String result = client.job.isWerewolf ? "負け" :  "勝ち";
				client.controlMessage.sendMessage(new Message("fin", result));
			});
			
			Server_ControlMessage.sendMessage(new Message("msg", "市民陣営の勝利です"), clientList);
			
			return true;
		}
		
		return false;
	}
	
	private void timer(final int setTime) {
		Timer timer = new Timer();
		
		TimerTask timertask = new TimerTask() {
			int sec = setTime;
			
			@Override
			public void run() {
				Server_ControlMessage.sendMessage(new Message("tim", String.valueOf(sec)), clientList);
				sec--;
				
				if(sec < 0) {
					Server_ControlMessage.sendMessage(new Message("tim", "-"), clientList);
					timer.cancel();
				}
			}
		};
		
		timer.scheduleAtFixedRate(timertask, 0, 1000);
	}
	
	public void addClient(Server_ClientManager client) {
		clientList.add(client);
		aliveClientList.add(client);
	}
}
