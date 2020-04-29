package com.mars.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("organization")
@Data
public class OrganizationEntity {

    private String id;
    private String name;

}
