package com.mars.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mars.framework.datasource.cache.CacheRelations;
import com.mars.framework.datasource.cache.RelativeCache;
import com.mars.system.entity.UserEntity;
import com.mars.system.model.UserInfo;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@CacheNamespace(implementation = RelativeCache.class, eviction = RelativeCache.class, flushInterval = 30 * 60 * 1000)
@CacheRelations(from = OrganizationMapper.class)
public interface UserMapper extends BaseMapper<UserEntity> {

    UserInfo queryUserInfo(@Param("userId") String userId);

}
