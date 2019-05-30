package com.noob.spider.samples;

import com.noob.spider.processor.PageProcessor;
import com.noob.spider.selector.PlainText;
import com.noob.spider.Page;
import com.noob.spider.Site;
import com.noob.spider.Spider;

import java.util.List;

/**
 * @author code4crafter@gmail.com <br>
 * Date: 13-4-21
 * Time: 下午8:08
 */
public class DiaoyuwengProcessor implements PageProcessor {

    private Site site;

    @Override
    public void process(Page page) {
        List<String> requests = page.getHtml().links().regex("(http://www\\.diaoyuweng\\.com/home\\.php\\?mod=space&uid=88304&do=thread&view=me&type=thread&order=dateline&from=space&page=\\d+)").all();
        page.addTargetRequests(requests);
        requests = page.getHtml().links().regex("(http://www\\.diaoyuweng\\.com/thread-\\d+-1-1.html)").all();
        page.addTargetRequests(requests);
        if (page.getUrl().toString().contains("thread")){
            page.putField("title", page.getHtml().xpath("//a[@id='thread_subject']"));
            page.putField("content", page.getHtml().xpath("//div[@class='pcb']//tbody/tidyText()"));
            page.putField("date",page.getHtml().regex("发表于 (\\d{4}-\\d+-\\d+ \\d+:\\d+:\\d+)"));
            page.putField("id",new PlainText("1000"+page.getUrl().regex("http://www\\.diaoyuweng\\.com/thread-(\\d+)-1-1.html").toString()));
        }
    }

    @Override
    public Site getSite() {
        if (site==null){
            site= Site.me().setDomain("www.diaoyuweng.com").
                    setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31").setCharset("GBK").setSleepTime(500);
        }
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new DiaoyuwengProcessor()).addUrl("http://www.diaoyuweng.com/home.php?mod=space&uid=88304&do=thread&view=me&type=thread&from=space").run();
    }
}
