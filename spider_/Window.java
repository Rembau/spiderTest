package spider_;
//import java.net.*;
//import java.io.*;
import java.awt.*;
import javax.swing.*;

//import d.spider.MainFrame;
//import d.spider.SearchThread;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window extends JFrame implements ActionListener  {

	private static final long serialVersionUID = 1L;
			static int count=0;
	        static int tcount=0;
	        static long i;
			Panel panel;
	        TextField field;
	        static TextArea textarea;
	        Button button;
	        Label label;
		    Window(){ 
			super("Spider");
			i=System.currentTimeMillis();
			panel=new Panel();
			button=new Button("  ËÑË÷  ");
			textarea=new TextArea();
			textarea.setEditable(false);
		    //textarea.setLineWrap(true);
			field=new TextField("20",10);
			panel.add(new Label("Ïß³ÌÊý:"));
			//panel.setForeground(Color.BLUE);
			panel.add(field);
			panel.add(button);
			add(panel,"North");
			add(new JScrollPane(textarea),"Center");
			button.addActionListener(this);
			setSize(800,600);
			this.setLocationRelativeTo(null);
			setVisible(true);
		    }
				
		    public void actionPerformed(ActionEvent e) {
			 int a=Integer.valueOf(field.getText());
				 for(int i=1;i<=a;i++)
				 {
		             new IpThread(i,a).start();
		           }
				}
		    public synchronized static void setText(String s,int mark){
		    	       if(mark==1)count++;
		    	       else if(mark==2)tcount++;
						textarea.append(" ÐòºÅ"+count+":"+s+"       "+tcount+""+(System.currentTimeMillis()-i)+"\n");	 
		    }
			public static void main(String args[]){
			    new Window();
			}


}
