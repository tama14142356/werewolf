
//2019/5/24 8:48　高橋　那弥

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

public class Client_Main {
	static final String deadOrAlivename[] = { "生存", "死亡" };
	static private Client_Main client;
	protected ChatWindow chatwindow;
	protected VoteWindow votewindow;
	private Socket socket;
	private String username;
	private Client_ControlMessage controlMessage;
	private String jobname;
	private String jobexplanation;
	public List<String> aliveList = new ArrayList<String>();
	public List<String> deadList = new ArrayList<String>();
	public List<ArrayList<String>> CanSeeList = new ArrayList<ArrayList<String>>();
	protected int deadOrAlive;

	public static void main(String[] args) {
		client = new Client_Main(args[0]);
		client.chatwindow = ChatWindow.getInstance();
		client.chatwindow.Showwindow();
		client.votewindow = VoteWindow.getInstance();
	}

	private Client_Main(String name) {
		username = name;
		deadOrAlive = 0;
		jobname = "未定";
		jobexplanation = "まだ情報はありません";
	}

	public void setJobName(String job) {
		Scanner sc = new Scanner(job);
		int i = 0;
		while (sc.hasNext()) {
			String jobname = sc.next();
			if (i == 0)
				this.jobname = jobname;
			else
				jobexplanation = jobname;
			++i;
		}
		sc.close();
		chatwindow.setExplanation();
	}
	

	public synchronized static Client_Main getInstance(String name) {
		if (client == null)
			client = new Client_Main(name);
		return client;
	}

	public static Socket getSocket() {
		return client.socket;
	}

	public String getName() {
		return username;
	}

	public String getExplanation() {
		return jobexplanation;
	}

	public String getJobName() {
		return jobname;
	}

	public Client_ControlMessage getcontrolmessage() {
		return controlMessage;
	}



	private void requestConnection() throws IOException {
		String hostname = "localhost";
		InetAddress addr = InetAddress.getByName(hostname);
		socket = new Socket(addr, 8080);
		controlMessage = new Client_ControlMessage(client, socket);
	}

	public void startConnection() {
		try {
			client.requestConnection();
			Message sendusername = new Message("reg" , username);
			//client.controlMessage.sendMessage(sendusername);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startGame() {
		chatwindow.UpdateScene(0);
	}

}
