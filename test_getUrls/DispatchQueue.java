package test_getUrls;

import java.util.*;

public class DispatchQueue {
	private LinkedList<String> waitLinks;       //待操作队列 0
	private LinkedList<String> alreadyLinks;	//已操作队列 1	
	private LinkedList<String> errorLinks;		//错误队列   2
	private LinkedList<String> externalLinks;	//外部链接队列 3
	private Hashtable<Integer,String> nowLinks;
	public DispatchQueue(){
		waitLinks=new LinkedList<String>();
		alreadyLinks=new LinkedList<String>();
		errorLinks=new LinkedList<String>();
		externalLinks=new LinkedList<String>();
		nowLinks=new Hashtable<Integer,String>();
	}
	public synchronized boolean isExist(String url){
		if(containsA(url) || containsER(url) || containsEX(url) || contaionsW(url) || nowLinks.contains(url)){
			return true;
		}
		return false;
	}
	public synchronized void addW(String url){
		if(!isExist(url)){
			waitLinks.add(url);
		}
	}
	public synchronized String getW(){
		if(waitLinks.isEmpty()){
			try {
				wait();
				return null;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			notifyAll();
		}
		return waitLinks.remove();
	}
	public synchronized boolean contaionsW(String url){
		for(String str:waitLinks){
			if(str.replaceAll("//", "/").equals(url.replaceAll("//", "/"))){
				return true;
			}
		}
		return false;
	}
	public synchronized boolean isEmptyW(){
		if(waitLinks.size()==0){
			return true;
		}
		return false;
	}
	public synchronized void addA(String url){
		alreadyLinks.add(url);
	}
	public synchronized boolean containsA(String url){
		for(String str:alreadyLinks){
			if(str.replaceAll("//", "/").equals(url.replaceAll("//", "/"))){
				return true;
			}
		}
		return false;
	}
	public synchronized void addER(String url){
		errorLinks.add(url);
	}
	public synchronized boolean containsER(String url){
		for(String str:errorLinks){
			if(str.replaceAll("//", "/").equals(url.replaceAll("//", "/"))){
				return true;
			}
		}
		return false;
	}
	public synchronized void addEX(String url){
		externalLinks.add(url);
	}
	public synchronized String getEX(){
		return externalLinks.remove();
	}
	public synchronized boolean containsEX(String url){
		for(String str:externalLinks){
			if(str.replaceAll("//", "/").equals(url.replaceAll("//", "/"))){
				return true;
			}
		}
		return false;
	}
	public synchronized boolean isEmptyEX(){
		return externalLinks.isEmpty();
	}
	public void addN(int key,String value){
		nowLinks.put(key, value);
	}
	public void removeN(int key){
		nowLinks.remove(key);
	}
	public static void main(String[] args) {

	}

}
