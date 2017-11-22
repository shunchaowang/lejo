package me.lejo.app.controller.rest;

import me.lejo.app.vo.RestResponse;
import me.lejo.app.vo.RoleCommand;
import me.lejo.app.vo.UserCommand;
import me.lejo.core.domain.Role;
import me.lejo.core.domain.User;
import me.lejo.core.repository.RoleRepository;
import me.lejo.core.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/rest/user")
public class RestUserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestUserController.class);
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/create")
    public RestResponse create(@RequestBody @Valid UserCommand userCommand, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new RestResponse("error", bindingResult.getAllErrors());
        }

        User user = userRepository.save(create(userCommand));
        return new RestResponse("success", new UserCommand(user));
    }

    @PutMapping("/update")
    RestResponse update(@Valid @RequestBody UserCommand userCommand, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new RestResponse("error", bindingResult.getAllErrors());
        }

        User user = userRepository.save(update(userCommand));
        return new RestResponse("success", new UserCommand(user));
    }

    @DeleteMapping("/delete/{id}")
    RestResponse delete(@PathVariable long id) {
        User user = userRepository.getOne(id);
        userRepository.delete(id);
        return new RestResponse("success", user.getUsername());
    }


    private User create(UserCommand userCommand) {

        User user = new User();
        user.setDateCreated(new Date());
        user.setPassword(passwordEncoder.encode("password"));
        user.setActive(true);

        clone(user, userCommand);

        return user;
    }

    private User update(UserCommand userCommand) {

        User user = userRepository.getOne(userCommand.getId());
        user.setLastUpdated(new Date());
        user.getRoles().clear();

        clone(user, userCommand);

        return user;
    }

    private void clone(User user, UserCommand userCommand) {
        user.setUsername(userCommand.getUsername());
        user.setFirstName(userCommand.getFirstName());
        user.setLastName(userCommand.getLastName());
        user.setDescription(userCommand.getDescription());

        for (RoleCommand roleCommand : userCommand.getRoles()) {
            Role role = roleRepository.getOne(roleCommand.getId());
            user.getRoles().add(role);
        }
    }
}