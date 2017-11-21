package me.lejo.app;

import me.lejo.core.domain.MenuCategory;
import me.lejo.core.domain.MenuItem;
import me.lejo.core.domain.Permission;
import me.lejo.core.domain.Role;
import me.lejo.core.domain.User;
import me.lejo.core.repository.MenuCategoryRepository;
import me.lejo.core.repository.MenuItemRepository;
import me.lejo.core.repository.PermissionRepository;
import me.lejo.core.repository.RoleRepository;
import me.lejo.core.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

@Profile("default")
@Component
public class DefaultDataLoader implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDataLoader.class);

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MenuCategoryRepository menuCategoryRepository;
    @Autowired
    private MenuItemRepository menuItemRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    @Override
    public void run(String... args) throws Exception {

        LOGGER.debug("Bootstrapping data ...");

        // create menus
        MenuCategory userManagementMenuCategory = new MenuCategory("userManagement", 1);
        userManagementMenuCategory = menuCategoryRepository.save(userManagementMenuCategory);
        MenuItem manageUserMenuItem = new MenuItem("manageUser", "user/index", 11);
        manageUserMenuItem.setMenuCategory(userManagementMenuCategory);
        manageUserMenuItem = menuItemRepository.save(manageUserMenuItem);

        // create permissions
        Permission manageUserPermission = new Permission("manageUser", true);
        manageUserPermission.setMenuItem(manageUserMenuItem);
        manageUserPermission = permissionRepository.save(manageUserPermission);

        // create roles
        Role adminRole = new Role("ROLE_ADMIN", true);
        adminRole.setPermissions(new HashSet<>(Arrays.asList(manageUserPermission)));
        adminRole = roleRepository.save(adminRole);
        Role userRole = new Role("ROLE_USER", true);
        userRole = roleRepository.save(userRole);

        // create users
        User adminUser = new User("admin", passwordEncoder.encode("admin"), "Admin",
                "Admin", new Date(), true);
        adminUser.setRoles(new HashSet<>(Arrays.asList(adminRole)));
        userRepository.save(adminUser);
        User userUser = new User("user", passwordEncoder.encode("user"), "User",
                "User", new Date(), true);
        userUser.setActive(true);
        userUser.setRoles(new HashSet<>(Arrays.asList(userRole)));
        userRepository.save(userUser);

        LOGGER.debug("Done bootstrapping.");
    }
}
