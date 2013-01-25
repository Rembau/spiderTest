package datasql;

import java.util.LinkedList;

public class ConnsForThread {

	private int con_num=5;
	private LinkedList<Conn> list=new LinkedList<Conn>();
	public ConnsForThread(int num){
		if(num>0)this.con_num=num;
		for(int i=0;i<con_num;i++)list.add(new Conn());
	}
	public synchronized Conn GetConn(String info){
		while(list.isEmpty()){
			try{
				this.wait();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		System.out.println("���ݿ�������"+list.size()+"����ȡ���������ݿ����,"+info);
		return list.removeFirst();
	}
	public synchronized void PutConn(Conn c,String info){
		list.add(c);
		notify();
		System.out.println("���ݿ�������"+list.size()+"�����ء��������ݿ����,"+info);
	}
}
