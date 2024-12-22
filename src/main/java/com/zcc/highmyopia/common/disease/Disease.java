package com.zcc.highmyopia.common.disease;

import lombok.*;

/**
 * @Author zcc
 * @Date 2024/12/22
 * @Description
 */
@Getter
@AllArgsConstructor
public enum Disease {

    // 糖尿病性视网膜病变组
    Diabetes("糖尿病", "E14.900X001", DiseaseGroup.DIABETES_RETINOPATHY),
    TypeOneDiabetes("1型糖尿病", "E10.900", DiseaseGroup.DIABETES_RETINOPATHY),
    TypeTwoDiabetes("2型糖尿病", "E11.900", DiseaseGroup.DIABETES_RETINOPATHY),
    DiabeticRetinopathy("糖尿病性视网膜病变", "E14.300x071+H36.0", DiseaseGroup.DIABETES_RETINOPATHY),
    TypeOneDiabeticRetinopathy("1型糖尿病性视网膜病变", "E10.301+H36.0*", DiseaseGroup.DIABETES_RETINOPATHY),
    TypeTwoDiabeticRetinopathy("2型糖尿病性视网膜病变", "E11.301+H36.0*", DiseaseGroup.DIABETES_RETINOPATHY),

    // 视网膜静脉阻塞组
    RetinalVeinOcclusion("视网膜静脉阻塞", "H34.802", DiseaseGroup.RETINAL_VEIN_OCCLUSION),
    RetinalBranchVeinOcclusion("视网膜分支静脉阻塞", "H34.801", DiseaseGroup.RETINAL_VEIN_OCCLUSION),
    CentralRetinalVeinOcclusion("视网膜中央静脉阻塞", "H34.803", DiseaseGroup.RETINAL_VEIN_OCCLUSION),
    PartialRetinalVeinOcclusion("视网膜部分性静脉阻塞", "H34.804", DiseaseGroup.RETINAL_VEIN_OCCLUSION),

    // 高度近视组
    HighMyopia("高度近视", "H52.100x001", DiseaseGroup.HIGH_MYOPIA),
    PathologicMyopia("病理性近视", "H44.203", DiseaseGroup.HIGH_MYOPIA),
    MacularFissure("黄斑劈裂", "H35.320", DiseaseGroup.HIGH_MYOPIA),
    MacularHemorrhage("黄斑出血", "H35.601", DiseaseGroup.HIGH_MYOPIA),
    DegenerativeMyopia("变性近视", "H44.200", DiseaseGroup.HIGH_MYOPIA),
    MacularVitreousTraction("黄斑玻璃体牵拉综合症", "H43.802", DiseaseGroup.HIGH_MYOPIA),
    ChoroidalNeovascularization("脉络膜新生血管", "H35.003", DiseaseGroup.HIGH_MYOPIA),

    // 青光眼组
    SuspectedGlaucoma("可疑青光眼", "H40.000", DiseaseGroup.GLAUCOMA),
    Glaucoma("青光眼", "H40.900", DiseaseGroup.GLAUCOMA),
    PostOperativeGlaucoma("青光眼术后", "Z54.000x.33", DiseaseGroup.GLAUCOMA),
    ClosedAngleGlaucoma("闭角型青光眼", "H40.200x001", DiseaseGroup.GLAUCOMA),
    AcuteClosedAngleGlaucoma("急性闭角型青光眼", "H40.200x002", DiseaseGroup.GLAUCOMA),
    ChronicClosedAngleGlaucoma("慢性闭角型青光眼", "H40.202", DiseaseGroup.GLAUCOMA),
    PrimaryClosedAngleGlaucoma("原发性闭角型青光眼", "H40.200", DiseaseGroup.GLAUCOMA),
    OpenAngleGlaucoma("开角型青光眼", "H40.100x001", DiseaseGroup.GLAUCOMA),
    PrimaryOpenAngleGlaucoma("原发性开角型青光眼", "H40.100", DiseaseGroup.GLAUCOMA),
    CongenitalGlaucoma("先天性青光眼", "Q15.000", DiseaseGroup.GLAUCOMA),
    JuvenileGlaucoma("青少年型青光眼", "Q15.005", DiseaseGroup.GLAUCOMA),
    SteroidInducedGlaucoma("激素性青光眼", "H40.800x003", DiseaseGroup.GLAUCOMA),
    EndStageGlaucoma("绝对期青光眼", "H44.501", DiseaseGroup.GLAUCOMA),
    PigmentaryGlaucoma("色素性青光眼", "H40.101", DiseaseGroup.GLAUCOMA),
    SecondaryGlaucoma("继发性青光眼", "H40.500，H40.500x002", DiseaseGroup.GLAUCOMA),

    // 角膜溃疡组
    BacterialCornealUlcer("细菌性角膜溃疡", "H16.006", DiseaseGroup.CORNEAL_ULCER),
    ViralCornealUlcer("病毒性角膜溃疡", "H16.000*001", DiseaseGroup.CORNEAL_ULCER),
    FungalCornealUlcer("真菌性角膜溃疡", "B49.x04+H19.2*", DiseaseGroup.CORNEAL_ULCER),

    // 黄斑变性组
    AgeRelatedMacularDegeneration("年龄相关性黄斑变性", "H35.305", DiseaseGroup.MACULAR_DEGENERATION),
    AtrophicAgeRelatedMacularDegeneration("萎缩性年龄相关性黄斑变性", "H35.300x010", DiseaseGroup.MACULAR_DEGENERATION),
    DryAgeRelatedMacularDegeneration("干性年龄相关性黄斑变性", "H35.300x010", DiseaseGroup.MACULAR_DEGENERATION),
    ExudativeAgeRelatedMacularDegeneration("渗出性年龄相关性黄斑变性", "H35.300x011", DiseaseGroup.MACULAR_DEGENERATION),
    WetAgeRelatedMacularDegeneration("湿性年龄相关性黄斑变性", "H35.300x011", DiseaseGroup.MACULAR_DEGENERATION),

    // 分类组
    Cataract("白内障", "H26.900", DiseaseGroup.CATARACT),
    NuclearCataract("核性白内障", "H25.100", DiseaseGroup.CATARACT),
    TraumaticCataract("外伤性白内障", "H26.100", DiseaseGroup.CATARACT),
    CongenitalCataract("先天性白内障", "Q12.000", DiseaseGroup.CATARACT),
    SteroidCataract("激素性白内障", "H26.300x001", DiseaseGroup.CATARACT),
    DrugInducedCataract("药物性白内障", "H26.300", DiseaseGroup.CATARACT),
    ComplicatedCataract("并发性白内障", "H26.200", DiseaseGroup.CATARACT),

    // 干眼组
    DryEye("干眼症", "H16.202", DiseaseGroup.DRY_EYE),
    DryEyeSyndrome("干眼综合征", "H04.103", DiseaseGroup.DRY_EYE);

    private final String name; // 疾病名称
    private final String code; // 疾病代码
    private final DiseaseGroup group; // 疾病组别

    @Getter
    @AllArgsConstructor
    // 枚举类型定义，疾病组别
    public enum DiseaseGroup {
        DIABETES_RETINOPATHY("糖尿病视网膜病变"),
        RETINAL_VEIN_OCCLUSION("视网膜静脉阻塞"),
        HIGH_MYOPIA("高度近视"),
        GLAUCOMA("青光眼"),
        CORNEAL_ULCER("角膜溃疡"),
        MACULAR_DEGENERATION("黄斑变性"),
        CATARACT("白内障"),
        DRY_EYE("干眼");

        private final String name;
    }


    public static Disease findByName(String diseaseName) {
        for (Disease disease : Disease.values()) {
            if (disease.getName().equals(diseaseName)) {
                return disease;
            }
        }
        return null; // 如果没有找到，返回 null
    }
}
