package search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 基本能实现网页抓取，不过要手动输入URL 将整个html内容保存到指定文件
 * 
 */
/**
 * @author Administrator
 *
 */
public class SelectedByUrl {
	public static int URL_TIME_LIMIT=20;//url连接超时限制，大于20*16ms没有数据时认为获取失败
	@SuppressWarnings("finally")
	public static String getWebBody(String URL){
	        String s = "";
	        try {
	            //从url打开stream
	            InputStream in = null;
	            HttpURLConnection conn = (HttpURLConnection) new URL(URL).openConnection();
	            in = conn.getInputStream();
	            //尝试从http头中获取字符集
	            String contentType = conn.getHeaderField("Content-Type").toLowerCase();
	            String encoding = "utf-8";
	            boolean charsetFound = false;
	            if (contentType.contains("charset")) {
	                encoding = contentType.split("charset=")[1];
	                charsetFound = true;
	            }
	            //如果没有的话,读取头1024个字符，检查html的header
	            byte[] buf = new byte[1024];
	            if (!charsetFound) {
	            	int con=0;
					while(in.available()==0){
						 try {
								Thread.sleep(16);
							} catch (InterruptedException e) {
								// TODO 自动生成 catch 块
								e.printStackTrace();
							}
							if(con++>SelectedByUrl.URL_TIME_LIMIT)return null;
					}
	                int len = in.read(buf);
	                while (len <= 32) {
	                    len = in.read(buf);
	                }
	                String header = new String(buf, 0, len);
	                Pattern p = Pattern.compile(".*<meta.*content=.*charset=.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	                Matcher m = p.matcher(header);
	                if (m.matches()) {
	                    encoding = header.toLowerCase().split("charset=")[1].replaceAll("[^a-z|1-9|\\-]", " ").split("\\s+")[0];
	               
	                } else {
	                    //如果没有的话，直接用gbk解码
	                    encoding = "gbk";
	                }
	            }
	            //开始读取内容正文。
	            BufferedReader br = new BufferedReader(new InputStreamReader(in, encoding));
	            String header = new String(buf, encoding);
	            //add the header to our content
	            StringBuffer sb = new StringBuffer(header);
	            char[] charBuf = new char[2048];
	            int len = br.read(charBuf);
	            while (len != -1) {
	                sb.append(charBuf, 0, len);
	                len = br.read(charBuf);
	            }
	            br.close();
	            s = sb.toString();
	            if(!s.trim().startsWith("<")){
	                s = "<"+s.trim();
	            }
	        } catch (IOException ex) {
	            //ex.printStackTrace();
	            s="";
	            
	        }finally{
	        	return s;
	        }
	}
	
	/**
	 * @param url 文件的url
	 * @param path  保存的本地路径
	 * @return
	 */
	public File SaveFile(String url,String path){
		String[] name=url.split("[/\\\\&#?]");
		//System.out.println(name);
		InputStream in = null;
		File ff=new File(path+"/"+name[name.length-1]);
		FileOutputStream out;
		try {
			out=new FileOutputStream(ff);
		} catch (FileNotFoundException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
			return null;
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			in=conn.getInputStream();
			byte[] data=new byte[2048];
			int len=0;
			while(len!=-1){
				int con=0;
				while(in.available()==0){
					 try {
							Thread.sleep(16);
						} catch (InterruptedException e) {
							// TODO 自动生成 catch 块
							e.printStackTrace();
						}
						if(con++>SelectedByUrl.URL_TIME_LIMIT)return null;
				}
				len=in.read(data);
				out.write(data, 0, len);
			}
		} catch (MalformedURLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			try {
				if(out!=null)out.close();
			} catch (IOException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
			try {
				if(in!=null)in.close();
			} catch (IOException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		return ff;
	}
	//test
	public static void main(String[] args) {
//		SelectedByUrl s=new SelectedByUrl();
		int i=0;
		while(i++<50){
			new Thread(){
				int i=0;
//				SelectedByUrl s=new SelectedByUrl();
				public void run(){
					while(true){
						System.out.println((i++)+""+SelectedByUrl.getWebBody("http://api.inewsweek.cn/api/api.ring_agree_and_neutral_and_oppose.php?ring_id=107&type=agree"));
					}
				}
			}.start();
			System.out.println((i++)+""+SelectedByUrl.getWebBody("http://api.inewsweek.cn/api/api.ring_agree_and_neutral_and_oppose.php?ring_id=107&type=agree"));
		}
		
	}

}


