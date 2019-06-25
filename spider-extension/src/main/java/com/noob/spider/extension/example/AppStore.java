package com.noob.spider.extension.example;

import com.noob.spider.core.Site;
import com.noob.spider.extension.model.OOSpider;
import com.noob.spider.extension.model.annotation.ExtractBy;
import com.noob.spider.core.utils.Experimental;

import java.util.List;

/**
 * @author code4crafter@gmail.com
 * @since 0.4.1
 */
@Experimental
public class AppStore {

    @ExtractBy(type = ExtractBy.Type.JsonPath, value = "$..trackName")
    private String trackName;

    @ExtractBy(type = ExtractBy.Type.JsonPath, value = "$..description")
    private String description;

    @ExtractBy(type = ExtractBy.Type.JsonPath, value = "$..userRatingCount")
    private int userRatingCount;

    @ExtractBy(type = ExtractBy.Type.JsonPath, value = "$..screenshotUrls")
    private List<String> screenshotUrls;

    @ExtractBy(type = ExtractBy.Type.JsonPath, value = "$..supportedDevices")
    private List<String> supportedDevices;

    public static void main(String[] args) {
        AppStore appStore = OOSpider.create(Site.me(), AppStore.class).<AppStore>get("http://itunes.apple.com/lookup?id=653350791&country=cn&entity=software");
        System.out.println(appStore.trackName);
        System.out.println(appStore.description);
        System.out.println(appStore.userRatingCount);
        System.out.println(appStore.screenshotUrls);
        System.out.println(appStore.supportedDevices);
    }
}
