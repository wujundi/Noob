package com.noob.spider.core.selector;

import com.noob.spider.core.selector.RegexSelector;
import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * @author code4crafter@gmail.com <br>
 */
public class RegexSelectorTest {

    @Test(expected = IllegalArgumentException.class)
    public void testRegexWithSingleLeftBracket() {
        String regex = "\\d+(";
        new com.noob.spider.core.selector.RegexSelector(regex);
    }

    @Test
    public void testRegexWithLeftBracketQuoted() {
        String regex = "\\(.+";
        String source = "(hello world";
        com.noob.spider.core.selector.RegexSelector regexSelector = new com.noob.spider.core.selector.RegexSelector(regex);
        String select = regexSelector.select(source);
        Assertions.assertThat(select).isEqualTo(source);
    }

    @Test
    public void testRegexWithZeroWidthAssertions() {
        String regex = "^.*(?=\\?)(?!\\?yy)";
        String source = "hello world?xx?yy";
        com.noob.spider.core.selector.RegexSelector regexSelector = new com.noob.spider.core.selector.RegexSelector(regex);
        String select = regexSelector.select(source);
        Assertions.assertThat(select).isEqualTo("hello world");


        regex = "\\d{3}(?!\\d)";
        source = "123456asdf";
        regexSelector = new RegexSelector(regex);
        select = regexSelector.select(source);
        Assertions.assertThat(select).isEqualTo("456");
    }
}
