
//2019/5/24 8:48　高橋　那弥

import java.util.Scanner;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class ChatWindow extends JFrame {
	Client_Main client;

	private static ChatWindow frame;
	static final String condition[] = {"昼", "夕方", "夜"};
	JButton sendButton;
	JButton connect_button;
	JTextArea text;
	JTextArea chatRoom;
	JTextArea canSeeRoom;
	private final static String title = "人狼ゲーム";
	JTextArea memberRoom;
	private JTextArea explanationRoom;
	private JLabel timeLabel;
	private JLabel nameLabel;
	int time;

	protected static int nowtime = 0;// 0:day,2:night, 1:evening

	public void Showwindow() {
		frame = getInstance();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static ChatWindow getInstance() {
		if (frame == null) {
			frame = new ChatWindow();
			return frame;
		}
		return frame;
	}

	public void exitWidow(String killmsg) {
		chatRoom.append(killmsg);
		chatRoom.append("チャット終了します...");
		frame.dispose();
	}

	public void setExplanation() {
		explanationRoom.setText("");
		explanationRoom.append(client.getExplanation());
	}
	
	public void Updatetime(String time) {
		String timer = "残り" + time + "秒";
		timeLabel.setText(condition[nowtime] + "," + timer);
		nameLabel.setText(client.getName() + " : " + client.getJobName() + ", "
				+ Client_Main.deadOrAlivename[client.deadOrAlive]);
		if(time.equals("-"))client.votewindow.exitWindow();
	}

	public void UpdateScene(int nowtime) {
		ChatWindow.nowtime = nowtime;
		timeLabel.setText(condition[nowtime] + "," + String.valueOf(time));
	}
	
	public void UpdateCanSee(String data) {
		canSeeRoom.setText("");
		Scanner sc = new Scanner(data);
		while(sc.hasNext()) {
			String tmp = sc.next();
			String user[] = tmp.split(",");
			if(user[0].equals(client.getName()))continue;
			for(int i = 0; i < user.length;++i) {
				canSeeRoom.append(user[i]);
				if(i < user.length - 1) {
					canSeeRoom.append(": ");
				}
			}
			canSeeRoom.append("\n");
		}
		sc.close();
	}

	// alivelist,deadlist update
	public void Updatemember(String list) {
		memberRoom.setText("");
		Scanner sc = new Scanner(list);
		while(sc.hasNext()) {
			String userlist = sc.next();
			String user[] = userlist.split(",");
			if(user[0].equals(client.getName()))continue;
			for(int i = 0; i < user.length; ++i) {
				memberRoom.append(user[i]);
				if(i < user.length - 1) {
					memberRoom.append(": ");
				}
			}
			memberRoom.append("\n");
		}
		sc.close();
		Updatetime("-");
	}
	
	public void ReceiveMessage(String message) {
		chatRoom.append(message + "\n");
	}

	private ChatWindow() {
		client = Client_Main.getInstance(title);
		setTitle(title);
		setBounds(100, 100, 450, 530);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowListener() {
			public void windowClosing(WindowEvent event) {
				JLabel labelmsg = new JLabel("人狼ゲームを棄権しますか");
				int option = JOptionPane.showConfirmDialog(frame, labelmsg, "棄権", JOptionPane.YES_NO_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
				if (option == JOptionPane.YES_OPTION)
					frame.dispose();
			}

			public void windowActivated(WindowEvent arg0) {
			}

			public void windowClosed(WindowEvent arg0) {
			}

			public void windowDeactivated(WindowEvent arg0) {
			}

			public void windowDeiconified(WindowEvent arg0) {
			}

			public void windowIconified(WindowEvent arg0) {
			}

			public void windowOpened(WindowEvent arg0) {
			}
		});

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		String username = client.getName();
		String jobname = client.getJobName();
		String deadOrAlive = Client_Main.deadOrAlivename[client.deadOrAlive];
		nameLabel = new JLabel(username + " : " + jobname + ", " + deadOrAlive);
		nameLabel.setFont(new Font(null, Font.BOLD, 16));
		nameLabel.setAlignmentX(0.5f);
		topPanel.add(nameLabel);
		if (nowtime > 2) {
			System.err.println("day error");
			System.exit(1);
		}
		String now = condition[nowtime];
		String timer = "残り ー 秒";
		timeLabel = new JLabel(now + ", " + timer);
		timeLabel.setAlignmentX(0.5f);
		topPanel.add(timeLabel);
		
		JPanel explanationPanel = new JPanel();
		explanationPanel.setMaximumSize(new Dimension(450, 100));

		String explanationtitle = "仕事内容";
		JLabel explanationLabel = new JLabel(explanationtitle);
		explanationRoom = new JTextArea();
		explanationRoom.setLineWrap(true);
		explanationRoom.setEnabled(false);
		JScrollPane explanationScrollPane = new JScrollPane(explanationRoom);
		explanationScrollPane.setPreferredSize(new Dimension(350, 30));
		explanationPanel.add(explanationLabel);
		explanationPanel.add(explanationScrollPane);
		explanationRoom.append(client.getExplanation());
		explanationRoom.setDisabledTextColor(Color.BLUE);
		
		JPanel listPanel = new JPanel();
		listPanel.setMinimumSize(new Dimension(450, 115));	
		
		JPanel canSeePanel = new JPanel();
		canSeePanel.setPreferredSize(new Dimension(200, 115));
		String canSeeTitle = "他メンバーの情報";
		JLabel canSeeLabel = new JLabel(canSeeTitle);
		canSeeRoom = new JTextArea();
		canSeeRoom.setLineWrap(true);
		canSeeRoom.setEnabled(false);
		JScrollPane canSeeScrollpane = new JScrollPane(canSeeRoom);
		canSeeScrollpane.setPreferredSize(new Dimension(200, 90));
		canSeePanel.add(canSeeLabel);
		canSeePanel.add(canSeeScrollpane);
		canSeeRoom.setDisabledTextColor(Color.RED);

		JPanel memberPanel = new JPanel();
		memberPanel.setPreferredSize(new Dimension(200, 115));
		String memberTitle = "メンバーリスト";
		JLabel memberLabel = new JLabel(memberTitle);
		memberRoom = new JTextArea();
		memberRoom.setLineWrap(true);
		memberRoom.setEnabled(false);
		memberRoom.setDisabledTextColor(Color.BLACK);
		JScrollPane memberScrollpane = new JScrollPane(memberRoom);
		memberScrollpane.setPreferredSize(new Dimension(200, 90));
		memberPanel.add(memberLabel);
		memberPanel.add(memberScrollpane);

		for (String name : client.aliveList)
			memberRoom.append("alive : " + name + "\n");
		for (String name : client.deadList)
			memberRoom.append("dead : " + name + "\n");

		topPanel.add(explanationPanel);
		listPanel.add(canSeePanel);
		listPanel.add(memberPanel);

		chatRoom = new JTextArea();
		chatRoom.setLineWrap(true);
		chatRoom.setEnabled(false);
		chatRoom.setDisabledTextColor(Color.BLACK);
		JScrollPane chatScrollpane = new JScrollPane(chatRoom);
		chatScrollpane.setPreferredSize(new Dimension(400, 250));

		text = new JTextArea();
		text.setLineWrap(true);
		JScrollPane textScrollpane = new JScrollPane(text);
		textScrollpane.setPreferredSize(new Dimension(350, 35));

		sendButton = new JButton("送信");
		sendButton.setAlignmentX(0.5f);
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Message sendmessage = new Message("msg" , text.getText());
				if(nowtime == 0)
				client.getcontrolmessage().sendMessage(sendmessage);
				text.setText("");
			}
		});

		JLabel connectlabel = new JLabel("connecting..");
		connectlabel.setAlignmentX(0.5f);
		topPanel.add(connectlabel);
		connectlabel.setVisible(false);
		connect_button = new JButton("接続");
		connect_button.setAlignmentX(0.5f);
		connect_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				connectlabel.setVisible(true);
				client.startConnection();
				connectlabel.setVisible(false);
				connect_button.setVisible(false);
			}
		});


		Container contentPane = getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		contentPane.add(topPanel);
		contentPane.add(listPanel);
		contentPane.add(chatScrollpane);
		contentPane.add(textScrollpane);
		contentPane.add(sendButton);
		contentPane.add(connect_button);
	}

}