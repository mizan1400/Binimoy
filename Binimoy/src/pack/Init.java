package pack;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JFrame;

public class Init {

	public static void main(String[] args) throws UnknownHostException 
	{
		//JFrame jf= new JFrame("Binimoy");
		String iip=InetAddress.getLocalHost().getHostAddress();
		System.out.println("Your IP "+iip+"\nEnter your partner IP");
		iip=new Scanner(System.in).next();
		Info.otherIp=iip;
		new Home();

	}

}
