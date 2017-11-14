package pack;

import java.io.IOException;
import java.io.InputStream;

class ReadFromServer extends Thread {
	InputStream in = null;
	String name=null;
	public ReadFromServer(InputStream ins,String name) {
		in = ins;
		this.name=name;
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
				System.out.println(name+"  >>  "+reply);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
