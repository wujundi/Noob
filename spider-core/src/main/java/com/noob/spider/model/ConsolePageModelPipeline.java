package com.noob.spider.model;

import com.noob.spider.pipeline.PageModelPipeline;
import org.apache.commons.lang3.builder.ToStringBuilder;
import com.noob.spider.Task;

/**
 * Print page model in console.<br>
 * Usually used in test.<br>
 * @author code4crafter@gmail.com <br>
 * @since 0.2.0
 */
public class ConsolePageModelPipeline implements PageModelPipeline {
    @Override
    public void process(Object o, Task task) {
        System.out.println(ToStringBuilder.reflectionToString(o));
    }
}
