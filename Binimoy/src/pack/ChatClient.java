package pack;
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

public class ChatClient {
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

	public ChatClient() {
		// TODO Auto-generated method stub

		try {
			//Runtime.getRuntime().exec(new String[] {"cmd.exe","/c","start"});
			Socket soc = new Socket(Info.otherIp, Info.chatPort);

			// D din=new DataInputStream(soc.getInputStream());
			DataOutputStream dout = new DataOutputStream(soc.getOutputStream());
			DataInputStream din = new DataInputStream(soc.getInputStream());
			Scanner in = new Scanner(System.in);
			System.out.println("connected");
			ReadFromServer rd = new ReadFromServer(soc.getInputStream(),"he");
			while (true) {
				/// write to server
				String myin=in.nextLine();
				System.out.println("ME >>  "+myin);
				dout.write(myin.getBytes("UTF8"));
				dout.writeByte(27);
				dout.flush();
				
			}

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}




	
}

