package pack;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Client
{
	Image newimg;
	static BufferedImage bimg;
	byte[] bytes;

	public Client() 
	{
		
		try {
			Socket client = new Socket(Info.otherIp, Info.scrnPort);
			
			while(true) {
				
			send_data(client);
			Thread.sleep(60);
			}
			
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		/*
		
		String serverName = "localhost";
		int port = 6066;
		try
		{
			Socket client = new Socket(serverName, port);
			Robot bot;
			bot = new Robot();
			
			int i=0;
			while(i != 0) // ++i<10000
			{
				
				bimg = bot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
				
				if(bimg==null) System.out.println("lal"); ///debug
				ImageIO.write(bimg,"JPG",client.getOutputStream());
				System.out.println(i);///debug
				
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			client.close();
		} catch(IOException | AWTException e) {
			e.printStackTrace();
		} */
	}
	
	
	
	static void send_data(Socket client ) throws UnknownHostException, IOException, AWTException {
		
		Robot bot = new Robot();
		BufferedImage bimg = bot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
		
		ByteArrayOutputStream compressed = new ByteArrayOutputStream();
		ImageOutputStream outputStream = ImageIO.createImageOutputStream(compressed);

		// NOTE: The rest of the code is just a cleaned up version of your code

		// Obtain writer for JPEG format
		ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();

		// Configure JPEG compression: 70% quality
		ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
		jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		jpgWriteParam.setCompressionQuality(0.7f);

		// Set your in-memory stream as the output
		jpgWriter.setOutput(outputStream);

		// Write image as JPEG w/configured settings to the in-memory stream
		// (the IIOImage is just an aggregator object, allowing you to associate
		// thumbnails and metadata to the image, it "does" nothing)
		jpgWriter.write(null, new IIOImage(bimg, null, null), jpgWriteParam);

		// Dispose the writer to free resources
		jpgWriter.dispose();

		// Get data for further processing...
		byte[] jpegData = compressed.toByteArray();
		
		/*InputStream in = new ByteArrayInputStream(jpegData);
		BufferedImage bImageFromConvert = ImageIO.read(in);
		
		JFrame frame = new JFrame();
		frame.getContentPane().add(new JLabel(new ImageIcon(bImageFromConvert)));
		frame.setVisible(true); 
		
		frame.pack(); */
		
		
		DataOutputStream dOut = new DataOutputStream(client.getOutputStream());

		dOut.writeInt(jpegData.length); // write length of the message
		dOut.write(jpegData);  
		
	}
	
	
}