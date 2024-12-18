package com.zcc.highmyopia.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.common.lang.ResultCode;
import com.zcc.highmyopia.po.Dept;
import com.zcc.highmyopia.po.Doctor;
import com.zcc.highmyopia.po.User;
import com.zcc.highmyopia.service.IDoctorService;
import com.zcc.highmyopia.service.IUserService;
import com.zcc.highmyopia.shiro.AccountProfile;
import com.zcc.highmyopia.util.JwtUtils;
import com.zcc.highmyopia.util.SaltUtil;
import com.zcc.highmyopia.util.ShiroUtil;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/18
 * @Description
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/${app.config.api-version}/doctor")
public class DoctorController {


    private final IDoctorService doctorService;

    // 添加用户
    @PostMapping("/add")
    @RequiresAuthentication
    public Result addDoctor(@Validated @RequestBody Doctor doctor) {
        doctor.setCreateTime(new Date());
        doctor.setUpdateTime(new Date());
        doctor.setStatus(1);
        doctorService.save(doctor);
        return Result.succ(null);
    }

    // 编辑用户
    @PostMapping("/edit")
    @RequiresAuthentication
    public Result editDoctor(@RequestBody Doctor doctor) {
        LambdaUpdateWrapper<Doctor> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Doctor::getId, doctor.getId());
        wrapper.set(Doctor::getDoctorName, doctor.getDoctorName());
        wrapper.set(Doctor::getUpdateTime, LocalDateTime.now());
        doctorService.update(wrapper);
        return Result.succ(null);
    }

    // 失效某用户
    @GetMapping("/invalid/{doctorId}")
    @RequiresAuthentication
    public Result invalidDoctor(@PathVariable(name = "doctorId") Long doctorId) {
        LambdaUpdateWrapper<Doctor> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Doctor::getId, doctorId);
        wrapper.set(Doctor::getStatus, 0);
        wrapper.set(Doctor::getUpdateTime, LocalDateTime.now());
        doctorService.update(wrapper);
        return Result.succ(null);
    }

    // 查找
    @GetMapping("/find/{doctorId}")
    @RequiresAuthentication
    public Result FindDoctor(@PathVariable(name = "doctorId") Long doctorId) {
        LambdaQueryWrapper<Doctor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Doctor::getId, doctorId);
        Doctor one = doctorService.getOne(wrapper);
        return Result.succ(one);
    }


    // 分页查询
    @GetMapping("page")
    @RequiresAuthentication
    public Result pageDoctor(@RequestParam(defaultValue = "1") Integer pageNumber,  // 页码默认 0
                           @RequestParam(defaultValue = "10") Integer pageSize) {  // 每页大小默认 10
        IPage<Doctor> page = new Page<>();
        page.setPages(pageNumber);
        page.setSize(pageSize);
        IPage<Doctor> page1 = doctorService.page(page);
        return Result.succ(page1);
    }
}