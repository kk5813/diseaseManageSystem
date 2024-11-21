package com.zcc.highmyopia.util;

import com.zcc.highmyopia.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;

public class ShiroUtil {


    public static AccountProfile getProfile() {
        return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
    }
}
