package search;

import java.util.InputMismatchException;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindUrl {

	/*
	 * 根据给定文本匹配出其中链接url
	 * @param text 解析文本
	 * */
	public static TreeMap<String,String[]> Find(String text){
		TreeMap<String,String[]> map=new TreeMap<String,String[]>();
		Pattern p=Pattern.compile(
				//------------------------------1-----------------------------------------------
				"\\bhref\\s*=\\s*[\"']{0,1}([^\"\\s'<>;]+)[\"']{0,1}([^<>]{0,50}title\\s*=\\s*" 
				//-------------3---------------------------------------5----------
				+"[\"']{0,1}([^\"'<>]+)[\"']{0,1}){0,1}([^<>]{0,50}>([^<\n]+)<){0,1}" 
				 
				//----------------------------------6-----------------------------------8-------------
				+"|"+"\\bvalue\\s*=\\s*[\"']{0,1}(http://[^\"\\s;<>]+)[\"']{0,1}([^<>]{0,50}>([^<\n]+)<){0,1}"
		);
		Matcher m=p.matcher(text);
		while(m.find()){
			try {
				String href1=m.group(1);
				String href2=m.group(3);
				String href3=m.group(5);
				String value1=m.group(6);
				String value3=m.group(8);
				System.out.print(href1+" ");
				System.out.print(href2+" ");
				System.out.print(href3+" ");
				System.out.print(value1+" ");
				System.out.println(value3+" ");
				if(href1==null)href1=value1;
				if(href2==null)href2=href3;
				if(href2==null)href2=value3;
				//System.out.println(++num+"........................................"+href1);for(int i=0;i<9;i++)System.out.println(i+": "+m.group(i));
				if(!href1.matches(".*(javascript|css).*")){
					String[] arr={href1,href2};
					map.put(href1,arr);
				}
			} catch (InputMismatchException e) {
				e.printStackTrace();
			}
			
		}
		return map;
	}
	public TreeMap<String,String[]> FindImg(){
		TreeMap<String,String[]> map=new TreeMap<String,String[]>();
		/*Pattern p=Pattern.compile(
				""
		);*/
		return map;
	}
	public static void main(String[] args){
		String url="http://www.hsu.edu.cn/";
		SelectedByUrl select=new SelectedByUrl();
//		FindUrl find=new FindUrl();
		select.SaveFile(url, "d:");
		String text=SelectedByUrl.getWebBody(url);
		//long time=System.currentTimeMillis();
		//String text="0: href=\"javascript:void(0)\"";
		TreeMap<String,String[]> list=FindUrl.Find(text);
		System.out.println(list.size());
		/*for(String[] arr:list.values()){
			System.out.println("...."+arr[0]+"------"+arr[1]+"....");
			System.out.println("---------------------------------------------------------------");
		}*/
		//System.out.println(System.currentTimeMillis()-time);
	}
}
