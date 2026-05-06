package com.github.owenliou.campkeeper.web.common.sso.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import tw.gov.nat.mydata.auth.IdToken;

import java.util.Date;

@ToString(callSuper = true)
public class CustomIdToken extends IdToken {

    @JsonIgnore
    public boolean isExpired() {
        return getExp() < (int) (new Date().getTime() / 1000);
    }

    @JsonIgnore
    public boolean isAdmin() {
        if (StringUtils.isBlank(getRoleCode())) {
            return false;
        }

        return Role.isAdmin(getRoleCode().toUpperCase());
    }

    @JsonIgnore
    public boolean isProvider() {
        if (StringUtils.isBlank(getRoleCode())) {
            return false;
        }

        return Role.isProvider(getRoleCode().toUpperCase());
    }

    public Integer roleLevel() {
        return Role.fromCode(getRoleCode()).level;
    }

}
