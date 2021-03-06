package com.noob.manage.dao;

import com.google.gson.Gson;
import com.noob.manage.utils.StaticValue;
import com.noob.spider.core.ResultItems;
import com.noob.spider.core.Task;
import com.noob.spider.core.pipeline.Pipeline;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

/**
 * Created by gsh199449 on 2016/10/24.
 */
@Component
public class CommonWebpageRedisPipeline implements Pipeline {
    private static Jedis jedis;
    private final boolean needRedis;
    private final String publishChannelName;
    private final Gson gson = new Gson();
    private Logger LOG = LogManager.getLogger(CommonWebpageRedisPipeline.class);

    @Autowired
    public CommonWebpageRedisPipeline(StaticValue staticValue) {
        this.needRedis = staticValue.isNeedRedis();
        this.publishChannelName = staticValue.getWebpageRedisPublishChannelName();
        if (this.needRedis) {
            LOG.info("正在初始化Redis客户端,Host:{},Port:{}", staticValue.getRedisHost(), staticValue.getRedisPort());
            jedis = new Jedis(staticValue.getRedisHost(), staticValue.getRedisPort());
            LOG.info("Jedis初始化成功,Clients List:{}", jedis.clientList());
        } else {
            LOG.warn("未初始化Redis客户端");
        }
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        if (!needRedis) { return; }
        // 将 redis 当做消息队列来用，将抓取结果发布到对应的“频道”（Channel，类比kafka的Topic)
        long receivedClientsCount = jedis.publish(publishChannelName, gson.toJson(resultItems.getAll()));
    }
}

// 2019-07-23 20:36