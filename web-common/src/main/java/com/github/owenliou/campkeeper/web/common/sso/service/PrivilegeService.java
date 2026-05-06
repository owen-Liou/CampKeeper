package com.github.owenliou.campkeeper.web.common.sso.service;


import com.github.owenliou.campkeeper.web.common.sso.model.SsoUserDetails;

import java.util.Optional;

public interface PrivilegeService {

    boolean hasLoginInfo();

    SsoUserDetails getCurrentSsoUserDetails();

    /**
     * 是否可以看全部機關：MODA_ADMIN, SYS-ADMIN, CUSTOMER-SERVICE
     */
    boolean isAdmin();

    boolean isNotAdmin();

    /**
     * 使用者帳號資料庫 ID
     */
    Optional<Integer> getSysUserId();

    /**
     * 使用者帳號
     */
    Optional<String> getAccount();

    /**
     * 機關代號
     */
    Optional<String> getProviderId();

    /**
     * 是我權限範圍內的 DP
     * @param dpId
     */
    boolean isMyDp(String dpId);

    /**
     * 不是我權限範圍內的 DP
     * @param dpId
     */
    boolean isNotMyDp(String dpId);

}
