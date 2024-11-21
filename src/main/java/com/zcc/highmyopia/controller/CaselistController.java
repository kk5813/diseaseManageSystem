package com.zcc.highmyopia.controller;

import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.entity.Caselist;
import com.zcc.highmyopia.entity.Followup;
import com.zcc.highmyopia.mapper.CaselistMapper;
import com.zcc.highmyopia.service.CaselistService;
import com.zcc.highmyopia.service.FollowupService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liangyue
 * @since 2021-02-08
 */
@RestController
@RequestMapping("/caselist")  // 设置请求的基础路径
public class CaselistController {

    @Autowired
    CaselistService caselistService;  // 病例服务

    @Autowired
    CaselistMapper caselistMapper;  // 病例映射器

    @Autowired
    FollowupService followupService;  // 复查服务

    /**
     * 进行预测的接口
     * @param path 图片路径
     * @return 预测结果
     */
    @GetMapping("/doPredict")
    @RequiresAuthentication
    public Result doPredictByPath(String path) {
        return Result.succ(path);
    }

    /**
     * 根据患者ID获取过去的病例列表
     * @param id 患者ID
     * @return 过去的病例列表
     */
    @GetMapping("/pastCaselist/{id}")
    @RequiresAuthentication
    public Result caselistByPID(@PathVariable(name = "id") Integer id) {
        return Result.succ(caselistMapper.selectPastCaseByPId(id));
    }

    /**
     * 根据患者ID获取病例
     * @param id 患者ID
     * @return 患者的病例
     */
    @GetMapping("/pastCaselistByPatientId/{id}")
    @RequiresAuthentication
    public Result caselistByPatientID(@PathVariable(name = "id") String id) {
        return Result.succ(caselistMapper.selectPastCaseByPatientId(id));
    }

    /**
     * 根据病例ID获取具体病例
     * @param id 病例ID
     * @return 病例信息
     */
    @GetMapping("/pastCase/{id}")
    @RequiresAuthentication
    public Result caseByID(@PathVariable(name = "id") Integer id) {
        return Result.succ(caselistService.getById(id));
    }

    /**
     * 获取过去的病例列表
     * @return 过去的病例列表
     */
    @GetMapping("/pastCaseList")
    @RequiresAuthentication
    public Result pastCaselist() {
        return Result.succ(caselistMapper.selectPastCaselist());
    }

    /**
     * 根据类型获取过去的病例列表
     * @param type 类型
     * @return 符合条件的病例列表
     */
    @GetMapping("/pastCaseListHave/{type}")
    @RequiresAuthentication
    public Result pastCaselistHave(@PathVariable(name = "type") Integer type) {
        if(type == 1) {
            return Result.succ(caselistMapper.selectPastCaselistHaveIol());
        } else if(type == 2) {
            return Result.succ(caselistMapper.selectPastCaselistHaveOct());
        } else if(type == 3) {
            return Result.succ(caselistMapper.selectPastCaselistHaveOpt());
        } else {
            return Result.fail("查询失败");
        }
    }

    /**
     * 根据类型获取当前病例列表
     * @param type 类型
     * @return 符合条件的病例列表
     */
    @GetMapping("/caseListHave/{type}")
    @RequiresAuthentication
    public Result caselistHave(@PathVariable(name = "type") Integer type) {
        if(type == 1) {
            return Result.succ(caselistMapper.selectCaselistHaveIol());
        } else if(type == 2) {
            return Result.succ(caselistMapper.selectCaselistHaveOct());
        } else if(type == 3) {
            return Result.succ(caselistMapper.selectCaselistHaveOpt());
        } else {
            return Result.fail("查询失败");
        }
    }

    /**
     * 根据范围查询病例
     * @param low 范围下限
     * @param high 范围上限
     * @return 符合条件的病例列表
     */
    @GetMapping("/caseListRange/{low}/{high}")
    @RequiresAuthentication
    public Result caselistIolrange(@PathVariable double low, @PathVariable double high) {
        List<Caselist> temp = caselistMapper.selectCaselistIOL();
        List<Caselist> res = new ArrayList<>();
        for(Caselist caselist : temp) {
            try {
                double alod = Double.valueOf(caselist.getALOD().split(" ")[0]);
                double alos = Double.valueOf(caselist.getALOS().split(" ")[0]);
                if((alod > low && alod <= high) || (alos > low && alos <= high)) {
                    res.add(caselist);
                }
            } catch (NullPointerException e) {
                // 处理空值的异常，继续下一个
            }
        }
        return Result.succ(res);
    }

    /**
     * 根据范围查询过去的病例
     * @param low 范围下限
     * @param high 范围上限
     * @return 符合条件的过去病例列表
     */
    @GetMapping("/pastCaseListRange/{low}/{high}")
    @RequiresAuthentication
    public Result pastCaselistIolrange(@PathVariable double low, @PathVariable double high) {
        List<Caselist> temp = caselistMapper.selectPastCaselistIOL();
        List<Caselist> res = new ArrayList<>();
        for(Caselist caselist : temp) {
            try {
                double alod = Double.valueOf(caselist.getALOD().split(" ")[0]);
                double alos = Double.valueOf(caselist.getALOS().split(" ")[0]);
                if((alod > low && alod <= high) || (alos > low && alos <= high)) {
                    res.add(caselist);
                }
            } catch (NullPointerException e) {
                // 处理空值的异常，继续下一个
            }
        }
        return Result.succ(res);
    }

    /**
     * 获取当前病例列表
     * @return 当前病例列表
     */
    @GetMapping("/caseList")
    @RequiresAuthentication
    public Result caselist() {
        return Result.succ(caselistMapper.selectCaselist());
    }

    /**
     * 提交病例信息
     * @param caselist 病例对象
     * @return 提交结果
     */
    @PostMapping("/submitCase")
    @RequiresAuthentication
    public Result submitCase(@RequestBody Caselist caselist) {
        // 检查诊断结果是否为空
        if(caselist.getDiagnosis() == null || "".equals(caselist.getDiagnosis())) {
            return Result.fail("完善失败，请填写诊断结果");
        }
        caselist.setVisittime(LocalDate.now());  // 设置就诊时间
        caselist.setStat(0);  // 设置状态
        caselist.setWanshan(1);  // 设置完善状态

        // 保存病例信息
        caselistService.saveOrUpdate(caselist);

        // 创建跟进对象并保存
        Followup temp = new Followup();
        LocalDateTime localDateTime = LocalDateTime.now();
        if(caselist.getPlan() != null) {
            temp.setPlanVisitDate(localDateTime.plusDays(caselist.getPlan()));
        }

        temp.setCaseId(caselist.getId());
        temp.setPatientId(caselist.getPatientId());
        followupService.saveOrUpdate(temp);

        return Result.succ("完善成功");
    }
}
