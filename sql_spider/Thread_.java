package sql_spider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import sql_spider.conn.DBoperate;

/**
 * Description:��ȡҳ����߳�
 * @author Administrator
 *
 */
public class Thread_ extends Thread{
	GetContent_URL getC=new GetContent_URL();  //url��ʽ��ȡҳ��
	GetContent_http getC_; //socket��ʽ
	DBoperate dbOperate=new DBoperate();         //���ݿ����
	GetLinks_ getL=new GetLinks_();				 //��ȡҳ������������
	DispatchQueue dq;
	DispatchHost dh;
	ShowError se;
	String url;
	String host;
	static volatile int urlNumCount=0;
	int mark;
	public Thread_(){
	}
	public void init(int mark,DispatchQueue dq,DispatchHost dh){
		this.mark=mark;
		this.dq=dq;
		this.dh=dh;
		getL.setD(dq);
		dq.setConn(dbOperate);
	}
	public void init(int mark,DispatchQueue dq,DispatchHost dh,ShowError se){
		this.mark=mark;
		this.dq=dq;
		this.dh=dh;
		this.se=se;
		getL.setD(dq);
		dq.setConn(dbOperate);
	}
	public void setHost(String host){
		try {
			URL url=new URL(host);
			this.host=url.getHost();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getC.setHost(this.host);
		if(GetContent_http.setHost(this.host)){
			System.out.println("���ӳɹ���");
		}
	}
	public void run(){
			go();
	}
	public void go(){
		while(true){
			if((url=dq.getW())==null){    //�Ƿ�ɹ���ȡһ����õ�����
				System.out.println("null"+url);
				continue;
			}
			if(judgeExternal(url)){			//�Ƿ����ⲿ����
				dq.addO(url,3);				
				continue;
			}
			getC_=new GetContent_http();
			String content=getC_.getContent(url);	//
			while(content.trim().startsWith("http://")){	//�ж�ҳ���Ƿ���ת
				if(se!=null){
					se.addE(url+" "+content);		//�ڴ�����ʾ������ʾ
				}
				if(dq.addO(content,1)){
					break;
				}
				content = getC_.getContent(content);//���ݷ��ص��µ�url��ȡ����
			}
			if(content.trim().startsWith("http:")){
				continue;
			}
			if(content.startsWith("error")){		//��ȡҳ���Ƿ���󷵻ش������
				System.out.println(url+" "+content+"===============================");
				dq.addO(url,2);
				if(se!=null){
					se.addE(url+" "+content);
				}
				continue;
			}
			getL.getLink(url,content);				//��ȡҳ�����ӣ������Ӷ��й����ཻ��
			dq.addO(url,1);
			System.out.println(url+" ===��ȡ�ɹ�===��");
			dbOperate.setPStmt(content, url); //���ݿ�
		    urlNumCount++;
			System.out.println(urlNumCount+" "+new Date());
			getC_=null;
		}
	}
	/**
	 * Description:�ж��Ƿ����ⲿ����
	 * @param url �жϵ�����
	 * @return  boolean
	 */
	public boolean judgeExternal(String url){
		try {
			String host_=new URL(url).getHost();
			URL u_1=new URL("http://"+host_);
			URL u_2=new URL("http://"+host);
			if(u_1.sameFile(u_2)){
				return false;
			}
			System.out.println(url+"���ⲿ���ӣ�======================"+host_);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	public static void main(String[] args) {

	}

}
