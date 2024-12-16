package com.zcc.highmyopia.controller.old;



import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.entity.old.Caselist;
import com.zcc.highmyopia.entity.Followup;
import com.zcc.highmyopia.mapper.old.CaselistMapper;
import com.zcc.highmyopia.service.old.CaselistService;
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
@RequestMapping("/caselist")
public class CaselistController {

    @Autowired
    CaselistService caselistService;
    @Autowired
    CaselistMapper caselistMapper;
    @Autowired
    FollowupService followupService;

    @GetMapping("/doPredict")
    @RequiresAuthentication
    public Result doPredictByPath(String path) {
//        @PathVariable(name = "localpath")

        return Result.succ(path);
//        System.out.println("待诊断图片路径为：" + path);
//        return  Result.succ("该图片诊断结论如下：" +
//                "患者眼底豹纹状改变明显，为高度近视 " +
//                " 检测到视盘萎缩斑 见图中绿色区域 " +
//                "<br/> 无明显病理性病变" +
//                "<br/> 综合判定为病理性近视，请安排其尽快复查");
    }

    @GetMapping("/pastCaselist/{id}")
    @RequiresAuthentication
    public Result caselistByPID(@PathVariable(name = "id") Integer id) {
        return Result.succ(caselistMapper.selectPastCaseByPId(id));
    }

    @GetMapping("/pastCaselistByPatientId/{id}")
    @RequiresAuthentication
    public Result caselistByPatientID(@PathVariable(name = "id") String id) {
        return Result.succ(caselistMapper.selectPastCaseByPatientId(id));
    }

    @GetMapping("/pastCase/{id}")
    @RequiresAuthentication
    public Result caseByID(@PathVariable(name = "id") Integer id) {
        return Result.succ(caselistService.getById(id));
    }

    @GetMapping("/pastCaseList")
    @RequiresAuthentication
    public Result pastCaselist() {
        return Result.succ(caselistMapper.selectPastCaselist());
    }

    @GetMapping("/pastCaseListHave/{type}")
    @RequiresAuthentication
    public Result pastCaselistHave(@PathVariable(name = "type") Integer type) {
        if(type==1) {
            return Result.succ(caselistMapper.selectPastCaselistHaveIol());
        } else if(type == 2) {
            return Result.succ(caselistMapper.selectPastCaselistHaveOct());
        } else if(type ==3) {
            return Result.succ(caselistMapper.selectPastCaselistHaveOpt());
        } else  return Result.fail("查询失败");
    }

    @GetMapping("/caseListHave/{type}")
    @RequiresAuthentication
    public Result caselistHave(@PathVariable(name = "type") Integer type) {
        if(type==1) {
            return Result.succ(caselistMapper.selectCaselistHaveIol());
        } else if(type == 2) {
            return Result.succ(caselistMapper.selectCaselistHaveOct());
        } else if(type ==3) {
            return Result.succ(caselistMapper.selectCaselistHaveOpt());
        } else  return Result.fail("查询失败");
    }

    @GetMapping("/caseListRange/{low}/{high}")
    @RequiresAuthentication
    public Result caselistIolrange(@PathVariable double low, @PathVariable double high) {
        List<Caselist> temp = caselistMapper.selectCaselistIOL();
        List<Caselist> res = new ArrayList<>();
        for(Caselist caselist : temp) {
            try{
                double alod = Double.valueOf(caselist.getALOD().split(" ")[0]).doubleValue();
                double alos = Double.valueOf(caselist.getALOS().split(" ")[0]).doubleValue();
                if((alod > low && alod <= high) || (alod > low && alod <= high )) {
                    res.add(caselist);
                }
            } catch (NullPointerException nullPointerException) {

            }

        }
        return Result.succ(res);
    }

    @GetMapping("/pastCaseListRange/{low}/{high}")
    @RequiresAuthentication
    public Result pastCaselistIolrange(@PathVariable double low, @PathVariable double high) {
        List<Caselist> temp = caselistMapper.selectPastCaselistIOL();
        List<Caselist> res = new ArrayList<>();
        for(Caselist caselist : temp) {
            try{
                double alod = Double.valueOf(caselist.getALOD().split(" ")[0]).doubleValue();
                double alos = Double.valueOf(caselist.getALOS().split(" ")[0]).doubleValue();
                if((alod > low && alod <= high) || (alod > low && alod <= high )) {
                    res.add(caselist);
                }
            } catch (NullPointerException nullPointerException) {

            }

        }
        return Result.succ(res);
    }


    @GetMapping("/caseList")
    @RequiresAuthentication
    public Result caselist() {
        return Result.succ(caselistMapper.selectCaselist());
    }

    @PostMapping("/submitCase")
    @RequiresAuthentication
    public Result submitCase(@RequestBody Caselist caselist) {
        if(caselist.getDiagnosis() == null || "".equals(caselist.getDiagnosis())) {
            return Result.fail("完善失败，请填写诊断结果");
        }
        caselist.setVisittime(LocalDate.now());
        caselist.setStat(0);
        caselist.setWanshan(1);
        System.out.println();
        caselistService.saveOrUpdate(caselist);
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
