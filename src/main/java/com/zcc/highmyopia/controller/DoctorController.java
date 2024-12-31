package com.zcc.highmyopia.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcc.highmyopia.common.Constants;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.common.lang.ResultCode;
import com.zcc.highmyopia.po.Dept;
import com.zcc.highmyopia.po.Doctor;
import com.zcc.highmyopia.po.User;
import com.zcc.highmyopia.service.IDoctorService;
import com.zcc.highmyopia.service.IRedisService;
import com.zcc.highmyopia.service.IUserService;
import com.zcc.highmyopia.shiro.AccountProfile;
import com.zcc.highmyopia.util.JwtUtils;
import com.zcc.highmyopia.util.SaltUtil;
import com.zcc.highmyopia.util.ShiroUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;
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
@Api(tags = "医生管理")
@RequestMapping("/api/${app.config.api-version}/doctor")
public class DoctorController {


    private final IDoctorService doctorService;

    private final IRedisService redisService;

//    @PostMapping("/add")
//    @ApiOperation(value = "添加医生")
//    @RequiresAuthentication
//    public Result addDoctor(@Validated @RequestBody Doctor doctor) {
//        String cacheKey = Constants.RedisKey.DOCTOR + doctor.getId();
//        doctor.setCreateTime(LocalDateTime.now());
//        doctor.setUpdateTime(LocalDateTime.now());
//        doctor.setStatus(1);
//        boolean save = doctorService.save(doctor);
//        if (save)
//            redisService.setValue(cacheKey, doctor);
//        return Result.succ(null);
//    }

//    @PostMapping("/edit")
//    @ApiOperation(value = "编辑医生")
//    @RequiresAuthentication
//    public Result editDoctor(@RequestBody Doctor doctor) {
//        String cacheKey = Constants.RedisKey.DOCTOR + doctor.getId();
//        LambdaUpdateWrapper<Doctor> wrapper = new LambdaUpdateWrapper<>();
//        wrapper.eq(Doctor::getId, doctor.getId());
//        wrapper.set(Doctor::getDoctorName, doctor.getDoctorName());
//        wrapper.set(Doctor::getUpdateTime, LocalDateTime.now());
//        boolean update = doctorService.update(wrapper);
//        if (update)
//            redisService.remove(cacheKey);
//        return Result.succ(null);
//    }

//    @GetMapping("/invalid/{doctorId}")
//    @ApiOperation(value = "删除失效医生")
//    @RequiresAuthentication
//    public Result invalidDoctor(@PathVariable(name = "doctorId") Long doctorId) {
//        String cacheKey = Constants.RedisKey.DOCTOR + doctorId;
//        LambdaUpdateWrapper<Doctor> wrapper = new LambdaUpdateWrapper<>();
//        wrapper.eq(Doctor::getId, doctorId);
//        wrapper.set(Doctor::getStatus, 0);
//        wrapper.set(Doctor::getUpdateTime, LocalDateTime.now());
//        boolean update = doctorService.update(wrapper);
//        if (update)
//            redisService.remove(cacheKey);
//        return Result.succ(null);
//    }

    @GetMapping("/find/{doctorId}")
    @ApiOperation(value = "查找医生")
    @RequiresAuthentication
    public Result FindDoctor(@PathVariable(name = "doctorId") Long doctorId) {
        String cacheKey = Constants.RedisKey.DOCTOR + doctorId;
        Doctor doctor = redisService.getValue(cacheKey);
        if (doctor != null) return Result.succ(doctor);
        LambdaQueryWrapper<Doctor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Doctor::getId, doctorId);
        doctor = doctorService.getOne(wrapper);
        redisService.setValue(cacheKey, doctor);
        return Result.succ(doctor);
    }


    @GetMapping("page")
    @ApiOperation(value = "分页查询医生")
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