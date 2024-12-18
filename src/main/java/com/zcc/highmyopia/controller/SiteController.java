package com.zcc.highmyopia.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.po.Site;
import com.zcc.highmyopia.service.ISiteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @Author zcc
 * @Date 2024/12/18
 * @Description
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/${app.config.api-version}/site")
public class SiteController {
    
    private final ISiteService SiteService;

    // 添加用户
    @PostMapping("/add")
    @RequiresAuthentication
    public Result addSite(@Validated @RequestBody Site site) {
        SiteService.save(site);
        return Result.succ(null);
    }

    // 编辑用户
    @PostMapping("/edit")
    @RequiresAuthentication
    public Result editSite(@RequestBody Site site) {
        LambdaUpdateWrapper<Site> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Site::getId, site.getId());
        wrapper.set(Site::getSiteName, site.getSiteName());
        wrapper.set(Site::getUpdateTime, LocalDateTime.now());
        SiteService.update(wrapper);
        return Result.succ(null);
    }

    // 失效某用户
    @GetMapping("/invalid/{siteId}")
    @RequiresAuthentication
    public Result invalidSite(@PathVariable(name = "siteId") Long siteId) {
        LambdaUpdateWrapper<Site> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Site::getId, siteId);
        wrapper.set(Site::getStatus, 0);
        wrapper.set(Site::getUpdateTime, LocalDateTime.now());
        SiteService.update(wrapper);
        return Result.succ(null);
    }

    // 查找
    @GetMapping("/find/{siteId}")
    @RequiresAuthentication
    public Result FindSite(@PathVariable(name = "siteId") Long siteId) {
        LambdaQueryWrapper<Site> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Site::getId, siteId);
        Site one = SiteService.getOne(wrapper);
        return Result.succ(one);
    }


    // 分页查询
    @GetMapping("page")
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