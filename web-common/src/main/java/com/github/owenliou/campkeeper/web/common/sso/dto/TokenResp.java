package com.github.owenliou.campkeeper.web.common.sso.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class TokenResp {

    private CustomIdToken idToken;

    private CustomAccessToken accessToken;

    private String refreshToken;

    public TokenResp() {
        //
    }

    public TokenResp(CustomIdToken idToken, CustomAccessToken accessToken, String refreshToken) {
        this.idToken = idToken;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
