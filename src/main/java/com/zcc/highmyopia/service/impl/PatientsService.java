package com.zcc.highmyopia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcc.highmyopia.common.Constants;
import com.zcc.highmyopia.common.dto.ElementShowDTO;
import com.zcc.highmyopia.common.dto.PatientsDTO;
import com.zcc.highmyopia.hospital.entity.ElementEntity;
import com.zcc.highmyopia.hospital.entity.ElementVisionEntity;
import com.zcc.highmyopia.hospital.entity.VisitEntity;
import com.zcc.highmyopia.po.*;
import com.zcc.highmyopia.mapper.IPatientsMapper;
import com.zcc.highmyopia.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liangyue
 * @since 2021-02-05
 */
@Service
public class PatientsService extends ServiceImpl<IPatientsMapper, Patients> implements IPatientsService {

    @Autowired
    IPatientsMapper patientMapper;

    @Resource
    private IRedisService redisService;

    @Resource
    private IElementService elementService;
    @Resource
    private IElementVisionService elementVisionService;
    @Resource
    private IVisitsService visitsService;
    @Resource
    private IDeptService deptService;
    @Resource
    private ISiteService siteService;
    @Resource
    private IDoctorService doctorService;


    @Override
    public List<Patients> pageQuery(Integer pageNumber, Integer pageSize) {
        Page<Patients> page = new Page<>(pageNumber, pageSize);
        IPage<Patients> pages = this.page(page);
        return pages.getRecords();
    }

    @Override
    public List<Patients> searchPatients(PatientsDTO patientsDTO) {
        return patientMapper.searchPatients(patientsDTO);
    }

    @Override
    public Patients getPatientById(Long patientId) {
        String cacheKey = Constants.RedisKey.PATIENTS + patientId;
        Patients patients = redisService.getValue(cacheKey);
        if (patients != null) return patients;

        patients = patientMapper.selectById(patientId);
        if (patients == null) return new Patients();
        redisService.setValue(cacheKey, patients);
        return patients;
    }

    //拼接visits， element, elementVision
    @Override
    public  List<ElementShowDTO> timeLineElement(Long patientId) {
        // 病历element
        List<Element> elementList = elementService.list(new LambdaQueryWrapper<Element>()
                .eq(Element::getPatientId, patientId));
        List<ElementEntity> elementEntities = elementList.stream()
                .map(element -> {
                    String patientName = getPatientById(element.getPatientId()).getName();
                    return ElementEntity.poToEntity(element,patientName);})
                .collect(Collectors.toList());
        // visits就诊信息
        List<Visits> visitsList = visitsService.list(new LambdaQueryWrapper<Visits>()
                .eq(Visits::getPatientId, patientId));
        List<VisitEntity> visitEntities = visitsList.stream()
                .map( visit -> {
                    Patients patient = getPatientById(visit.getPatientId());
                    Dept dept = deptService.getDeptById(visit.getDeptId());
                    Doctor doctor = doctorService.getDoctorById(visit.getDoctorId());
                    Site site = siteService.getSiteById(visit.getSiteId());
                    return VisitEntity.poToEntity(visit, patient, doctor, dept, site);
                })
                .collect(Collectors.toList());

        List<ElementVision> elementVisions = elementVisionService.list(new LambdaQueryWrapper<ElementVision>()
                .eq(ElementVision::getPatientId, patientId));
        List<ElementVisionEntity> elementVisionEntities =  elementVisions.stream()
                .map(elementVision -> {
                    String patientName = getPatientById(Long.valueOf(elementVision.getPatientId())).getName();
                    return ElementVisionEntity.poToEntity(elementVision, patientName);
                })
                .collect(Collectors.toList());

        // 获取 elementEntities 列表并将其按照 visitNumber 分组
        Map<String, ElementEntity> elementMap = elementEntities.stream()
                .collect(Collectors.toMap(ElementEntity::getVisitNumber, elementEntity -> elementEntity));

        // 获取 visitEntities 列表并将其按照 visitNumber 分组
        Map<String, VisitEntity> visitMap = visitEntities.stream()
                .collect(Collectors.toMap(VisitEntity::getVisitNumber, visitEntity -> visitEntity));

        // 获取 elementVisionEntities 列表并将其按照 visitNumber 分组
        Map<String, ElementVisionEntity> elementVisionMap = elementVisionEntities.stream()
                .collect(Collectors.toMap(ElementVisionEntity::getVisitNumber, elementVisionEntity -> elementVisionEntity));

        List<ElementShowDTO> timeLineElement = new ArrayList<>();

        for (String visitNumber : visitMap.keySet()) {
            VisitEntity visitEntity = visitMap.get(visitNumber);
            ElementEntity elementEntity = elementMap.get(visitNumber);
            ElementVisionEntity elementVisionEntity = elementVisionMap.get(visitNumber);

            // 创建 ElementShowDTO 实体，并将相关数据填充进去
            ElementShowDTO elementShowDTO = new ElementShowDTO();

            if (visitEntity != null) {
                elementShowDTO.setVisitEntity(visitEntity);
            }
            if (elementEntity != null) {
                elementShowDTO.setElementEntity(elementEntity);
            }
            if (elementVisionEntity != null) {
                elementShowDTO.setElementVisionEntity(elementVisionEntity);
            }

            timeLineElement.add(elementShowDTO);
        }

        // 返回结果
        return timeLineElement;
    }
}
