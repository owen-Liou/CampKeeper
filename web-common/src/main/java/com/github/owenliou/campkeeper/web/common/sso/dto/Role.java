package com.github.owenliou.campkeeper.web.common.sso.dto;

import java.util.List;

public enum Role {

/*
    system(0),
    maintenance(1),
    ndc(2),
    org(3),
    dp_sp(4),
    sub(5),
*/

    SYS_ADMIN(          1, "SYS-ADMIN",           "系統管理者"),
    REVIEW(             1, "REVIEW",              "審核人員"),
    CUSTOMER_SERVICE(   1, "CUSTOMER-SERVICE",    "維運客服人員"),

    MODA_ADMIN(         2, "MODA-ADMIN",          "數位部管理者"),

    PROVIDER_SUPERVISOR(3, "PROVIDER-SUPERVISOR", "機關管理者"),
    SMEPASS(            3, "SMEPASS",             "smepass機關管理者"),
    
    PROVIDER_ADMIN(     4, "PROVIDER-ADMIN",      "資料/服務管理者"), // DP/SP 的管理員(主帳號)
    PROVIDER_USER(      5, "PROVIDER-USER",       "副帳號"), // DP/SP 的一般使用者(副帳號)
    ;

    /**
     * level 愈大權限愈小
     */
    public final Integer level;

    public final String code;

    public final String name;

    public static final List<String> role_admin = List.of(MODA_ADMIN.code, SYS_ADMIN.code, CUSTOMER_SERVICE.code);

    public static final List<String> role_provider = List.of(PROVIDER_SUPERVISOR.code, SMEPASS.code, PROVIDER_ADMIN.code, PROVIDER_USER.code);

    Role(Integer level, String code, String name) {
        this.level = level;
        this.code = code;
        this.name = name;
    }

    public static Role fromCode(String code) {
        return Role.valueOf(code);
    }
    
    public static boolean isAdmin(String code) {
        return role_admin.contains(code);
    }

    public static boolean isProvider(String code) {
        return role_provider.contains(code);
    }
    
}
