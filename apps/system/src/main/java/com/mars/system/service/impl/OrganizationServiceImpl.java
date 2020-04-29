package com.mars.system.service.impl;

import com.mars.system.dao.OrganizationMapper;
import com.mars.system.entity.OrganizationEntity;
import com.mars.system.service.OrganizationService;
import org.springframework.stereotype.Service;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    private OrganizationMapper organizationMapper;

    public OrganizationServiceImpl(OrganizationMapper organizationMapper) {
        this.organizationMapper = organizationMapper;
    }

    @Override
    public OrganizationEntity queryById(String id) {
        return organizationMapper.selectById(id);
    }

    @Override
    public void updateOrg(OrganizationEntity entity) {
        organizationMapper.updateById(entity);
    }
}
