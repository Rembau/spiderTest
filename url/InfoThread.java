package url;

public class InfoThread implements Runnable{

	private Thread th=null;
	public InfoThread(){
		
	}
	public void run() {
		// TODO �Զ����ɷ������
		
	}
	public void start(){
		if(th==null){
			th=new Thread(this);
			th.start();
		}
	}
	public void stop(){
		th=null;
	}
}
