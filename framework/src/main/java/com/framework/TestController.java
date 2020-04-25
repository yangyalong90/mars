package com.framework;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    //    @RequiresPermissions("api:abc")
    @GetMapping("/api/abc")
    @ResponseBody
    public String a() {

//        Subject subject = SecurityUtils.getSubject();
//        subject.checkPermission("api:abc");

        return "555";

    }

    @RequiresPermissions("api:abcd")
    @GetMapping("/api/abcd")
    @ResponseBody
    public String ad() {

//        Subject subject = SecurityUtils.getSubject();
//        subject.checkPermission("api:abc");

        return "66";

    }

    @PostMapping("/{key}")
    @ResponseBody
    public void add(@PathVariable("key") String key, @RequestBody User user) {
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

}
