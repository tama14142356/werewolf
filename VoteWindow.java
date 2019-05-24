
// 2019/5/24 9:24　高橋　那弥

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.*;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class VoteWindow extends JFrame{
	private static VoteWindow frame;
	private final static String title = "投票";
	ButtonGroup group = new ButtonGroup();
	ArrayList<JRadioButton> radio = new ArrayList<JRadioButton>();
	JButton sendButton;
	JLabel timeLabel;
	JLabel messageLabel;
	JPanel radioPanel;
	boolean isOpened = false;
	Client_Main client;
	List<String> canvoteList = new ArrayList<String>();

	public void Showvote() {
		frame = getInstance();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static VoteWindow getInstance() {
		if (frame == null)
			frame = new VoteWindow();
		return frame;
	}

	public void setCanvote(String canvotelist) {
		Scanner sc = new Scanner(canvotelist);
		canvoteList.clear();
		while (sc.hasNext()) {
			String user = sc.next();
			canvoteList.add(user);
		}
		
		sc.close();

		group.clearSelection();
		
		for(JRadioButton r : radio) {
			group.remove(r);
		}
		
		
		radio.clear();
		for (String user : canvoteList) {
			radio.add(new JRadioButton(user));
		}
		
		radioPanel.removeAll();
		if(radio.size() == 0)group.add(new JRadioButton("いません"));
		for (JRadioButton r : radio) {
			group.add(r);
			r.setAlignmentX(0.0f);
			radioPanel.add(r);
		}
	}

	public void setvotelabel(String title) {
		messageLabel.setText(title);
	}

	public void exitWindow() {
		frame.dispose();
		radioPanel.removeAll();
		frame.setVisible(false);
	}

	private VoteWindow() {
		setTitle(title);
		client = Client_Main.getInstance(title);
		setBounds(100, 100, 250, 220);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowListener() {
			public void windowClosing(WindowEvent event) {
				JLabel labelmsg = new JLabel("投票してください");
				JOptionPane.showMessageDialog(frame, labelmsg);
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

		JPanel messagePanel = new JPanel();
		messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
		messageLabel = new JLabel("");
		messageLabel.setAlignmentX(0.5f);
		messagePanel.add(messageLabel);

		radioPanel = new JPanel();
		radioPanel.setMaximumSize(new Dimension(200, 130));
		radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));
		radioPanel.setAlignmentX(0.5f);
		group = new ButtonGroup();
		radio = new ArrayList<JRadioButton>();
		int number = canvoteList.size();
		for (int i = 0; i < number; i++) {
			radio.add(new JRadioButton(canvoteList.get(i)));
		}
		for (JRadioButton r : radio) {
			group.add(r);
			r.setAlignmentX(0.0f);
			radioPanel.add(r);
		}

		sendButton = new JButton("投票");
		sendButton.setAlignmentX(0.5f);
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String optionmsg = null;
				System.out.println("start");
				String selectuser = "";
				for (JRadioButton r : radio) {
					System.out.println(r.getText());
					if (r.isSelected()) {
						selectuser = r.getText();
						optionmsg = selectuser + "に投票しますか？";
						break;
					}
				}
				if (optionmsg == null) {
					JLabel labelmsg = new JLabel("選択されてません");
					JOptionPane.showMessageDialog(frame, labelmsg);
				} else {
					JLabel labelmsg = new JLabel(optionmsg);
					Message vote = null;
					if (client.chatwindow.nowtime == 1)
						vote = new Message("vot", selectuser);
					else if (client.chatwindow.nowtime == 2)
						vote = new Message("nvt", selectuser);
					int option = JOptionPane.showConfirmDialog(frame, labelmsg, "投票", JOptionPane.YES_NO_OPTION,
							JOptionPane.INFORMATION_MESSAGE);
					if (option == JOptionPane.YES_OPTION) {
						client.getcontrolmessage().sendMessage(vote);
						frame.dispose();
					}
				}
			}
		});

		Container contentPane = getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		contentPane.add(messagePanel);
		contentPane.add(radioPanel);
		contentPane.add(sendButton);
	}
}
