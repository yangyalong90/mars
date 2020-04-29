package com.mars.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mars.framework.datasource.cache.RelativeCache;
import com.mars.system.entity.OrganizationEntity;
import org.apache.ibatis.annotations.CacheNamespace;
import org.springframework.stereotype.Repository;

@Repository
@CacheNamespace(implementation = RelativeCache.class, eviction = RelativeCache.class, flushInterval = 30 * 60 * 1000)
public interface OrganizationMapper extends BaseMapper<OrganizationEntity> {
}
