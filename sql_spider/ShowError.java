package sql_spider;

import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

public class ShowError {
	public JTextArea textarea=new JTextArea();
	public JButton button;
	public ShowError(){
		JPanel panel;
        JTextField field=null;
        panel=new JPanel();
        button=new JButton("  start  ");
        
        field=new JTextField("20",10);
        button.addActionListener(new Action_1(field,this));
        
		panel.add(new Label("Ïß³ÌÊý:"));
		panel.add(field);
		panel.add(button);
		
		JFrame frame=new JFrame();
		textarea.setEditable(false);
		frame.add(panel,"North");
		frame.add(new JScrollPane(textarea),"Center");
		frame.setSize(800,600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		frame.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					System.exit(0);
				}
			});
	}
	public synchronized void addE(String str){
		textarea.append(str+"\t\n");
	}
	public void setBDisvisible(){
		
	}
	public static void main(String[] args) {
		new ShowError();
	}
}
class Action_1 implements ActionListener {
	JTextField jf;
	ShowError se;
	Action_1(JTextField jf,ShowError se){
		this.jf=jf;
		this.se=se;
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().trim().equals("start")){
			Main m=new Main(Integer.parseInt(jf.getText().trim()));
			m.init(se, "http://www.hsu.edu.cn/");
			m.handle();
		}
	}
}
