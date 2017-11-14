package pack;

import java.io.File;
import javax.swing.JFileChooser;

public class Send {
	Send()
	{
		JFileChooser fc=new JFileChooser();    
		
		int i=fc.showOpenDialog(null);    
		if(i==JFileChooser.APPROVE_OPTION)
		{    
			File f=fc.getSelectedFile();    
			String filepath=f.getPath();
			System.out.println(filepath);
		}
	}

}
