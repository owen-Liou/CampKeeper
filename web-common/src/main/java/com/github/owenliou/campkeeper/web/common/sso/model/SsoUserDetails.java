package com.github.owenliou.campkeeper.web.common.sso.model;

import com.github.owenliou.campkeeper.web.common.sso.dto.CustomAccessToken;
import com.github.owenliou.campkeeper.web.common.sso.dto.CustomIdToken;
import com.github.owenliou.campkeeper.web.common.sso.dto.TokenResp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * SSO 使用者資訊   
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class SsoUserDetails extends User implements GrantedAuthoritiesContainer {

    private final static String role_user = "ROLE_USER";
    private final static String role_admin = "ROLE_ADMIN";
    private final static String role_provider = "ROLE_PROVIDER";

    private CustomIdToken idToken;
    private CustomAccessToken accessToken;
    private String refreshToken;
    /**
     * TokenResp
     */
    private String tokenInfo;

    public SsoUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getGrantedAuthorities() {
        return getAuthorities();
    }

    /**
     * Spring Security 登入帳號叫 username
     */
    @Override
    public String getUsername() {
        return idToken.getAccount();
    }

/*
    // 有了 getUsername() 結果 lombok 不自動產生 getUserName()，所以只好自己加
    public String getUserName() {
        return idToken.getName();
    }
*/

    @Override
    public boolean isAccountNonExpired() {
        return !idToken.isExpired() && !accessToken.isExpired();
    }

    public boolean isAccountExpired() {
        return !isAccountNonExpired();
    }

    public boolean isDisabled() {
        return !isEnabled();
    }

    public boolean isAdmin() {
        return getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(role_admin));
    }

    public static SsoUserDetails fromTokenResp(TokenResp tokenResp, String tokenInfo) {
        SsoUserDetails result = new SsoUserDetails("N/A", "N/A", authorities(tokenResp.getIdToken()));
        result.setIdToken(tokenResp.getIdToken());
        result.setAccessToken(tokenResp.getAccessToken());
        result.setRefreshToken(tokenResp.getRefreshToken());
        result.setTokenInfo(tokenInfo);
        return result;
    }

    public static List<GrantedAuthority> authorities(CustomIdToken customIdToken) {
        if (customIdToken == null || StringUtils.isBlank(customIdToken.getRoleCode())) {
            return Collections.emptyList();
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role_user));

        if (customIdToken.isAdmin()) {
            authorities.add(new SimpleGrantedAuthority(role_admin));
        }

        if (customIdToken.isProvider()) {
            authorities.add(new SimpleGrantedAuthority(role_provider));
        }

        return authorities;
    }

}
