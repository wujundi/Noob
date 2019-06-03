package com.noob.spider.selector;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author code4crafter@gmail.com <br>
 */
public class ExtractorsTest {

    String html = "<div><h1>test<a href=\"xxx\">aabbcc</a></h1></div>";

    String html2 = "<title>aabbcc</title>";

    @Test
    public void testEach() {
        assertThat(Selectors.$("div h1 a").select(html)).isEqualTo("<a href=\"xxx\">aabbcc</a>");
        assertThat(Selectors.$("div h1 a", "href").select(html)).isEqualTo("xxx");
        assertThat(Selectors.$("div h1 a", "innerHtml").select(html)).isEqualTo("aabbcc");
        assertThat(Selectors.xpath("//a/@href").select(html)).isEqualTo("xxx");
        assertThat(Selectors.regex("a href=\"(.*)\"").select(html)).isEqualTo("xxx");
        assertThat(Selectors.regex("(a href)=\"(.*)\"", 2).select(html)).isEqualTo("xxx");
    }

    @Test
    public void testCombo() {
        assertThat(Selectors.and(Selectors.$("title"), Selectors.regex("aa(bb)cc")).select(html2)).isEqualTo("bb");
        OrSelector or = Selectors.or(Selectors.$("div h1 a", "innerHtml"), Selectors.xpath("//title"));
        assertThat(or.select(html)).isEqualTo("aabbcc");
        assertThat(or.select(html2)).isEqualTo("<title>aabbcc</title>");
    }
}
