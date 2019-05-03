package site.myall.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import site.myall.util.SpiderUtil;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class PageHandler implements PageProcessor {

    private Site site = SpiderUtil.getSite();

    private static final String baseUrl ="http://www.caitabts1.com/";

    private static final Logger log= LogManager.getLogger(PageHandler.class);

    @Override
    public void process(Page page) {
        String url = page.getUrl().toString();
        log.info("process:{}",url);
        if (Pattern.compile("http://www.caitabts1.com/forum.php\\?mod=attachment&aid=.*").matcher(url).find()){
            byte[] bytes = page.getBytes();
            OutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(new File("temp.txt"));
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (outputStream != null){
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            StringBuilder result = new StringBuilder();
            InputStreamReader isr = null;
            BufferedReader read = null;
            try{
                isr = new InputStreamReader(new FileInputStream("temp.txt"), "GB2312");
                read = new BufferedReader(isr);
                String s = null;
                while((s = read.readLine())!=null){//使用readLine方法，一次读一行
                    result.append(System.lineSeparator()).append(s);
                }
            }catch(Exception e){
                e.printStackTrace();
            }finally {
                try {
                    if (read!=null){
                        read.close();
                    }
                    if (isr!=null){
                        isr.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            page.getResultItems().put("title",page.getRequest().getExtra("title"));
            page.getResultItems().put("fileString",result.toString());
            return;
        }


        if (Pattern.compile("http://www.caitabts1.com/forum.php\\?mod=forumdisplay&fid=\\d+?&page=\\d+?").matcher(url).find()){
            //寻找帖子链接
            //http://www.caitabts1.com/forum.php?mod=viewthread&tid=17944&extra=page%3D1
            List<String> requests = page.getHtml().links().regex("http://www.caitabts1.com/forum.php\\?mod=viewthread&tid=\\d+?&extra=page%.*").all();
            Collections.reverse(requests);
//            System.out.println(requests.toString());
            page.addTargetRequests(requests);
            return;
        }

        //http://www.caitabts1.com/forum.php?mod=forumdisplay&fid=91
        if (Pattern.compile("http://www.caitabts1.com/forum.php\\?mod=forumdisplay&fid=\\d+?").matcher(url).find()){
            //寻找最大页数
            String numString = page.getHtml().xpath("//span[@id='fd_page_top']").xpath("//div/label/span/text()").toString();
            String maxNum = Pattern.compile("[^0-9]").matcher(numString).replaceAll("");
            for (int i = Integer.parseInt(maxNum); i>0;i--){
                page.addTargetRequest(url+"&page="+i);
            }
            return;
        }


        //处理具体帖子
        if (Pattern.compile("http://www.caitabts1.com/forum.php\\?mod=viewthread&tid=\\d+?&extra=page%.*").matcher(url).find()){

            String title = page.getHtml().xpath("//span[@id=thread_subject]/text()").toString();
            System.out.println(title);
            String fileUrl = page.getHtml().xpath("//p[@class=attnm]/a/@href").toString();
            if (fileUrl == null)
                return;
            fileUrl = baseUrl+fileUrl;
            Request request = new Request(fileUrl);
            request.putExtra("title",title);
            page.addTargetRequest(request);
//            List<String> imageArray = null;
//
//            imageArray = page.getHtml().xpath("//div[@class='mbn savephotop']").regex("data.*?\\.jpg").all();
//
//            if (imageArray!=null){
//
//                if (imageArray.size()==0)
//
//                    imageArray = page.getHtml().xpath("//div[@id=postlist]").xpath("//div[@class='mbn savephotop']").regex("http.*?\\.jpg").all();
//
//                else {
//
//                    String baseUrl =" http://caitabts.com/";
//
//                    for(String str:imageArray) {
//
//                        int position = imageArray.indexOf(str);
//
//                        imageArray.remove(str);
//
//                        imageArray.add(position,baseUrl+str);
//
//                    }
//
//                }
//
//            }

        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
