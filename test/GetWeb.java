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
import java.util.ArrayList;//�����б� 
import java.util.regex.Matcher; 
import java.util.regex.Pattern; 
import java.util.Hashtable; 

public class GetWeb { 

        private int webDepth = 50;//������� 
        private int intThreadNum = 30;//�߳��� 
        private ArrayList<String> non_visited = new ArrayList<String>();//�����洢δ����URL 
        private ArrayList<String> visited = new ArrayList<String>();//�����洢�ѷ���URL 
        private Hashtable<String,Integer> deepUrls = new Hashtable<String,Integer>();//�洢����URL��� 
        private String regex_script ="<[ \\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";//���� script ��ǩ 
        private String regex_style ="<[ \\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";//����css 
        private String regex_tag ="<[^>]*?>";//���� html ��ǩ ��"<"��ͷ �� ">"��β        
        private String regex_t ="\t";        //�����Ʊ�� 
        private String regex_s =" +";   //���˿ո� 
        private String regex_n ="\n";   //���˻��з� 
        private String regex_spe ="\\&[^;]+;";//����������� 
        private String regex_sin = "'"; //���˵����� 
        private String regex_Url = "<a\\s+href\\s*=\\s*\"/*([^>]*?)/*\\s*\"[^>]*>";//�������� 
        private String regex_file = ".htm|.html|.jsp|[^/]asp|.aspx";//�����ļ� 
        private String regex_limit = "192.168|172.16|218.22|hsu";//����У������ 
        private String regex_Scr = "javascript|mailto|downid|19:80/vip/"; 
        private String regex_title = "<title.*?>(.+?)</title>";//����title 
        private String regex_description = "<meta\\s+name\\s*=\\s*\"\\s*description\\s*\"\\s+content\\s*=\\s*\"(.+?)\"[^>]*>" + 
        "|<meta\\s+content\\s*=\\s*\"(.+?)\"\\s+name\\s*=\\s*\"\\s*description\\s*\"[^>]*>";//����meta_description 
        private String regex_keywords = "<meta\\s+name\\s*=\\s*\"\\s*keywords\\s*\"\\s+content\\s*=\\s*\"(.+?)\"[^>]*>" + 
        "|<meta\\s+content\\s*=\\s*\"(.+?)\"\\s+name\\s*=\\s*\"\\s*keywords\\s*\"[^>]*>";//����meta_keywords 
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

//        ��δ����url�б���ȡ��һ��url����,����non_visited�б���ɾ������ 
        public synchronized String getAUrl() 
        { 
                String tmpAUrl = non_visited.get(0); 
                non_visited.remove(0); 
                return tmpAUrl; 
        } 

//        ����ҳ��ؼ���(title,meta) 
        public String parse(Pattern p,String code){ 
                String s = null; 
                Matcher m = p.matcher(code); 
                if(m.find()){ 
                        s = (m.group(1)==null)? m.group(2):m.group(1); 
                } 
                return s; 
        } 

        //������ҳ��ǩ 
        public String filter(Pattern p,String html){ 
                Matcher m = p.matcher(html);                
                html = m.replaceAll(""); 
                return html; 
        } 

//        main����   
        public static void main(String[] args) 
        { 
                GetWeb gw = new GetWeb(); 
                gw.getBeginPage(); 
        } 


//        ��ɨ�����ݿ��ó�ʼҳ�� 
        public void getBeginPage() 
        { 
                int count = db.countWebID(); 
                String webURL = null; 
                for(int i=1;i<=count;i++){ 
                        webURL = db.getWebUrls(i); 
                        non_visited.add(webURL);//������ҳ��ӵ�δ����URL�б�β�� 
                        visited.add(webURL);//������ҳ��ӵ��ѷ���URL�б�β�� 
                        deepUrls.put(webURL,1);//������ҳ(url)��Ϊ��ֵ���url���1����ӳ�� 
                } 

                //�������߳� 
                for (int i=0;i<intThreadNum;i++) 
                { 
                        new Thread(new Processer()).start();         
                }                   
        } 



//        ץȥ��ҳ���ж��Ƿ�ﵽ�������"getWebByUrl()" 
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
                        conn.setDoOutput(true);  //ʹ������conn�������뻹������� 
                        conn.setConnectTimeout(1000); 
                        //conn.setReadTimeout(100000); 
                        try{ 
                                conn.connect(); 
                        }catch(SocketTimeoutException e){ 
                                System.out.println("Get web failed!(���ӳ�ʱ!)      " + strUrl); 
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
                                System.out.println("Get web failed!(��ȡ��ʱ!)      " + strUrl); 
                                //e.printStackTrace(); 
                                return; 
                        } 
                        bReader = new BufferedReader(new InputStreamReader(is)); 
                        sBuffer = new StringBuffer();//�����ҳȫ������ 
                        while ( (rLine = bReader.readLine()) != null) 
                        { 
                                sBuffer.append(rLine);//����ҳ���ݴ浽������ 
                                rLine = null; 
                        } 
                        bReader.close(); 
                        code = sBuffer.toString(); 
                        code = new String(code.getBytes("gbk"),charset); //������� 
                } catch (MalformedURLException e) { 
                        System.out.println("Get web failed!(�����url!)      " + strUrl); 
                        //e.printStackTrace(); 
                        return; 
                } catch (NullPointerException e) { 
                        System.out.println("Get web failed!(��ָ�����!)      " + strUrl); 
                        //e.printStackTrace(); 
                        return; 
                } catch (UnsupportedEncodingException e) { 
                        System.out.println("Get web failed!(����ı���!)      " + strUrl); 
                        //e.printStackTrace(); 
                        return; 
                } catch (IOException e) { 
                        System.out.println("Get web failed!(IO����!)      " + strUrl); 
                        //e.printStackTrace(); 
                        return; 
                } 
                if (deepUrls.get(strUrl) < webDepth){  //�жϵ�ǰurl������Ƿ񳬹����õ�������� 
                        getUrlByString(code,strUrl);//����getUrlByString()������ȡҳ���а��������� 
                } 
                //������ҳ����,meta��Ϣ;���˱�ǩ 
                title = parse(p_title,code);                 //����title 
                keywords = parse(p_keywords,code);      //����meta keywords 
                description = parse(p_description,code);//����meta description 
                code = filter(p_script,code);  //���� script ��ǩ 
                code = filter(p_style,code);   //����css 
                code = filter(p_tag,code);     //���� html ��ǩ ��"<"��ͷ �� ">"��β 
                code = filter(p_t,code);       //�����Ʊ�� 
                code = filter(p_s,code);       //���˿ո� 
                code = filter(p_n,code);       //���˻��з� 
                code = filter(p_spe,code);     //����������� 
                code = filter(p_sin,code);    //���˵����� 
                db.putWebContent(strUrl,deepUrls.get(strUrl),title,keywords,description,code);//��ҳ��洢�� 
                System.out.println("Get web successfully! "          + strUrl); 
        } 

//        "getUrlByString()"���Gatherÿ�ν�ȡ��һ������,���ҵ����е�url,����non_visited,visited,deepUrls�� 
        public void getUrlByString(String tmpStr,String strUrl) 
        { 
                String tempURL = null; 
                Matcher m_Url = p_Url.matcher(tmpStr); 
                while(m_Url.find()){        //�ҵ�һ��<a>��ǩ 
                        tempURL = m_Url.group(1);//�ѱ�ǩ��������ó��� 
                        Matcher m_Scr = p_Scr.matcher(tempURL); 
                        if(m_Scr.find()){ 
                                continue; 
                        } 
                        if(tempURL.contains("http://")){//����"http://"���������� 
                                Matcher m_limit = p_limit.matcher(tempURL); 
                                if(!m_limit.find()){  //���������������������� 
                                        continue; 
                                } 
                        }else{   //������"http://"�����·�� 
                                if(!tempURL.contains("../")){ 
                                        int flag1 = strUrl.indexOf("/", 7);//flag1��ʾhttp://֮���һ�γ���"/"��λ�� 
                                        try{                             //����Ե�ַ�ӵ���ҳ��ַ�� 
                                                tempURL = strUrl.substring(0,flag1)+"/"+tempURL;//�԰���file�ĸ�url���д��� 
                                        }catch(StringIndexOutOfBoundsException e){ 
                                                tempURL = strUrl+"/"+tempURL; //������file�ĸ�urlֱ�Ӽӵ���Ե�ַǰ 
                                        } 
                                }else{ 
                                        int flag2 = strUrl.lastIndexOf("/");//flag2��ʾ��url���һ�γ���"/"��λ�� 
                                        String sub = strUrl.substring(0,flag2); 
                                        flag2 = sub.lastIndexOf("/");    //�ٽ�һ�� 
                                        sub = strUrl.substring(0,flag2); 
                                        tempURL = sub + "/" + tempURL.substring(3);//��"../"�ص� 
                                } 
                        } 
                        if (!visited.contains(tempURL)){   //�����ƥ������urlû�����ѷ����б��г��ֹ� 
                                non_visited.add(tempURL);      //����ǰurl����δ����URL�б��� 
                                visited.add(tempURL);          //����ǰurl�����ѷ���URL�б��� 
                                deepUrls.put(tempURL,(deepUrls.get(strUrl)+1));//����ǰurl�Ͷ�Ӧ����ҳ��ȷ���deepUrls(hashtable)�� 
                        } 
                } 
        } 


        //ҳ��ץȡ֩�� 
        class Processer implements Runnable 
        { 
                public void run() 
                { 
                        while (!non_visited.isEmpty())//���δ�����б�Ϊ��,ÿ�λ��һ��urlȥץҳ�� 
                        { 
                                getWebByUrl(getAUrl()); 
                        } 
                } 
        } 
} 