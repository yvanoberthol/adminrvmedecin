package com.yvanscoop.gestmedecins.entities.security;

import org.springframework.security.core.GrantedAuthority;

public class Authotity implements GrantedAuthority {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String authority;

    public Authotity(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
