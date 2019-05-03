package site.myall;

import site.myall.core.DataPipline;
import site.myall.core.PageHandler;
import site.myall.dao.Sqlite;
import us.codecraft.webmagic.Spider;

public class SpiderIn {
    public static void main(String[] args) {
        Sqlite sqlite = Sqlite.getInstance();
        if(!sqlite.existTable("results")){
            sqlite.createTable("results");
        }
        Spider.create(new PageHandler())
                .addPipeline(new DataPipline())
                .addUrl("http://www.caitabts1.com/forum.php?mod=forumdisplay&fid=91")
                .thread(1)
                .run();
    }
}
