package search;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SelectPort {
	private InetAddress ip = null;
	private int port = 0;
	public void setIP(InetAddress ip_info) {
		this.ip = ip_info;
	}
	public void setPort(int port_info) {

		this.port = port_info;
	}
	public InetAddress getIP() {
		return this.ip;
	}

	public int getPort() {
		return this.port;
	}

	public boolean scan() {
		Socket scanSocket = new Socket();
		InetSocketAddress socketaddress = new InetSocketAddress(ip, port);

		try {
			scanSocket.connect(socketaddress, 100);
			// socketaddress=scanSocket.getRemoteSocketAddress();
			scanSocket.setTcpNoDelay(true);// 设置TCP_NODELAY为true，确保包尽快发送
			return scanSocket.isConnected();
		} catch (Exception e) {
			// e.printStackTrace();
			return false;
		}

		finally {
			/*
			 * try { scanSocket.setSoLinger(true, 0); } catch (SocketException
			 * e1) { e1.printStackTrace(); }
			 */
			try {
				scanSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String args[]) throws Exception {
		long ftomtime = System.currentTimeMillis();
		SelectPort sp = new SelectPort();
		for (int i = 0; i <= 255; i++) {
			String ip = "192.168.21." + i;

			sp.setIP(InetAddress.getByName(ip));
			sp.setPort(80);
			if (sp.scan()) {
				System.out.println(ip + "is open ");
			}
			if (i == 255) {
				long sd = System.currentTimeMillis() - ftomtime;
				System.out.println("扫描耗时（ms）=" + sd);
			}

		}

	}

}
