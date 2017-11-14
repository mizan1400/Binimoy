package pack;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class Home extends JFrame implements ActionListener {

	JButton sendFile;
	JButton recieve;
	JButton shareScreen;
	JButton viewScreen;
	JButton user1;
	JButton user2;
	JButton message1,message2;
	JLabel title;
	
	JFrame jf= new JFrame("Binimoy");

	private Component parent;
	Home()
	{
		//super("Binimoy");


		title = new JLabel("Binimoy");
		title.setFont(new Font("Courier New", Font.ITALIC, 30));
		title.setForeground(Color.BLUE);

		sendFile = new JButton("Send File");
		recieve = new JButton("Recieve File");
		shareScreen = new JButton("Share Screen");
		viewScreen = new JButton("View Screen");
		message1 = new JButton("1st Massenger");
		message2 = new JButton("2nd Massenger");
		user1 = new JButton("1st User");
		user2 = new JButton("2nd User");

		title.setBounds(130,30,200,30);
		sendFile.setBounds(130,100,120,30);
		recieve.setBounds(130,150,120,30);
		shareScreen.setBounds(130,200,120,30);
		viewScreen.setBounds(130,250,120,30);
		message1.setBounds(130,300,120,30);
		message2.setBounds(130,350,120,30);

		sendFile.addActionListener(this);
		recieve.addActionListener(this);
		shareScreen.addActionListener(this);
		viewScreen.addActionListener(this);
		message1.addActionListener(this);
		message2.addActionListener(this);

		add(title);
		add(sendFile);
		add(recieve);
		add(shareScreen);
		add(viewScreen);
		add(message1);
		add(message2);

		setLayout(null);  
		setVisible(true);
		setSize(400, 600);

	}

	public void actionPerformed(ActionEvent ae) 
	{ 

		if(ae.getSource()==sendFile)
		{    
			System.out.println("sent");
			Thread t=new Thread() {
				public void run(){
					new TestChat();
				}
			};
			t.start();



		}

		else if(ae.getSource()==recieve)
		{    
			System.out.println("RecieveFile");
			Thread tr=new Thread() {
				public void run(){
					new ClientSite();
				}
			};
			tr.start();

		}

		else if(ae.getSource()==shareScreen)
		{    
			System.out.println("shareScreenClient");
			Thread tc=new Thread() {
				public void run(){
					new Client();
				}
			};
			tc.start();


		}

		else if(ae.getSource()==viewScreen)
		{    
			System.out.println("viewScreen");
			Thread tv=new Thread() {
				public void run(){
					new Server();
				}
			};
			tv.start();



		}

		else if(ae.getSource()==message1)
		{    

			System.out.println("message1");
			Thread tmsg=new Thread() {
				public void run(){
					new ChatServer();
				}
			};
			tmsg.start();
		}

		else if(ae.getSource()==message2)
		{    

			System.out.println("message2");
			Thread tmsg2=new Thread() {
				public void run(){
					new ChatClient();
				}
			};
			tmsg2.start();
		}

	}

}
