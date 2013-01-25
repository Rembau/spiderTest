package test;
import java.io.BufferedReader; 
import java.io.IOException; 
import java.io.InputStream; 
import java.io.InputStreamReader; 
import java.io.UnsupportedEncodingException; 
import java.net.MalformedURLException; 
import java.net.SocketTimeoutException; 
import java.net.URL; 
import java.net.URLConnection; 
import java.util.ArrayList;//数组列表 
import java.util.regex.Matcher; 
import java.util.regex.Pattern; 
import java.util.Hashtable; 

public class GetWeb { 

        private int webDepth = 50;//爬虫深度 
        private int intThreadNum = 30;//线程数 
        private ArrayList<String> non_visited = new ArrayList<String>();//用来存储未访问URL 
        private ArrayList<String> visited = new ArrayList<String>();//用来存储已访问URL 
        private Hashtable<String,Integer> deepUrls = new Hashtable<String,Integer>();//存储所有URL深度 
        private String regex_script ="<[ \\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";//过滤 script 标签 
        private String regex_style ="<[ \\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";//过滤css 
        private String regex_tag ="<[^>]*?>";//过滤 html 标签 以"<"开头 以 ">"结尾        
        private String regex_t ="\t";        //过滤制表符 
        private String regex_s =" +";   //过滤空格 
        private String regex_n ="\n";   //过滤换行符 
        private String regex_spe ="\\&[^;]+;";//过滤特殊符号 
        private String regex_sin = "'"; //过滤单引号 
        private String regex_Url = "<a\\s+href\\s*=\\s*\"/*([^>]*?)/*\\s*\"[^>]*>";//解析链接 
        private String regex_file = ".htm|.html|.jsp|[^/]asp|.aspx";//过滤文件 
        private String regex_limit = "192.168|172.16|218.22|hsu";//限制校外链接 
        private String regex_Scr = "javascript|mailto|downid|19:80/vip/"; 
        private String regex_title = "<title.*?>(.+?)</title>";//解析title 
        private String regex_description = "<meta\\s+name\\s*=\\s*\"\\s*description\\s*\"\\s+content\\s*=\\s*\"(.+?)\"[^>]*>" + 
        "|<meta\\s+content\\s*=\\s*\"(.+?)\"\\s+name\\s*=\\s*\"\\s*description\\s*\"[^>]*>";//解析meta_description 
        private String regex_keywords = "<meta\\s+name\\s*=\\s*\"\\s*keywords\\s*\"\\s+content\\s*=\\s*\"(.+?)\"[^>]*>" + 
        "|<meta\\s+content\\s*=\\s*\"(.+?)\"\\s+name\\s*=\\s*\"\\s*keywords\\s*\"[^>]*>";//解析meta_keywords 
        private Pattern p_Url = Pattern.compile(regex_Url,Pattern.CASE_INSENSITIVE); 
        private Pattern p_limit = Pattern.compile(regex_limit,Pattern.CASE_INSENSITIVE); 
        private Pattern p_file = Pattern.compile(regex_file,Pattern.CASE_INSENSITIVE); 
        private Pattern p_Scr = Pattern.compile(regex_Scr,Pattern.CASE_INSENSITIVE); 
        private Pattern p_script = Pattern.compile(regex_script,Pattern.CASE_INSENSITIVE); 
        private Pattern p_style = Pattern.compile(regex_style,Pattern.CASE_INSENSITIVE); 
        private Pattern p_tag = Pattern.compile(regex_tag,Pattern.CASE_INSENSITIVE); 
        private Pattern p_t = Pattern.compile(regex_t,Pattern.CASE_INSENSITIVE); 
        private Pattern p_s = Pattern.compile(regex_s,Pattern.CASE_INSENSITIVE); 
        private Pattern p_n = Pattern.compile(regex_n,Pattern.CASE_INSENSITIVE); 
        private Pattern p_spe = Pattern.compile(regex_spe,Pattern.CASE_INSENSITIVE); 
        private Pattern p_sin = Pattern.compile(regex_sin,Pattern.CASE_INSENSITIVE); 
        private Pattern p_title = Pattern.compile(regex_title,Pattern.CASE_INSENSITIVE); 
        private Pattern p_description = Pattern.compile(regex_description,Pattern.CASE_INSENSITIVE); 
        private Pattern p_keywords = Pattern.compile(regex_keywords,Pattern.CASE_INSENSITIVE); 

        PutInDB db = new PutInDB(); 

//        从未访问url列表中取第一个url返回,并在non_visited列表中删除该项 
        public synchronized String getAUrl() 
        { 
                String tmpAUrl = non_visited.get(0); 
                non_visited.remove(0); 
                return tmpAUrl; 
        } 

//        解析页面关键字(title,meta) 
        public String parse(Pattern p,String code){ 
                String s = null; 
                Matcher m = p.matcher(code); 
                if(m.find()){ 
                        s = (m.group(1)==null)? m.group(2):m.group(1); 
                } 
                return s; 
        } 

        //过滤网页标签 
        public String filter(Pattern p,String html){ 
                Matcher m = p.matcher(html);                
                html = m.replaceAll(""); 
                return html; 
        } 

//        main方法   
        public static void main(String[] args) 
        { 
                GetWeb gw = new GetWeb(); 
                gw.getBeginPage(); 
        } 


//        从扫描数据库获得初始页面 
        public void getBeginPage() 
        { 
                int count = db.countWebID(); 
                String webURL = null; 
                for(int i=1;i<=count;i++){ 
                        webURL = db.getWebUrls(i); 
                        non_visited.add(webURL);//将该主页添加到未访问URL列表尾部 
                        visited.add(webURL);//将该主页添加到已访问URL列表尾部 
                        deepUrls.put(webURL,1);//将该主页(url)作为键值与该url深度1进行映射 
                } 

                //开启多线程 
                for (int i=0;i<intThreadNum;i++) 
                { 
                        new Thread(new Processer()).start();         
                }                   
        } 



//        抓去网页并判断是否达到爬虫深度"getWebByUrl()" 
        public  void getWebByUrl(String strUrl) 
        {                         // url      
                URL url = null; 
                URLConnection conn = null; 
                String charset = null; 
                String rLine = null; 
                String code = null; 
                String title = null; 
                String keywords = null; 
                String description = null; 
                StringBuffer sBuffer = null; 
                InputStream is = null; 
                BufferedReader bReader = null; 
                try { 
                        url = new URL(strUrl); 
                        Matcher m_file = p_file.matcher(url.getFile()); 
                        if(url.getFile()!=""&&url.getFile().contains(".")&&!m_file .find()){ 
                                return; 
                        } 
                        conn = url.openConnection(); 
                        conn.setDoOutput(true);  //使该连接conn除了输入还可以输出 
                        conn.setConnectTimeout(1000); 
                        //conn.setReadTimeout(100000); 
                        try{ 
                                conn.connect(); 
                        }catch(SocketTimeoutException e){ 
                                System.out.println("Get web failed!(连接超时!)      " + strUrl); 
                                //e.printStackTrace(); 
                                return; 
                        } 
                        if((charset = conn.getContentType())!=null){ 
                                if(charset.contains("charset")){ 
                                        charset = charset.substring(charset.indexOf("=")+1); 
                                }else{ 
                                        charset = "gb2312"; 
                                } 
                        }else{ 
                                charset = "gb2312"; 
                        } 
                        try{ 
                                is = conn.getInputStream(); 
                        }catch(SocketTimeoutException e){ 
                                System.out.println("Get web failed!(读取超时!)      " + strUrl); 
                                //e.printStackTrace(); 
                                return; 
                        } 
                        bReader = new BufferedReader(new InputStreamReader(is)); 
                        sBuffer = new StringBuffer();//存放网页全部代码 
                        while ( (rLine = bReader.readLine()) != null) 
                        { 
                                sBuffer.append(rLine);//将网页内容存到缓存中 
                                rLine = null; 
                        } 
                        bReader.close(); 
                        code = sBuffer.toString(); 
                        code = new String(code.getBytes("gbk"),charset); //编码过滤 
                } catch (MalformedURLException e) { 
                        System.out.println("Get web failed!(错误的url!)      " + strUrl); 
                        //e.printStackTrace(); 
                        return; 
                } catch (NullPointerException e) { 
                        System.out.println("Get web failed!(空指针错误!)      " + strUrl); 
                        //e.printStackTrace(); 
                        return; 
                } catch (UnsupportedEncodingException e) { 
                        System.out.println("Get web failed!(错误的编码!)      " + strUrl); 
                        //e.printStackTrace(); 
                        return; 
                } catch (IOException e) { 
                        System.out.println("Get web failed!(IO出错!)      " + strUrl); 
                        //e.printStackTrace(); 
                        return; 
                } 
                if (deepUrls.get(strUrl) < webDepth){  //判断当前url的深度是否超过设置的爬虫深度 
                        getUrlByString(code,strUrl);//调用getUrlByString()方法获取页面中包含的连接 
                } 
                //解析网页标题,meta信息;过滤标签 
                title = parse(p_title,code);                 //解析title 
                keywords = parse(p_keywords,code);      //解析meta keywords 
                description = parse(p_description,code);//解析meta description 
                code = filter(p_script,code);  //过滤 script 标签 
                code = filter(p_style,code);   //过滤css 
                code = filter(p_tag,code);     //过滤 html 标签 以"<"开头 以 ">"结尾 
                code = filter(p_t,code);       //过滤制表符 
                code = filter(p_s,code);       //过滤空格 
                code = filter(p_n,code);       //过滤换行符 
                code = filter(p_spe,code);     //过滤特殊符号 
                code = filter(p_sin,code);    //过滤单引号 
                db.putWebContent(strUrl,deepUrls.get(strUrl),title,keywords,description,code);//入页面存储库 
                System.out.println("Get web successfully! "          + strUrl); 
        } 

//        "getUrlByString()"获得Gather每次截取的一行文字,并找到其中的url,存入non_visited,visited,deepUrls中 
        public void getUrlByString(String tmpStr,String strUrl) 
        { 
                String tempURL = null; 
                Matcher m_Url = p_Url.matcher(tmpStr); 
                while(m_Url.find()){        //找到一个<a>标签 
                        tempURL = m_Url.group(1);//把标签里的链接拿出来 
                        Matcher m_Scr = p_Scr.matcher(tempURL); 
                        if(m_Scr.find()){ 
                                continue; 
                        } 
                        if(tempURL.contains("http://")){//包含"http://"的完整链接 
                                Matcher m_limit = p_limit.matcher(tempURL); 
                                if(!m_limit.find()){  //若该链接是外网链接则丢弃 
                                        continue; 
                                } 
                        }else{   //不包含"http://"的相对路径 
                                if(!tempURL.contains("../")){ 
                                        int flag1 = strUrl.indexOf("/", 7);//flag1表示http://之后第一次出现"/"的位置 
                                        try{                             //将相对地址加到主页地址后 
                                                tempURL = strUrl.substring(0,flag1)+"/"+tempURL;//对包含file的父url进行处理 
                                        }catch(StringIndexOutOfBoundsException e){ 
                                                tempURL = strUrl+"/"+tempURL; //不包含file的父url直接加到相对地址前 
                                        } 
                                }else{ 
                                        int flag2 = strUrl.lastIndexOf("/");//flag2表示父url最后一次出现"/"的位置 
                                        String sub = strUrl.substring(0,flag2); 
                                        flag2 = sub.lastIndexOf("/");    //再截一次 
                                        sub = strUrl.substring(0,flag2); 
                                        tempURL = sub + "/" + tempURL.substring(3);//把"../"截掉 
                                } 
                        } 
                        if (!visited.contains(tempURL)){   //如果所匹配的这个url没有在已访问列表中出现过 
                                non_visited.add(tempURL);      //将当前url加入未访问URL列表中 
                                visited.add(tempURL);          //将当前url加入已访问URL列表中 
                                deepUrls.put(tempURL,(deepUrls.get(strUrl)+1));//将当前url和对应的网页深度放入deepUrls(hashtable)中 
                        } 
                } 
        } 


        //页面抓取蜘蛛 
        class Processer implements Runnable 
        { 
                public void run() 
                { 
                        while (!non_visited.isEmpty())//如果未访问列表不为空,每次获得一个url去抓页面 
                        { 
                                getWebByUrl(getAUrl()); 
                        } 
                } 
        } 
} 