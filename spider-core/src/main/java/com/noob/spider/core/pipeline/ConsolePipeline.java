package com.noob.spider.core.pipeline;

import com.noob.spider.core.ResultItems;
import com.noob.spider.core.Task;

import java.util.Map;

/**
 * Write results in console.<br>
 * Usually used in extension.
 *
 * @author code4crafter@gmail.com <br>
 * @since 0.1.0
 */
public class ConsolePipeline implements Pipeline {

    @Override
    public void process(ResultItems resultItems, Task task) {
        System.out.println("get page: " + resultItems.getRequest().getUrl());
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            System.out.println(entry.getKey() + ":\t" + entry.getValue());
        }
    }
}
