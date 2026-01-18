package com.zcc.highmyopia.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author zcc
 * @Date 2024/12/04
 * @Description
 */
public class Constants {

    public final static String SPLIT = ",";
    public final static String COLON = ":";
    public final static String SPACE = " ";
    public final static String UNDERLINE = "_";
    public final static String DOT = "_";
    public final static String MODEL_INPUT_SPLIT = ",";

    public final static HashMap<String, String> ItemNameToType = new HashMap<String, String>(){{
        put("光学相干断层成像（OCT）", "OCT");
        put("扫描激光眼底检查(SLO)", "SLO");
        put("眼底照相", "fundus");
        put("视野检查", "VF");
        put("超声生物显微镜检查（UBM）", "UBM");
        put("共焦激光显微镜角膜检查", "CLSM");
    }};

    /**
     * 定义出缓存key的前缀标识，
     */
    public static class RedisKey {
        public static String PATIENTS = "patient_";

        public static String SITE = "site_";

        public static String DOCTOR = "doctor_";

        public static String DEPT = "dept_";

        public static String USER = "user:";

        public static String RULE_TREE_VO_KEY = "rule_tree_vo_";

        public static String MODEL_DISEASE = "model_disease";

        public static String REPEAT_SUBMIT = "repeat_submit:";

        public static String MODEL_DISEASE_INPUT = "model_disease_input:";

        public static String MODEL_DISEASE_URL = "model_disease_url:";
    }
    public static class FollowupStatus{
        public static int DELETE = -1;
        public static int NOT_FOLLOW = 0;

        public static int FOLLOW_SUCCESS = 1;
    }
    public static class FollowupTemplateStatus{
        public static int DELETE = 0;
    }
    public static class UserStatus{
        public static int DELETE = -1;

        public static int ADMIN = 0;

        public static int COMMON = 1;
    }

    public static class RedisCacheTime{
        public static final long DEFAULT_EXPIRED = 24 * 60 * 60 * 1000L; // 24小时
        public static final long TEN_SECOND = 10 * 1000L; // 10秒
    }
    @Getter
    @AllArgsConstructor
    public enum FileFilterType{
        OCT("OCT","光学相干断层成像（OCT）"),
        SLO("SLO", "扫描激光眼底检查(SLO)"),
        Fund("fundus", "眼底照相"),
        VF("VF", "视野检查"),
        UBM("UBM", "超声生物显微镜检查（UBM）"),
        CLSM("CLSM", "共焦激光显微镜角膜检查"),
        DEFAULT("default","默认不进行过滤");
        private final String type;
        private final String typeName;
    }
//    @Getter
//    @AllArgsConstructor
//    public enum FileFilterSmallType{
//        defaultSmallType(-1,"默认类型，不会上传"),
//        AS_OCT(0,"眼前节OCT"),
//        MACULAR_OCT(1,"黄斑区OCT"),
//        OPTIC_OCT(2,"视盘区OCT"),
//        OTHER_OCT(3,"其他OCT");
//        private final Integer typeCode;
//        private final String smallType;
//    }

    public static final HashMap<String, String> OCTToType = new HashMap<String, String>(){{
        // wjl 视网膜静脉阻塞
        put("4","1");
        // zc
        put("5","1");
        // wt
        put("6","1");
        // sc
        put("7","0");
        // zcc
        put("8","2");
//        put("眼前节OCT", "0");
//        put("黄斑区OCT", "1");
//        put("视盘区OCT", "2");
//        put("其他OCT", "3");
    }};
}
