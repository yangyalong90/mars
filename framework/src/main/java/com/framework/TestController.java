package com.framework;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

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

    @RequiresPermissions("api:abc")
    @GetMapping("/api/abc")
    @ResponseBody
    public String a(){

//        Subject subject = SecurityUtils.getSubject();
//        subject.checkPermission("api:abc");

        return "555";

    }

    @RequiresPermissions("api:abcd")
    @GetMapping("/api/abcd")
    @ResponseBody
    public String ad(){

//        Subject subject = SecurityUtils.getSubject();
//        subject.checkPermission("api:abc");

        return "66";

    }

}
