package com.noob.spider.extension.utils;

import org.junit.Test;

/**
 * @author code4crafer@gmail.com
 */
public class IPUtilsTest {

    @Test
    public void testGetFirstNoLoopbackIPAddresses() throws Exception {
        System.out.println(IPUtils.getFirstNoLoopbackIPAddresses());
    }
}
