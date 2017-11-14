package pack;
import java.awt.HeadlessException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

//import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class ChatServer {
	byte[] readStream(DataInputStream din) {
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


	byte[] createPacket(byte[]cmd,byte[]data) {
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
	public ChatServer() {
		// TODO Auto-generated method stub
		try {
			//Runtime.getRuntime().exec(new String[] {"cmd.exe","/c","start"});
			ServerSocket sv=new ServerSocket(Info.chatPort);
			System.out.println("SERVER WAITING .....");
			Socket s=sv.accept();

			Scanner in=new Scanner (System.in);
			DataOutputStream dout=new DataOutputStream(s.getOutputStream());

			System.out.println("SERVER con .....");



			ReadFromServer rd=new ReadFromServer(s.getInputStream(),"he");
			while(true) {
				/// write to client
				String myin=in.nextLine();
				System.out.println("ME >>  "+myin);
				dout.write(myin.getBytes("UTF8"));
				dout.writeByte(27);
				dout.flush();
			}
			//				MultiClient md=new MultiClient(s);
			//				md.join();
		}catch(Exception ee)
		{
				System.out.println(ee);
		}
	}

}



/*class MultiClient extends Thread{
	Socket clientSoc=null;
	DataOutputStream dout=null;
	public MultiClient(Socket soc) {
		clientSoc=soc;
		start();
	}
	@Override
	public void run() {
		try {
			ChatServer tc=new ChatServer();

			Scanner in=new Scanner (System.in);
			dout=new DataOutputStream(clientSoc.getOutputStream());
			ReadFromClint rd=new ReadFromClint(clientSoc.getInputStream());
			while(true) {
				/// write to client
				String myin=in.nextLine();
				System.out.println("ME >>  "+myin);
				dout.write(myin.getBytes("UTF8"));
				dout.writeByte(27);
				dout.flush();
			}
			//				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
class ReadFromClint extends Thread{

	InputStream ins=null;
	public ReadFromClint(InputStream _ins) {
		// TODO Auto-generated constructor stub
		ins=_ins;
		start();

	}
	@Override
	public void run() {
		while(true) {
			try {
				int i=0;
				String reply="";
				while((i=ins.read())!=27){		
					reply+=(char)i;
				}
				System.out.println("Client  >>  "+reply);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}*/
