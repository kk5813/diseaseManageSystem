package com.zcc.highmyopia.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcc.highmyopia.common.Constants;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.po.Site;
import com.zcc.highmyopia.service.IRedisService;
import com.zcc.highmyopia.service.ISiteService;
import com.zcc.highmyopia.service.impl.RedissonService;
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
@Api(tags = "眼别管理")
@RestController
@RequestMapping("/api/${app.config.api-version}/site")
public class SiteController {
    
    private final ISiteService SiteService;
    private final IRedisService redisService;

//    @PostMapping("/add")
//    @ApiOperation(value = "添加眼别")
//    @RequiresAuthentication
//    public Result addSite(@Validated @RequestBody Site site) {
//        String cacheKey = Constants.RedisKey.SITE + site.getId();
//        site.setUpdateTime(LocalDateTime.now());
//        site.setCreateTime(LocalDateTime.now());
//        boolean save = SiteService.save(site);
//        if (save)
//            redisService.setValue(cacheKey, site);
//        return Result.succ(null);
//    }
//
//    @PostMapping("/edit")
//    @ApiOperation(value = "编辑眼别")
//    @RequiresAuthentication
//    public Result editSite(@RequestBody Site site) {
//        String cacheKey = Constants.RedisKey.SITE + site.getId();
//        LambdaUpdateWrapper<Site> wrapper = new LambdaUpdateWrapper<>();
//        wrapper.eq(Site::getId, site.getId());
//        wrapper.set(Site::getSiteName, site.getSiteName());
//        wrapper.set(Site::getUpdateTime, LocalDateTime.now());
//        boolean update = SiteService.update(wrapper);
//        if (update)
//            redisService.remove(cacheKey);
//        return Result.succ(null);
//    }
//
//    @GetMapping("/invalid/{siteId}")
//    @ApiOperation(value = "失效眼别")
//    @RequiresAuthentication
//    public Result invalidSite(@PathVariable(name = "siteId") Long siteId) {
//        String cacheKey = Constants.RedisKey.SITE + siteId;
//        LambdaUpdateWrapper<Site> wrapper = new LambdaUpdateWrapper<>();
//        wrapper.eq(Site::getId, siteId);
//        wrapper.set(Site::getStatus, 0);
//        wrapper.set(Site::getUpdateTime, LocalDateTime.now());
//        boolean update = SiteService.update(wrapper);
//        if (update)
//            redisService.remove(cacheKey);
//        return Result.succ(null);
//    }

    @GetMapping("/find/{siteId}")
    @ApiOperation(value = "查找眼别")
    @RequiresAuthentication
    public Result FindSite(@PathVariable(name = "siteId") Long siteId) {
        String cacheKey = Constants.RedisKey.SITE + siteId;
        Site site = redisService.getValue(cacheKey);
        if (site != null) return Result.succ(site);
        LambdaQueryWrapper<Site> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Site::getId, siteId);
        site = SiteService.getOne(wrapper);
        redisService.setValue(cacheKey, site);
        return Result.succ(site);
    }


    @GetMapping("page")
    @ApiOperation(value = "分页查询眼别")
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