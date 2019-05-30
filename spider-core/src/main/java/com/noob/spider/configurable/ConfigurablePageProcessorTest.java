package com.noob.spider.configurable;

import com.noob.spider.downloader.MockGithubDownloader;
import org.junit.Test;
import com.noob.spider.ResultItems;
import com.noob.spider.Site;
import com.noob.spider.Spider;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author code4crafter@gmail.com
 * @date 14-4-5
 */
public class ConfigurablePageProcessorTest {

    @Test
    public void test() throws Exception {
        List<ExtractRule> extractRules = new ArrayList<ExtractRule>();
        ExtractRule extractRule = new ExtractRule();
        extractRule.setExpressionType(ExpressionType.XPath);
        extractRule.setExpressionValue("//title");
        extractRule.setFieldName("title");
        extractRules.add(extractRule);
        extractRule = new ExtractRule();
        extractRule.setExpressionType(ExpressionType.XPath);
        extractRule.setExpressionValue("//ul[@class='pagehead-actions']/li[1]//a[@class='social-count js-social-count']/text()");
        extractRule.setFieldName("star");
        extractRules.add(extractRule);
        ResultItems resultItems = Spider.create(new ConfigurablePageProcessor(Site.me(), extractRules))
                .setDownloader(new MockGithubDownloader()).get("https://github.com/code4craft/webmagic");
        assertThat(resultItems.getAll()).containsEntry("title", "<title>code4craft/com.noob.spider.webmagic · GitHub</title>");
        assertThat(resultItems.getAll()).containsEntry("star", " 86 ");

    }
}
