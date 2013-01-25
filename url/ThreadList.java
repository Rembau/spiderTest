package url;

import java.util.LinkedList;

import datasql.ConnsForThread;

public class ThreadList {

	private int url_thread_num=5;
	private ConnsForThread conns=null;
	private LinkedList<UrlThread> list=new LinkedList<UrlThread>();
	/**
	 * @param num ��ʼ��url�߳���Ŀ
	 */
	public ThreadList(int num,ConnsForThread conns){
		this.conns=conns;
		if(num>0)this.url_thread_num=num;
		for(int i=0;i<url_thread_num;i++){
			UrlThread thread=new UrlThread(this.conns,this);
			thread.start();
			list.add(thread);
		}
	}
	public synchronized UrlThread GetUrlThread(String info){
		while(list.isEmpty()){
			try{
				this.wait();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		UrlThread thread = list.removeFirst();
		System.out.println("������"+list.size()+"����ȡ�������̲߳�������ţ�"+thread.Getid()+","+info);
		return thread;
		
	}
	public synchronized void put(UrlThread thread, String info){
		System.out.println("������"+list.size()+"�����ء������̲߳�������ţ�"+thread.Getid()+","+info);
		list.add(thread);
		this.notify();
	}
}
