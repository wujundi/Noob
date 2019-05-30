package com.noob.spider.proxy;

import org.junit.Test;
import com.noob.spider.Site;
import com.noob.spider.Task;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author code4crafter@gmail.com
 *         Date: 17/4/16
 *         Time: 上午10:29
 */
public class SimpleProxyProviderTest {

    public static final Task TASK = Site.me().toTask();

    @Test
    public void test_get_proxy() throws Exception {
        Proxy originProxy1 = new Proxy("127.0.0.1", 1087);
        Proxy originProxy2 = new Proxy("127.0.0.1", 1088);
        SimpleProxyProvider proxyProvider = SimpleProxyProvider.from(originProxy1, originProxy2);
        Proxy proxy = proxyProvider.getProxy(TASK);
        assertThat(proxy).isEqualTo(originProxy1);
        proxy = proxyProvider.getProxy(TASK);
        assertThat(proxy).isEqualTo(originProxy2);
        proxy = proxyProvider.getProxy(TASK);
        assertThat(proxy).isEqualTo(originProxy1);
    }
}
