package com.mars.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("test1")
public class TestEntity {

    private int id;
    private int count;

}
