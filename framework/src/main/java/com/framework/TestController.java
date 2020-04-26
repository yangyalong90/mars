package com.framework;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.xml.ws.soap.Addressing;
import java.util.concurrent.TimeUnit;

@Controller
public class TestController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

//    @GetMapping("/login")
//    @ResponseBody
//    public String login(User user){
//
//        Subject subject = SecurityUtils.getSubject();
//
//        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.username, user.password);
//
//
//        subject.login(usernamePasswordToken);
//
//        return "aaaa";
//
//    }


    @GetMapping("/api/u1")
    @ResponseBody
    public String u1() {
        return "1";
    }

    @RequiresPermissions("api:u2")
    @GetMapping("/api/u2")
    @ResponseBody
    public String u2() {
        return "2";
    }

    @RequiresPermissions("api:u3")
    @GetMapping("/api/u3")
    @ResponseBody
    public String u3() {
        return "3";
    }


    @PostMapping("/{key}")
    @ResponseBody
    public void add(@PathVariable("key") String key, @RequestBody User user) {
        kafkaTemplate.send("TOPIC_1", JSON.toJSONString(user));
        redisTemplate.opsForValue().set(key, user);
    }

    @GetMapping("/{key}/")
    @ResponseBody
    public Object get(@PathVariable("key") String key) throws Exception {

        RLock lock = redissonClient.getLock(key + "---lock");

        lock.lock(10, TimeUnit.SECONDS);
        try {
            User u = (User) redisTemplate.opsForValue().get(key);

            u.setCount(u.count + 1);

            redisTemplate.opsForValue().set(key, u);

            return u;
        } finally {
            lock.unlock();
        }

    }

    @KafkaListener(topics = "TOPIC_1")
    public void test(ConsumerRecord<String, String> record) throws Exception {

        System.out.println("111");
        Thread.sleep(1000);
        System.out.println(record.value());

        System.out.println("222");

    }

}
