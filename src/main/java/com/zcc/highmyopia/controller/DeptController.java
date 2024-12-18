package com.zcc.highmyopia.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.po.Dept;
import com.zcc.highmyopia.service.IDeptService;
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
@RestController
@Api(tags = "科室管理")
@RequestMapping("/api/${app.config.api-version}/dept")
public class DeptController {

    private final IDeptService deptService;

    @PostMapping("/add")
    @ApiOperation(value = "添加科室")
    @RequiresAuthentication
    public Result addDept(@Validated @RequestBody Dept dept) {
        dept.setCreateTime(new Date());
        dept.setUpdateTime(new Date());
        deptService.save(dept);
        return Result.succ(null);
    }

    @PostMapping("/edit")
    @ApiOperation(value = "编辑科室")
    @RequiresAuthentication
    public Result editDept(@RequestBody Dept dept) {
        LambdaUpdateWrapper<Dept> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Dept::getId, dept.getId());
        wrapper.set(Dept::getDeptName, dept.getDeptName());
        wrapper.set(Dept::getUpdateTime, LocalDateTime.now());
        deptService.update(wrapper);
        return Result.succ(null);
    }

    @GetMapping("/invalid/{deptId}")
    @ApiOperation(value = "失效科室")
    @RequiresAuthentication
    public Result invalidDept(@PathVariable(name = "deptId") Long deptId) {
        LambdaUpdateWrapper<Dept> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Dept::getId, deptId);
        wrapper.set(Dept::getStatus, 0);
        wrapper.set(Dept::getUpdateTime, LocalDateTime.now());
        deptService.update(wrapper);
        return Result.succ(null);
    }

    @GetMapping("/find/{deptId}")
    @ApiOperation(value = "查找科室")
    @RequiresAuthentication
    public Result FindDept(@PathVariable(name = "deptId") Long deptId) {
        LambdaQueryWrapper<Dept> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dept::getId, deptId);
        Dept one = deptService.getOne(wrapper);
        return Result.succ(one);
    }


    @GetMapping("page")
    @ApiOperation(value = "分页查询科室")
    @RequiresAuthentication
    public Result pageDept(@RequestParam(defaultValue = "1") Integer pageNumber,  // 页码默认 0
                             @RequestParam(defaultValue = "10") Integer pageSize) {  // 每页大小默认 10
        IPage<Dept> page = new Page<>();
        page.setPages(pageNumber);
        page.setSize(pageSize);
        IPage<Dept> page1 = deptService.page(page);
        return Result.succ(page1);
    }
}