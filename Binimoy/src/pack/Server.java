package pack;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Server extends Thread
{
	private ServerSocket serverSocket;
	Socket server;

	/*public Server(int port) throws IOException, SQLException, ClassNotFoundException, Exception
	{
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(180000);
	}*/

	public void run()
	{
		while(true)
		{ 
			try
			{
				server = serverSocket.accept();
				BufferedImage img;
				int i=0;
				InputStream in=server.getInputStream();

				while(++i<10000) 
				{
					JFrame frame = new JFrame();
					System.gc();
					img=ImageIO.read(ImageIO.createImageInputStream(server.getInputStream()));
					if(img==null) {
						System.out.println("nll "+i);
						continue;
					}

					frame.getContentPane().add(new JLabel(new ImageIcon(img)));
					frame.setVisible(true); 
					img=null;
					frame.pack();
					System.out.println(i);

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
			catch(SocketTimeoutException st)
			{
				System.out.println("Socket timed out!");
				break;
			}
			catch(IOException e)
			{
				e.printStackTrace();
				break;
			}
			catch(Exception ex)
			{
				System.out.println(ex);
			}
		}
	}

	public Server() 
	{
		//Thread t = new Server(6066);
		//t.start();

		try {
			ServerSocket serverSocket1;
		Socket server;


		serverSocket1 = new ServerSocket(Info.scrnPort);
		serverSocket1.setSoTimeout(180000);
		JLabel jl=new JLabel();
		jl.setIcon(null);
		JFrame frame = new JFrame();
		frame.getContentPane().add(jl);
		frame.setVisible(true); 
		System.out.println("server waiting");
		System.out.println(InetAddress.getLocalHost().getHostAddress());
		Socket socket =serverSocket1.accept();
		System.out.println("server connected");
		int f=0;
		while(true) {


			DataInputStream dIn = new DataInputStream(socket.getInputStream());

			int length = dIn.readInt();   
			byte[] message = null;// read length of incoming message
			if(length>0) {
				message = new byte[length];
				dIn.readFully(message, 0, message.length); // read the message
			}


			InputStream in = new ByteArrayInputStream(message);
			BufferedImage bImageFromConvert = ImageIO.read(in);
			//		frame.getContentPane().add(new JLabel(new ImageIcon(bImageFromConvert)));

			
			jl.setIcon(new ImageIcon(bImageFromConvert));
			jl.revalidate();
			//   jl.repaint();
			//  jl.update(jl.getGraphics());

			frame.pack();
			f=1;



		}

		}
	
	catch(Exception ee){

	}
	finally
	{
		
	}
		
	}
}