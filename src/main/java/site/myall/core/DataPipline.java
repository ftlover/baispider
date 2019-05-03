package site.myall.core;

import org.apache.logging.log4j.LogManager;
import site.myall.bean.Result;
import site.myall.dao.Sqlite;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class DataPipline implements Pipeline {

    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(DataPipline.class);

    private static Sqlite sqlite = Sqlite.getInstance();

    @Override
    public void process(ResultItems resultItems, Task task) {
        String title = resultItems.get("title");
        String fileString = resultItems.get("fileString");
        if (title!=null&&title.length()!=0&&fileString!=null&&fileString.length()!=0){
            String baisiUrl = resultItems.getRequest().getUrl();
            Result result = new Result(title,baisiUrl,fileString);
            sqlite.insertResult(result);
            log.info("insertResult:{}",result.toString());
        }
    }
}
