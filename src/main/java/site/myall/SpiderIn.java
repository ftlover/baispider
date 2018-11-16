package site.myall;

import us.codecraft.webmagic.Spider;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SpiderIn {
    public static void main(String[] args) {
        List<String> urlsArray = new ArrayList<String>();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("urls")));
            String line = null;
            while ((line=bufferedReader.readLine())!=null){
                if(line.length()==0)
                    continue;
                if(line.charAt(0)== '#')
                    continue;
                urlsArray.add(line);
            }
            if(urlsArray.size()!=0){
                Spider spider = Spider.create(new PageHandler()).addPipeline(new SpiderPipline());
                Sqlite3 sqlite3 = Sqlite3.getInstance("bai_spider.db");
                sqlite3.createTable("results");
                for (String url:urlsArray)
                    spider.addUrl(url);
                spider.thread(1)
                        .run();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader!=null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


//        Sqlite3 sqlite3 = Sqlite3.getInstance("bai_spider.db");
//        sqlite3.createTable("results");
//        Spider.create(new PageHandler()).addPipeline(new SpiderPipline())
////                .addUrl("http://caitabts.com/forum.php?mod=viewthread&tid=3006&extra=page%3D1")
//                .addUrl("http://caitabts.com/forum.php?mod=forumdisplay&fid=87")
//                .addUrl("http://caitabts.com/forum.php?mod=forumdisplay&fid=2",
//                        "http://caitabts.com/forum.php?mod=forumdisplay&fid=74",
//                        "http://caitabts.com/forum.php?mod=forumdisplay&fid=75",
//                        "http://caitabts.com/forum.php?mod=forumdisplay&fid=76",
//                        "http://caitabts.com/forum.php?mod=forumdisplay&fid=90",
//                        "http://caitabts.com/forum.php?mod=forumdisplay&fid=77",
//                        "http://caitabts.com/forum.php?mod=forumdisplay&fid=89")
//                .thread(1)
//                .run();
//
//
    }
}
