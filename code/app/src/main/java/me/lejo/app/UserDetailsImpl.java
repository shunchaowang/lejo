package me.lejo.app;

import me.lejo.app.vo.Menu;
import me.lejo.app.vo.SubMenu;
import me.lejo.core.domain.MenuCategory;
import me.lejo.core.domain.MenuItem;
import me.lejo.core.domain.Permission;
import me.lejo.core.domain.Role;
import me.lejo.core.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsImpl.class);

    private String username;
    private String password;
    private boolean active;
    private HashSet<Role> roles;
    private HashSet<Permission> permissions;
    private List<Menu> menus;

    public UserDetailsImpl(User user) {

        username = user.getUsername();
        password = user.getPassword();
        active = user.isActive();
        roles = new HashSet<>();
        permissions = new HashSet<>();
        menus = new ArrayList<>();

        // permission should combine user's permissions and user's roles' permissions

        if (!CollectionUtils.isEmpty(user.getPermissions())) {
            permissions.addAll(user.getPermissions());
        }

        if (!CollectionUtils.isEmpty(user.getRoles())) {
            roles.addAll(user.getRoles());
            user.getRoles().forEach(role -> {
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    permissions.addAll(role.getPermissions());
                }
            });
        }

        // iterate permission to populate menus
        HashMap<String, Menu> menuMap = new HashMap<>();
        for (Permission permission : permissions) {
            MenuItem menuItem = permission.getMenuItem();
            if (menuItem == null) continue;
            MenuCategory menuCategory = menuItem.getMenuCategory();
            if (menuCategory == null) continue;

            if (!menuMap.containsKey(menuCategory.getName())) {
                // add a new key entry
                menuMap.put(menuCategory.getName(), new Menu(menuCategory));
            }

            SubMenu subMenu = new SubMenu(menuItem);
            if (menuMap.get(menuCategory.getName()).getSubMenus().contains(subMenu)) {
                continue;
            }

            menuMap.get(menuCategory.getName()).getSubMenus().add(subMenu);
        }

        // sort it
        menus = new ArrayList<>(menuMap.values());
        Collections.sort(menus);
        menus.forEach(menu -> {
            Collections.sort(menu.getSubMenus());
        });

        LOGGER.debug("Initialized user details successfully " + username);
    }

    /**
     * Returns the authorities granted to the user. Cannot return <code>null</code>.
     *
     * @return the authorities, sorted by natural key (never <code>null</code>)
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
            authorities.add(authority);
        }
        return authorities;
    }

    /**
     * Returns the password used to authenticate the user.
     *
     * @return the password
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Returns the username used to authenticate the user. Cannot return <code>null</code>
     * .
     *
     * @return the username (never <code>null</code>)
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Indicates whether the user's account has expired. An expired account cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user's account is valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    /**
     * Indicates whether the user is locked or unlocked. A locked user cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user is not locked, <code>false</code> otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    /**
     * Indicates whether the user's credentials (password) has expired. Expired
     * credentials prevent authentication.
     *
     * @return <code>true</code> if the user's credentials are valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    /**
     * Indicates whether the user is enabled or disabled. A disabled user cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user is enabled, <code>false</code> otherwise
     */
    @Override
    public boolean isEnabled() {
        return active;
    }

    public HashSet<Permission> getPermissions() {
        return permissions;
    }

    public HashSet<Role> getRoles() {
        return roles;
    }

    public List<Menu> getMenus() {
        return menus;
    }
}
