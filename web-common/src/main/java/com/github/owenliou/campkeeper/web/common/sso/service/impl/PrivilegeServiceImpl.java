package com.github.owenliou.campkeeper.web.common.sso.service.impl;

import com.github.owenliou.campkeeper.config.variables.Profiles;
import com.github.owenliou.campkeeper.web.common.sso.model.SsoUserDetails;
import com.github.owenliou.campkeeper.web.common.sso.service.PrivilegeService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PrivilegeServiceImpl implements PrivilegeService {

    @Override
    public boolean hasLoginInfo() {
        return SecurityContextHolder.getContext() != null &&
                SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null &&
                SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SsoUserDetails;
    }

    /**
     * 取得當下登入的使用者
     * @return null 未登入
     */
    @Override
    public SsoUserDetails getCurrentSsoUserDetails() {
        if (hasLoginInfo()) {
            return (SsoUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return null;
    }

    @Override
    public boolean isAdmin() {
        if (Profiles.isCurrentProfileMatch(Profiles.DEV, Profiles.DEV_LAB, Profiles.LAB)) {
            return true;
        }

        if (hasLoginInfo()) {
            return getCurrentSsoUserDetails().isAdmin();
        }
        return false;
    }

    @Override
    public boolean isNotAdmin() {
        return !isAdmin();
    }

    @Override
    public Optional<Integer> getSysUserId() {
        if (hasLoginInfo()) {
            return Optional.of(getCurrentSsoUserDetails().getIdToken().getSub());
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> getAccount() {
        if (hasLoginInfo()) {
            return Optional.of(getCurrentSsoUserDetails().getIdToken().getAccount());
        }

        return Optional.empty();
    }

}
