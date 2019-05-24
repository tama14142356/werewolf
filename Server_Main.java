
import java.net.ServerSocket;
import java.net.Socket;

public class Server_Main {
	private static Server_Main shared;
	private static Server_GameSystem gameSystem;
	
	private static final int PORT = 8080;
	
	private static int numberOfApplyPeople = 6;
	
	private Server_Main() {}
	
	public static void main(String[] args) {
		shared = getInstance();
		gameSystem = Server_GameSystem.getInstance();
		shared.waitForConnection();
	}
	
	public static synchronized Server_Main getInstance() {
		if (shared == null) shared = new Server_Main();
		return shared;
	}
	
	void waitForConnection() {
		try {
			ServerSocket server = new ServerSocket(PORT);
			System.out.println("Server open");
			
			while(true) {
				try {
					Socket socket = server.accept();
					Server_ClientManager client = new Server_ClientManager(socket);
					gameSystem.addClient(client);
					System.out.println("Client Connected");
				
					if (gameSystem.clientList.size() >= numberOfApplyPeople) {
						//server.close();
						System.out.println("Stop wait for Connection");
						gameSystem.startGame();
						break;
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	
	}
}
 