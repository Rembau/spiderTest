package test_getUrls;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import conn.DBoperate;

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
	}
	public void init(int mark,DispatchQueue dq,DispatchHost dh,ShowError se){
		this.mark=mark;
		this.dq=dq;
		this.dh=dh;
		this.se=se;
		getL.setD(dq);
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
				continue;
			}
			dq.addN(mark, url);				//��ǰ���� �ӵ���ǰ���Ӷ�����
			if(judgeExternal(url)){			//�Ƿ����ⲿ����
				dq.addEX(url);				
				continue;
			}
			getC_=new GetContent_http();
			String content=getC_.getContent(url);	//
			while(content.trim().startsWith("http://")){	//�ж�ҳ���Ƿ���ת
				if(se!=null){
					se.addE(url+" "+content);		//�ڴ�����ʾ������ʾ
				}
				if(dq.isExist(content)){
					break;
				}
				content = getC_.getContent(content);//���ݷ��ص��µ�url��ȡ����
			}
			if(content.trim().startsWith("http:")){
				continue;
			}
			if(content.startsWith("error")){		//��ȡҳ���Ƿ���󷵻ش������
				System.out.println(url+" "+content+"===============================");
				dq.addER(url);
				if(se!=null){
					se.addE(url+" "+content);
				}
				continue;
			}
			getL.getLink(url,content);				//��ȡҳ�����ӣ������Ӷ��й����ཻ��
			dq.addA(url);
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
