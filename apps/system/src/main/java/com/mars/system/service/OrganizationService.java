package com.mars.system.service;

import com.mars.system.entity.OrganizationEntity;

public interface OrganizationService {

    OrganizationEntity queryById(String id);

    void updateOrg(OrganizationEntity entity);

}
