package com.yhcd.coding.ssjwt.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

public class JwtGrantedAuthority implements GrantedAuthority {
    private String authority;

    public JwtGrantedAuthority() {
    }

    public JwtGrantedAuthority(String authority) {
        Assert.hasText(authority, "A granted authority textual representation is required");
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof JwtGrantedAuthority) {
            return authority.equals(((JwtGrantedAuthority) obj).authority);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.authority.hashCode();
    }

    @Override
    public String toString() {
        return this.authority;
    }
}
