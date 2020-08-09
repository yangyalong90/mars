package com.mars.framework.kafka;

import com.mars.framework.threads.WeakRefHashLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

@Component
@Slf4j
public class KafkaListenerDemo {

    // 消费到的数据缓存
    private Map<String, String> UPDATE_DATA_MAP = new ConcurrentHashMap<>();
    // 数据存储
    private Map<String, String> DATA_MAP = new ConcurrentHashMap<>();
    private WeakRefHashLock weakRefHashLock;

    public KafkaListenerDemo(WeakRefHashLock weakRefHashLock) {
        this.weakRefHashLock = weakRefHashLock;
    }

//    @KafkaListener(topics = "TOPIC_INSERT", topicPattern = "0")
    public void insert(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) throws InterruptedException{
        // 模拟顺序异常，也就是insert后消费，这里线程sleep
        Thread.sleep(1000);

        String id = record.value();
        log.info("接收到insert ：： {}", id);
        Lock lock = weakRefHashLock.lock(id);
        lock.lock();
        try {
            log.info("开始处理 {} 的insert", id);
            // 模拟 insert 业务处理
            Thread.sleep(1000);
            // 从缓存中获取 是否存在有update数据
            if (UPDATE_DATA_MAP.containsKey(id)){
                // 缓存数据存在，执行update
                doUpdate(id);
            }
            log.info("处理 {} 的insert 结束", id);
        }finally {
            lock.unlock();
        }
        acknowledgment.acknowledge();
    }

//    @KafkaListener(topics = "TOPIC_UPDATE")
    public void update(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) throws InterruptedException{

        String id = record.value();
        log.info("接收到update ：： {}", id);
        Lock lock = weakRefHashLock.lock(id);
        lock.lock();
        try {
            // 测试使用，不做数据库的校验
            if (!DATA_MAP.containsKey(id)){
                // 未找到对应数据，证明消费顺序异常，将当前数据加入缓存
                log.info("消费顺序异常，将update数据 {} 加入缓存", id);
                UPDATE_DATA_MAP.put(id, id);
            }else {
                doUpdate(id);
            }
        }finally {
            lock.unlock();
        }
        acknowledgment.acknowledge();
    }

    void doUpdate(String id) throws InterruptedException{
        // 模拟 update
        log.info("开始处理update：：{}", id);
        Thread.sleep(1000);
        log.info("处理update：：{} 结束", id);
    }

}
