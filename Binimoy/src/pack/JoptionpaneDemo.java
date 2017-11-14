package pack;


import javax.swing.*;  
public class JoptionpaneDemo {  
JFrame f;  
JoptionpaneDemo(){  
    f=new JFrame();  
    JOptionPane.showMessageDialog(f,"File sent successfully.","Send",JOptionPane.WARNING_MESSAGE);     
}  
public static void main(String[] args) {  
    new JoptionpaneDemo();  
}  
}  