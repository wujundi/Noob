package com.noob.spider.processor;

import com.noob.spider.pipeline.FilePipeline;
import org.junit.Ignore;
import org.junit.Test;
import com.noob.spider.Spider;
import com.noob.spider.pipeline.JsonFilePipeline;
import com.noob.spider.samples.SinaBlogProcessor;
import com.noob.spider.scheduler.FileCacheQueueScheduler;

import java.io.IOException;

/**
 * @author code4crafter@gmail.com <br>
 *         Date: 13-6-9
 *         Time: 上午8:02
 */
public class SinablogProcessorTest {

    @Ignore
    @Test
    public void test() throws IOException {
        SinaBlogProcessor sinaBlogProcessor = new SinaBlogProcessor();
        //pipeline是抓取结束后的处理
        //默认放到/data/com.noob.spider.webmagic/ftl/[domain]目录下
        JsonFilePipeline pipeline = new JsonFilePipeline("/data/com.noob.spider.webmagic/");
        //Spider.me()是简化写法，其实就是new一个啦
        //Spider.pipeline()设定一个pipeline，支持链式调用
        //ConsolePipeline输出结果到控制台
        //FileCacheQueueSchedular保存url，支持断点续传，临时文件输出到/data/temp/com.noob.spider.webmagic/cache目录
        //Spider.run()执行
        Spider.create(sinaBlogProcessor).pipeline(new FilePipeline()).pipeline(pipeline).scheduler(new FileCacheQueueScheduler("/data/temp/com.noob.spider.webmagic/cache/")).
                run();
    }
}
