package com.noob.spider.model.samples;

import com.noob.spider.model.annotation.ExtractBy;
import com.noob.spider.Site;
import com.noob.spider.model.OOSpider;

/**
 * @author code4crafter@gmail.com
 */
public class BaiduNews {

    @ExtractBy("//h3[@class='c-title']/a/text()")
    private String name;

    @ExtractBy("//div[@class='c-summary']/text()")
    private String description;

    @Override
    public String toString() {
        return "BaiduNews{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public static void main(String[] args) {
        OOSpider ooSpider = OOSpider.create(Site.me().setSleepTime(0), BaiduNews.class);
        //single download
        BaiduNews baike = ooSpider.<BaiduNews>get("http://news.baidu.com/ns?tn=news&cl=2&rn=20&ct=1&fr=bks0000&ie=utf-8&word=httpclient");
        System.out.println(baike);

        ooSpider.close();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}