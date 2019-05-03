package site.myall.util;

import us.codecraft.webmagic.Site;

import java.io.*;

public class SpiderUtil {

    private static final Site site;

    static {
        site = new Site().setRetryTimes(3).setSleepTime(1000);
        site.addHeader("Host","www.caitabts1.com")
                .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36")
                .addHeader("Referer","http://www.caitabts1.com/forum.php")
                .addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                .setDomain("www.caitabts1.com");
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("cookie")));
            String cookiesString = bufferedReader.readLine();
            String [] cookies = cookiesString.split(";");
            for (String str:cookies){
                String [] strs = str.split("=");
                site.addCookie(strs[0],strs[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Site getSite(){
        return site;
    }
}
