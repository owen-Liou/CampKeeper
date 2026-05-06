package com.github.owenliou.campkeeper.web.common.sso.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.ToString;
import tw.gov.nat.mydata.auth.AccessToken;

import java.util.Date;

@ToString(callSuper = true)
public class CustomAccessToken extends AccessToken {

    @JsonIgnore
    public boolean isExpired() {
        return getExp() < (int) (new Date().getTime() / 1000);
    }

}
