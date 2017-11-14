package pack;
import java.awt.FlowLayout;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

//import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class TestChat extends JFrame {
	
	static	byte[] readStream(DataInputStream din) {
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


	static	void sendFile(Socket s) throws IOException {
		
		JFrame f=new JFrame();
		JProgressBar jb;
		DataOutputStream dos=new DataOutputStream(s.getOutputStream());
		DataInputStream in=new DataInputStream(System.in);
		DataInputStream dis=new DataInputStream(s.getInputStream());
		PrintWriter pw=new PrintWriter(s.getOutputStream(),true);
		while(true) {

			try {
				System.out.println("fileChooser");
				JFileChooser fc=new JFileChooser();
				int result=fc.showOpenDialog(new JFrame());
				if(JFileChooser.APPROVE_OPTION==result){
					File file=fc.getSelectedFile();
					int fileSegment=5555;
					long currentPointer=0;
					long fileSize=file.length();
					if(file.canRead()) {

						RandomAccessFile rd=new RandomAccessFile(file, "r");
						dos.write(createPacket("124".getBytes("UTF8"), file.getName().getBytes("UTF8")));
						dos.flush();
						dos.write(createPacket("111".getBytes("UTF8"),String.valueOf(fileSize).getBytes("UTF8")));
						dos.flush();
						boolean loop_break=false;

						
						//JPanel jp= new JPanel();
						jb=new JProgressBar(0,(int) fileSize);    
						jb.setBounds(40,40,160,30);         
						jb.setValue(0);    
						jb.setStringPainted(true);    
						    
						jb.setSize(250,150);
						//f.add(jb);
						
						f.setSize(500, 100);
						jb.setLayout(new FlowLayout());    
						f.setVisible(true);
						long allCome=0;

					//	while(true) {

							byte[] cmd=new byte[3];
							if(dis.read()==2) {

								dis.read(cmd,0,cmd.length);
								//System.out.println(Integer.parseInt(new String (cmd)));
								switch(Integer.parseInt(new String(cmd))) {

								case 125:
									//System.out.println("got pntr "+currentPointer+"  "+fileSize);

									currentPointer=Long.parseLong(new String(readStream(dis)));
									//System.out.println("cnt pntr "+currentPointer+"  "+fileSize);
									if(currentPointer!=fileSize) {
										int buff_len=(int) ((fileSize-currentPointer) >fileSegment ? fileSegment: fileSize-currentPointer);
										byte[] tempbuff=new byte[buff_len];
										rd.seek(currentPointer);
										rd.read(tempbuff,0,buff_len);
										dos.write(createPacket("125".getBytes("UTF8"), tempbuff));
										dos.flush();
										System.out.println("sending  "+(currentPointer*100.0/fileSize)+ " %");

										allCome+=currentPointer;
										//jb.setValue((int) allCome);    
										//try{Thread.sleep(1);}catch(Exception e){}

									}
									else {
										loop_break=true;
										dos.write(createPacket("126".getBytes("UTF8"), "".getBytes()));
										dos.flush();
										
										if(loop_break) {
											System.out.println("send sucess");
											//JFrame f=new JFrame();  
										    JOptionPane.showMessageDialog(f,"File sent Successfully.","Alert",JOptionPane.WARNING_MESSAGE);  
										    f.setVisible(false);
										    return;

										}
										
										
									}
									break;
								}
							}
							if(loop_break) {
								System.out.println("send sucess");
								//JFrame f=new JFrame();  
							    JOptionPane.showMessageDialog(f,"File sent Successfully.","Alert",JOptionPane.WARNING_MESSAGE);  
							    f.setVisible(false);
							    return;

							}
						}

					}

			//}
			}catch(Exception e) {
			}
		}
		//s.close();

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
	public TestChat()  {
		ServerSocket sv=null;
		Socket s=null;
		try {
			// TODO Auto-generated method stub
			sv=new ServerSocket(Info.filePort);

			//while(true) {
			System.out.println("SERVER WAITING .....");


			s=sv.accept();
			System.out.println("SERVER con .....");
			//				MultiClient md=new MultiClient(s);
			//				md.join();

			//TestChat tc=new TestChat();
			sendFile(s);
			
			s.close();
			//	}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			if(sv!=null)
				try {
					if(s!=null) s.close();
					sv.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}


	}



}

class MultiClient extends Thread{
	Socket clientSoc=null;
	DataOutputStream dout=null;
	public MultiClient(Socket soc) {
		clientSoc=soc;
		start();
	}
	@Override
	public void run() {
		try {
			TestChat tc=new TestChat();
			tc.sendFile(clientSoc);
			clientSoc.close();
			//				Scanner in=new Scanner (System.in);
			//				dout=new DataOutputStream(clientSoc.getOutputStream());
			//				ReadFromClint rd=new ReadFromClint(clientSoc.getInputStream());
			//				while(true) {
			//					/// write to client
			//					String myin=in.nextLine();
			//					System.out.println("ME >>  "+myin);
			//					dout.write(myin.getBytes("UTF8"));
			//					dout.writeByte(27);
			//					dout.flush();
			//				}
			//				//				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
//	class ReadFromClint extends Thread{
//
//		InputStream ins=null;
//		public ReadFromClint(InputStream _ins) {
//			// TODO Auto-generated constructor stub
//			ins=_ins;
//			start();
//
//		}
//		@Override
//		public void run() {
//			while(true) {
//				try {
//					int i=0;
//					String reply="";
//					while((i=ins.read())!=27){		
//						reply+=(char)i;
//					}
//					System.out.println("Client  >>  "+reply);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//			}
//		}
//
//	}

