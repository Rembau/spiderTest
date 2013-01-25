package test_getUrls;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;

public class DispatchHost {
	private ArrayList<String> hostLink;  //IPµÿ÷∑
	public DispatchHost(){}
	public synchronized boolean add(String host){
		try {
			URL url=new URL(host);
			try {
				host=InetAddress.getByName(url.getHost()).getHostAddress();
			} catch (UnknownHostException e) {
				e.printStackTrace();
				return false;
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(contains(host)){
			return false;
		}
		hostLink.add(host);
		return true;
	}
	public boolean contains(String host){
		return hostLink.contains(host);
	}
	public void remove(String h){
		hostLink.remove(h);
	}
}
