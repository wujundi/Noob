package com.noob.spider.extension.example;

import com.noob.spider.core.Site;
import com.noob.spider.extension.model.ConsolePageModelPipeline;
import com.noob.spider.extension.model.HasKey;
import com.noob.spider.extension.model.OOSpider;
import com.noob.spider.extension.model.annotation.ExtractBy;
import com.noob.spider.extension.model.annotation.ExtractByUrl;

import java.util.List;

/**
 * @author code4crafter@gmail.com <br>
 * @since 0.4.1
 */
public class GithubRepoApi implements HasKey {

    @ExtractBy(type = ExtractBy.Type.JsonPath, value = "$.name", source = ExtractBy.Source.RawText)
    private String name;

    @ExtractBy(type = ExtractBy.Type.JsonPath, value = "$..owner.login", source = ExtractBy.Source.RawText)
    private String author;

    @ExtractBy(type = ExtractBy.Type.JsonPath, value = "$.language",multi = true, source = ExtractBy.Source.RawText)
    private List<String> language;

    @ExtractBy(type = ExtractBy.Type.JsonPath, value = "$.stargazers_count", source = ExtractBy.Source.RawText)
    private int star;

    @ExtractBy(type = ExtractBy.Type.JsonPath, value = "$.forks_count", source = ExtractBy.Source.RawText)
    private int fork;

    @ExtractByUrl
    private String url;

    public static void main(String[] args) {
        OOSpider.create(Site.me().setSleepTime(100)
                , new ConsolePageModelPipeline(), GithubRepoApi.class)
                .addUrl("https://api.github.com/repos/code4craft/webmagic").run();
    }

    @Override
    public String key() {
        return author + ":" + name;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public List<String> getLanguage() {
        return language;
    }

    public String getUrl() {
        return url;
    }

    public int getStar() {
        return star;
    }

    public int getFork() {
        return fork;
    }
}
