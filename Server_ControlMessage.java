
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Server_ControlMessage extends Thread{
	Socket socket;
	Server_ClientManager client;
	
	Server_ControlMessage(Server_ClientManager client, Socket socket){
		this.client = client;
		this.socket = socket;
		this.start();
	}
	
	@Override
	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			while(true) {
				Message message = new Message(in.readLine());
				System.out.println("comand: " + message.command + ", data: " + message.data);
				if (message.command.equals("msg")) {
					Server_ControlMessage.sendMessage(new Message("msg", "@" + client.username + ":" + message.data), Server_GameSystem.clientList);
				}else if (message.command.equals("vot")) {
					client.nextVote = message.data;
				}else if (message.command.equals("nvt")) {
					new Thread(() -> {
						client.job.select(message.data);
					}).start();
					
				}else if (message.command.equals("reg")) {
					boolean isUsed = false;
					
					for(Server_ClientManager client: Server_GameSystem.clientList) {
						String insUserName;
						if (client.username == null) insUserName = "";
						else insUserName = client.username;
						
						if (message.data.equals(insUserName)) {
							this.client.controlMessage.sendMessage(new Message("rqn"));
							isUsed = true;
							break;
						}
					}
					if (!isUsed) client.username = message.data;
				}else {
					System.out.println("invalid command: " + message.command + ", from: " + client.username); 
				}
			}
		}catch(NullPointerException e) {
			e.printStackTrace();
			this.client.killed("通信エラー");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(Message message) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),
					true);
			out.println(message.sendForm());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sendMessage(Message message, Socket socket) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),
					true);
			out.println(message.sendForm());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendMessage(Message message, ArrayList<Server_ClientManager> clientList) {
		clientList.forEach(client -> {
			try {
				PrintWriter out = new PrintWriter(
						new BufferedWriter(new OutputStreamWriter(client.socket.getOutputStream())), true);
				out.println(message.sendForm());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
