package com.noob.spider.extension.monitor;

import com.noob.spider.core.Spider;
import com.noob.spider.core.processor.example.GithubRepoPageProcessor;
import com.noob.spider.core.processor.example.ZhihuPageProcessor;
import org.junit.Test;

/**
 * @author code4crafer@gmail.com
 * @since 0.5.0
 */
public class SpiderMonitorTest {

    @Test
    public void testInherit() throws Exception {
        SpiderMonitor spiderMonitor = new SpiderMonitor(){
            @Override
            protected SpiderStatusMXBean getSpiderStatusMBean(Spider spider, MonitorSpiderListener monitorSpiderListener) {
                return new CustomSpiderStatus(spider, monitorSpiderListener);
            }
        };

        Spider zhihuSpider = Spider.create(new ZhihuPageProcessor())
                .addUrl("http://my.oschina.net/flashsword/blog").thread(2);
        Spider githubSpider = Spider.create(new GithubRepoPageProcessor())
                .addUrl("https://github.com/code4craft");

        spiderMonitor.register(zhihuSpider, githubSpider);

    }
}
