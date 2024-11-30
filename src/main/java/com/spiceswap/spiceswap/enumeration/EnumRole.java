package com.spiceswap.spiceswap.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum EnumRole {
    ADMIN(
            Set.of(
                    EnumPermission.ADMIN_CREATE,
                    EnumPermission.ADMIN_READ,
                    EnumPermission.ADMIN_UPDATE,
                    EnumPermission.ADMIN_DELETE
            )
    ),
    USER(
            Set.of(
                    EnumPermission.USER_READ,
                    EnumPermission.USER_CREATE,
                    EnumPermission.USER_UPDATE,
                    EnumPermission.USER_DELETE
            )
    );

    @Getter
    private final Set<EnumPermission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
