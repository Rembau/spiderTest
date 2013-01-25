package url;

import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public class TestFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	private CardLayout cardlayout=new CardLayout();
	public TestFrame(){
		init();
	}
	public void init(){
		setLayout(cardlayout);
		add(new UrlPanel(),"url");
		setSize(800, 600);
		setVisible(true);
		this.setLocationRelativeTo(null);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
	}
	@SuppressWarnings("unused")
	public static void main(String[] args){
		TestFrame frame=new TestFrame();
	}
}
