package site.myall;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class PageHandler implements PageProcessor {
    Site site = new Site().setRetryTimes(3).setSleepTime(100);
    public void process(Page page) {
        String url = page.getUrl().toString();
        //System.out.println(url);

        if (Pattern.compile("http://caitabts.com/forum.php\\?mod=forumdisplay&fid=\\d+?&page=\\d+?").matcher(url).find()){
            //寻找帖子链接
            List<String> requests = page.getHtml().links().regex("http://caitabts.com/forum.php\\?mod=viewthread&tid=\\d+?&extra=page%.*").all();
            Collections.reverse(requests);
            page.addTargetRequests(requests);
            return;
        }

        if (Pattern.compile("http://caitabts.com/forum.php\\?mod=forumdisplay&fid=\\d+?").matcher(url).find()){
            //寻找最大页数
            String numString = page.getHtml().xpath("//span[@id='fd_page_top']").xpath("//label/span/text()").toString();
            String maxNum = Pattern.compile("[^0-9]").matcher(numString).replaceAll("");
            for (int i = Integer.parseInt(maxNum); i>0;i--)
                page.addTargetRequest(url+"&page="+i);
            return;
        }

        if (Pattern.compile("http://caitabts.com/forum.php\\?mod=viewthread&tid=\\d+?&extra=page%.*").matcher(url).find()){
            String title = page.getHtml().xpath("//span[@id=thread_subject]/text()").toString();
            String cat_url = page.getHtml().xpath("//div[@id=postlist]").xpath("//div[@class=t_fsz]/table").links().all().toString().replaceAll("\\]","").replaceAll("\\[","");
            List<String> imageArray = null;
            imageArray = page.getHtml().xpath("//div[@id=postlist]").xpath("//div[@class='mbn savephotop']").regex("data.*?\\.jpg").all();
            if (imageArray!=null){
                if (imageArray.size()==0)
                    imageArray = page.getHtml().xpath("//div[@id=postlist]").xpath("//div[@class='mbn savephotop']").regex("http.*?\\.jpg").all();
                else {
                    String baseUrl =" http://caitabts.com/";
                    for(String str:imageArray) {
                        int position = imageArray.indexOf(str);
                        imageArray.remove(str);
                        imageArray.add(position,baseUrl+str);
                    }
                }
            }
            String imageUrls = imageArray.toString();

            if (title.length()!=0&&cat_url.length()!=0)
                page.putField("result",new Result(title,url,cat_url,imageUrls));

            return;
        }

    }

    public Site getSite() {
        site.addHeader("Host","caitabts.com")
                .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36")
                .addHeader("Referer","http://caitabts.com/forum.php");
        site.setDomain("caitabts.com");
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

        return site;
    }


}
