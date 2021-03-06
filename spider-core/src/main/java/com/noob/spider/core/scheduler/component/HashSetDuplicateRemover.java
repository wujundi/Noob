package com.noob.spider.core.scheduler.component;

import com.noob.spider.core.Request;
import com.noob.spider.core.Task;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author code4crafer@gmail.com
 */
public class HashSetDuplicateRemover implements DuplicateRemover {

    private Set<String> urls = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

    @Override
    public boolean isDuplicate(Request request, Task task) {
        return !urls.add(getUrl(request));
    }

    protected String getUrl(Request request) {
        return request.getUrl();
    }

    @Override
    public void resetDuplicateCheck(Task task) {
        urls.clear();
    }

    @Override
    public int getTotalRequestsCount(Task task) {
        return urls.size();
    }
}

// 通过 将 ConcurrentHashMap 改造成 HashSet 的方式，最终通过 HashSet 来实现去重
// HashSetDuplicateRemover 只是作为一个工具，在 DuplicateRemovedScheduler 中协助其在 push 阶段，实现get请求的url去重