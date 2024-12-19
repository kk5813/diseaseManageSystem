package com.zcc.highmyopia.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.po.Site;
import com.zcc.highmyopia.service.ISiteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author zcc
 * @Date 2024/12/18
 * @Description
 */
@Slf4j
@RequiredArgsConstructor
@Api(tags = "网址用户管理")
@RestController
@RequestMapping("/api/${app.config.api-version}/site")
public class SiteController {
    
    private final ISiteService SiteService;

    @PostMapping("/add")
    @ApiOperation(value = "添加用户")
    @RequiresAuthentication
    public Result addSite(@Validated @RequestBody Site site) {
        site.setUpdateTime(LocalDateTime.now());
        site.setCreateTime(LocalDateTime.now());
        SiteService.save(site);
        return Result.succ(null);
    }

    @PostMapping("/edit")
    @ApiOperation(value = "编辑用户")
    @RequiresAuthentication
    public Result editSite(@RequestBody Site site) {
        LambdaUpdateWrapper<Site> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Site::getId, site.getId());
        wrapper.set(Site::getSiteName, site.getSiteName());
        wrapper.set(Site::getUpdateTime, LocalDateTime.now());
        SiteService.update(wrapper);
        return Result.succ(null);
    }

    @GetMapping("/invalid/{siteId}")
    @ApiOperation(value = "失效某用户")
    @RequiresAuthentication
    public Result invalidSite(@PathVariable(name = "siteId") Long siteId) {
        LambdaUpdateWrapper<Site> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Site::getId, siteId);
        wrapper.set(Site::getStatus, 0);
        wrapper.set(Site::getUpdateTime, LocalDateTime.now());
        SiteService.update(wrapper);
        return Result.succ(null);
    }

    @GetMapping("/find/{siteId}")
    @ApiOperation(value = "查找用户")
    @RequiresAuthentication
    public Result FindSite(@PathVariable(name = "siteId") Long siteId) {
        LambdaQueryWrapper<Site> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Site::getId, siteId);
        Site one = SiteService.getOne(wrapper);
        return Result.succ(one);
    }


    @GetMapping("page")
    @ApiOperation(value = "分页查询用户")
    @RequiresAuthentication
    public Result pageSite(@RequestParam(defaultValue = "1") Integer pageNumber,  // 页码默认 0
                             @RequestParam(defaultValue = "10") Integer pageSize) {  // 每页大小默认 10
        IPage<Site> page = new Page<>();
        page.setPages(pageNumber);
        page.setSize(pageSize);
        IPage<Site> page1 = SiteService.page(page);
        return Result.succ(page1);
    }
}