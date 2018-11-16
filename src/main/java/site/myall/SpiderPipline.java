package site.myall;

import org.apache.commons.collections.map.HashedMap;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SpiderPipline implements Pipeline {
    private Set<String> urls_and_cat_urls = new HashSet<String>();
    private boolean isInit = false;

    private void init() throws SQLException {
        Sqlite3 sqlite3 = Sqlite3.getInstance("bai_spider.db");
        ResultSet resultSet = sqlite3.getUrls_catUrs();
        if (resultSet==null)
            return;
        while (resultSet.next()){
            urls_and_cat_urls.add(resultSet.getString("baisi_url"));
            urls_and_cat_urls.add(resultSet.getString("cat_url"));
        }

    }

    public void process(ResultItems resultItems, Task task) {
        if (!isInit){
            try {
                init();
                isInit = true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        Sqlite3 sqlite3 = Sqlite3.getInstance("bai_spider.db");
        Result result = resultItems.get("result");
        if (result != null)
            if(urls_and_cat_urls.add(result.getBaisi_url())&&urls_and_cat_urls.add(result.getCat_url())){
                Map<String,String> map = new HashMap<String, String>();
                map.put("title",result.getTitle());
                map.put("baisi_url",result.getBaisi_url());
                map.put("cat_url",result.getCat_url());
                map.put("image_urls",result.getImage_urls());
                sqlite3.insert("results",map);
            }

//        BufferedWriter bufferedWriter = null;
//        if (result!=null){
//            try {
//                bufferedWriter = new BufferedWriter(new FileWriter("result",true));
//                bufferedWriter.write(result.getTitle()+"---"+result.getCat_url()+"---"+result.getBaisi_url()+"---"+result.getImage_urls()+"\r\n");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }finally {
//                if (bufferedWriter!=null) {
//                    try {
//                        bufferedWriter.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
    }
}
