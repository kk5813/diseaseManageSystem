package com.zcc.highmyopia.common;

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

    /**
     * 定义出缓存key的前缀标识，
     */
    public static class RedisKey {
        public static String PATIENTS = "patient_";

        public static String SITE = "site_";

        public static String DOCTOR = "doctor_";

        public static String DEPT = "dept_";
    }
    public static class FollowupStatus{
        public static int DELETE = -1;
        public static int NOT_FOLLOW = 0;

        public static int FOLLOW_SUCCESS = 1;
    }

}
