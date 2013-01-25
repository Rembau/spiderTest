package url;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class UrlPanel extends  JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JLabel jl_url=new JLabel("起始网址：");
	private JTextField jt_url=new JTextField(20);
	private JLabel jl_sqlnum=new JLabel("数据库连接数：");
	private JTextField jt_sqlnum=new JTextField(5);
	private JLabel jl_threadnum=new JLabel("线程数：");
	private JTextField jt_threadnum=new JTextField(5);
	private JButton jb_url=new JButton("搜索");
	private JButton jb_rest=new JButton("暂停");
	private JPanel toppanel=new JPanel();
	private JTextArea jtf=null;
	private JLabel main_jl=new JLabel("搜索");
	private MangerThread th=null;
	public UrlPanel(){
		init();
	}
	private void init(){
		this.setLayout(new BorderLayout());
		toppanel.add(jl_url);
		toppanel.add(jt_url);
		jt_url.setText("http://www.hsu.edu.cn/");
		toppanel.add(jl_sqlnum);
		toppanel.add(jt_sqlnum);
		jt_sqlnum.setText("5");
		toppanel.add(jl_threadnum);
		toppanel.add(jt_threadnum);
		jt_threadnum.setText("5");
		toppanel.add(jb_url);
		toppanel.add(jb_rest);
		jb_url.addActionListener(this);
		jb_rest.addActionListener(this);
		jtf=new JTextArea();
		JScrollPane jsp=new JScrollPane(jtf);
		add(toppanel, BorderLayout.NORTH);
		add(jsp,BorderLayout.CENTER);
		add(main_jl,BorderLayout.SOUTH);
	}
	public void actionPerformed(ActionEvent e) {
		// TODO 自动生成方法存根
		if(e.getSource()==jb_url){
			int sqlnum;
			int threadnum;
			try {
				sqlnum = Integer.parseInt(jt_sqlnum.getText().trim());
				threadnum = Integer.parseInt(jt_threadnum.getText().trim());
			} catch (NumberFormatException e1) {
				// TODO 自动生成 catch 块
				e1.printStackTrace();
				return;
			}
			if(th==null){
				System.out.println("aaasdas");
				th=new MangerThread(main_jl, sqlnum,threadnum);
				th.SetFirstUrl(jt_url.getText().trim(), jtf);
				th.start();
			}
		}else if(e.getSource()==jb_rest){
			if(th!=null&&!th.isRest()){
				jb_rest.setText("继续");
				th.rest(true);
			}else if(th!=null&&th.isRest()){
				jb_rest.setText("暂停");
				th.rest(false);
			}
		}
	}
}
