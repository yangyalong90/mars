package com.mars.system.controller;

import com.mars.common.model.ApiResult;
import com.mars.system.dao.OrganizationMapper;
import com.mars.system.entity.OrganizationEntity;
import com.mars.system.service.OrganizationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sys/org")
public class OrganizationController {

    private OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping("/{orgId}")
    @ResponseBody
    public ApiResult queryOrg(@PathVariable("orgId") String orgId){
        return ApiResult.success(organizationService.queryById(orgId));
    }

    @PutMapping("/{orgId}")
    @ResponseBody
    public ApiResult updateOrg(@PathVariable("orgId") String orgId, @RequestBody OrganizationEntity entity){

        entity.setId(orgId);
        organizationService.updateOrg(entity);
        return ApiResult.success();

    }

}
