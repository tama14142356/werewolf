
//2019/05/24 9:26　高橋　那弥

import java.io.*;
import java.net.*;
import java.util.*;

public class Client_ControlMessage extends Thread {
	Client_Main client;
	Socket socket;
	boolean stoprunning = false;

	Client_ControlMessage(Client_Main client, Socket socket) {
		this.client = client;
		this.socket = socket;
		this.start();
	}

	public void stopRunning() {
		client.chatwindow.chatRoom.append("チャット終了します..." + "\n");
		stoprunning = true;
	}

	@Override
	public synchronized void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			/*if (socket == null)
				client.chatwindow.chatRoom.append("not connect\n");
			if (socket.isClosed())
				client.chatwindow.chatRoom.append("closed\n");*/
			while (!stoprunning) {
				Message message = new Message(in.readLine());
				//System.out.println("@commnd: " + message.command + " data: " + message.data);

				if (message.command.equals("msg")) {
					client.chatwindow.ReceiveMessage(message.data);
				} else if (message.command.equals("fin")) {
					client.chatwindow.ReceiveMessage(message.data);
				} else if (message.command.equals("ngt")) {
					ChatWindow.nowtime = 2;
					Scanner sc = new Scanner(message.data);
					String night = sc.next();
					while (sc.hasNext())
						night += " " + sc.next();
					sc.close();
					if (client.getJobName().equals("市民"))
						client.chatwindow.ReceiveMessage(night);
					client.votewindow.setvotelabel(night);
				} else if (message.command.equals("rqn")) {
					ChatWindow.nowtime = 2;
					client.getcontrolmessage().sendMessage(new Message("reg", client.getName()));
				} else if (message.command.equals("day")) {
					ChatWindow.nowtime = 0;
					client.startGame();
				} else if (message.command.equals("kil")) {
					client.deadOrAlive = 1;
					client.chatwindow.ReceiveMessage(message.data);
					// stopRunning();
				} else if (message.command.equals("rqv")) {
					ChatWindow.nowtime = 1;
					client.chatwindow.UpdateScene(1);
					Scanner sc = new Scanner(message.data);
					String title = sc.next();
					sc.close();
					client.votewindow.setvotelabel(title);
					String canvotelist = message.data.substring(title.length() + 1);
					client.votewindow.setCanvote(canvotelist);
					client.votewindow.Showvote();
				} else if (message.command.equals("rnv")) {
					client.chatwindow.UpdateScene(2);
					ChatWindow.nowtime = 2;
					Scanner sc = new Scanner(message.data);
					String label = sc.next();
					client.votewindow.setvotelabel(label);
					String canvotelist = "";
					if (sc.hasNext())
						canvotelist = message.data.substring(label.length() + 1);
					sc.close();
					client.votewindow.setCanvote(canvotelist);
					client.votewindow.Showvote();
				} else if (message.command.equals("tim")) {
					client.chatwindow.Updatetime(message.data);
				} else if (message.command.equals("doa")) {
					client.chatwindow.Updatemember(message.data);
				} else if (message.command.equals("job")) {
					client.setJobName(message.data);
				} else if (message.command.equals("ocd")) {
					client.chatwindow.UpdateCanSee(message.data);
				} else {
					System.out.println(message.command);
					System.err.println("invalid command");
				}
			}

		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public boolean sendMessage(Message message) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),
					true);
			out.println(message.sendForm());
			return true;
		} catch (Exception ignore) {
			return false;
		}
	}
}
