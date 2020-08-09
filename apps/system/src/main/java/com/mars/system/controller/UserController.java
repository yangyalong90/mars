package com.mars.system.controller;

import com.mars.common.model.ApiResult;
import com.mars.framework.User;
import com.mars.system.MyQueueLock;
import com.mars.system.dao.TestMapper;
import com.mars.system.dao.UserMapper;
import com.mars.system.entity.TestEntity;
import com.mars.system.entity.UserEntity;
import com.mars.system.service.UserService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sys/user")
public class UserController {

    private volatile UserService userService;
    private TestMapper testMapper;
    private KafkaTemplate<String, String> kafkaTemplate;

    public UserController(UserService userService, TestMapper testMapper, KafkaTemplate<String, String> kafkaTemplate) {
        this.userService = userService;
        this.testMapper = testMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @RequiresPermissions("sys:user:query")
    @GetMapping("/{userId}")
    public ApiResult queryUser(@PathVariable("userId") String userId) {
        return ApiResult.success(userService.queryUser(userId));
    }

    @RequiresPermissions("sys:user:update")
    @PutMapping("/{userId}")
    public ApiResult updateUser(@PathVariable("userId") String userId, @RequestBody UserEntity userEntity) {
        userEntity.setId(userId);
        userService.updateUser(userEntity);
        return ApiResult.success();
    }

    @GetMapping("/demo")
    @ResponseBody
    public ApiResult demo() {
        return ApiResult.success("this system");
    }

    @GetMapping("/batch")
    @ResponseBody
    public ApiResult batch() {
        MyQueueLock lock = new MyQueueLock();


        for (int i = 0; i < 20; i++){
            new Thread(() -> {
                lock.lock();
                int count = testMapper.selectById(1).getCount();
                if (count > 0) {
                    lock.lock();
                    count = count - 1;
                    TestEntity entity = new TestEntity();
                    entity.setId(1);
                    entity.setCount(count);
                    testMapper.updateById(entity);
                    System.out.println(Thread.currentThread().getName() + " -- 下单成功，当前库存 --- " + count);
                    lock.unlock();
                }else {
                    System.out.println(Thread.currentThread().getName() + " -- 下单失败");
                }
                lock.unlock();
            }, i + "").start();
        }

        return ApiResult.success();
    }

    @KafkaListener(topics = "TOPIC_TEST")
    public void test(ConsumerRecord<String, String> record, Acknowledgment acknowledgment){
        System.out.println(Thread.currentThread().getId() + " --- " + record.value() + " --- " + record.partition());
        acknowledgment.acknowledge();
    }

}
