package parsehtml;

import java.io.FileNotFoundException;
import java.io.IOException;

import search.SelectedByUrl;

/**
 *
 * @author Lamfeeling
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
//    	SelectedByUrl select=new SelectedByUrl();
        String data1 = SelectedByUrl.getWebBody("http://www.hsu.edu.cn");
        //String data1 = "<html><title></title> </html>";
        System.out.println("**********************************************");
        HTMLDocument doc = HTMLDocument.createHTMLDocument(data1);
        System.out.println(doc.getTitle() + "====\n=====" + doc.getBody());
        System.out.println("**********************************************");
    }

}