package pack;
import java.awt.FlowLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
//import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

public class ClientSite {
	static byte[] readStream(DataInputStream din) {
		byte[] data_buff=null;
		try {
			String buff_len="";
			int b=0;
			while((b=din.read())!=4) {
				buff_len+=(char)b;
			}
			int len=Integer.parseInt(buff_len);
			data_buff=new byte[len];
			int buffPos=0;
			while(buffPos<len) {
				int canReadSize=din.read(data_buff,buffPos,len-buffPos);
				buffPos+=canReadSize;
			}
		}catch(Exception e) {

		}
		return data_buff;
	}

	static byte[] createPacket(byte[]cmd,byte[]data) {
		byte[] pack=null;

		try {
			byte[] separator=new byte[1];
			byte[] initializ=new byte[1];
			byte[] data_length = String.valueOf(data.length).getBytes("UTF8");
			initializ[0]=2;
			separator[0]=4;
			pack=new byte[initializ.length+cmd.length+data_length.length+separator.length+data.length];
			System.arraycopy(initializ, 0, pack, 0, initializ.length);
			System.arraycopy(cmd, 0, pack, initializ.length, cmd.length);
			System.arraycopy(data_length, 0, pack, initializ.length+cmd.length, data_length.length);
			System.arraycopy(separator,0,pack,initializ.length+cmd.length+data_length.length,separator.length);
			System.arraycopy(data,0,pack,initializ.length+cmd.length+data_length.length+separator.length,data.length);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pack;

	}

	public ClientSite() {
		// TODO Auto-generated method stub

		try {
			Socket soc = new Socket(Info.otherIp, Info.filePort);
			// D din=new DataInputStream(soc.getInputStream());
			DataOutputStream dout = new DataOutputStream(soc.getOutputStream());
			DataInputStream din = new DataInputStream(soc.getInputStream());
			Scanner in = new Scanner(System.in);
			System.out.println("connected");
			//ReadFromServer rd = new ReadFromServer(soc.getInputStream());
			
				/*/// write to server
				String myin=in.nextLine();
				System.out.println("ME >>  "+myin);
				dout.write(myin.getBytes("UTF8"));
				dout.writeByte(27);
				dout.flush();*/
				//ClientSite cs=new ClientSite();
				getFile(din, dout);
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}




	static void getFile(DataInputStream dis,DataOutputStream dos) {
		
		RandomAccessFile rs=null;
		JFrame f=new JFrame();
		JProgressBar jb;

		try {
			
			
			long currentPointer=0;
			boolean loop_break=false;
			long fileSize=0;
			
			jb=new JProgressBar(0,(int) fileSize);    
			jb.setBounds(40,40,160,30);         
			jb.setValue(0);    
			jb.setStringPainted(true);    
			    
			jb.setSize(250,150);
			f.add(jb);
			
			f.setSize(500, 100);
			jb.setLayout(new FlowLayout());    
			f.setVisible(true);
			
			while(true) {
				if(dis.read()==2) {
					byte[] cmd=new byte[3];
					dis.read(cmd,0,cmd.length);
					//System.out.println(Integer.parseInt(new String (cmd)));
					switch(Integer.parseInt(new String (cmd))) {

					case 124:
						String flName= new String(readStream(dis));
						
						//System.out.println(flName);
						//String path="F:\\sharesoft\\"+flName;
						///get file size
						if(dis.read()==2) {
							byte [] dcmd=new byte[3];
							dis.read(dcmd,0,dcmd.length);
							if(Integer.parseInt(new String (dcmd))==111) 	fileSize=Long.parseLong(new String(readStream(dis)));
							else 	System.out.println("can't detect size");
						}
						else	System.out.println("can't detect size");
						
						rs=new RandomAccessFile(new File(flName), "rw");
						//System.out.println("file created");
						dos.write(createPacket("125".getBytes("UTF8"), String.valueOf(currentPointer).getBytes("UTF8")));
						dos.flush();
						System.out.println("receving...");
						break;

					case 125:
						rs.seek(currentPointer);
						byte[] temp_buff=readStream(dis);
						rs.write(temp_buff,0 , temp_buff.length);
						currentPointer=rs.getFilePointer();
						
						dos.write(createPacket("125".getBytes("UTF8"),String.valueOf(currentPointer).getBytes("UTF8")));
						dos.flush();
						//System.out.println("recveing  "+(currentPointer*100.0/fileSize)+ " %");
						
						jb.setValue((int) currentPointer);
						break;

					case 126: 
						System.out.println("successfull");
						//JFrame f=new JFrame();  
					    JOptionPane.showMessageDialog(f,"File recieved successfully.","Alert",JOptionPane.WARNING_MESSAGE);
						loop_break=true;
						return;




					}
					if(loop_break) break;
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}
/*
class ReadFromServer extends Thread {
	InputStream in = null;

	public ReadFromServer(InputStream ins) {
		in = ins;
		start();
	}

	@Override
	public void run() {
		while (true) {
			try {
				int i=0;
				String reply="";
				while((i=in.read())!=27){

					reply+=(char)i;
				}
				System.out.println("SERVER  >>  "+reply);
			} catch (IOException e) {
				
				e.printStackTrace();
			}

		}
	}

}
*/