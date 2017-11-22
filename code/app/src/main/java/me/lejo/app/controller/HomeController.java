package me.lejo.app.controller;

import me.lejo.app.UserDetailsImpl;
import me.lejo.app.util.Constant;
import me.lejo.app.vo.PasswordCommand;
import me.lejo.app.vo.ProfileCommand;
import me.lejo.core.domain.User;
import me.lejo.core.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Locale;

@Controller
public class HomeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    MessageSource messageSource;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private LocaleResolver localeResolver;

    @GetMapping({"/home", "/index", "/"})
    String home() {
        return "home/index";
    }

    @GetMapping("/login")
    String login() {
        return "login";
    }

    /**
     * Principal and Authentication can both be auto binded by spring context.
     * A principal can call name to get logged user's name,
     * An authentication can be casted to UserDetails.
     *
     * @param principal
     * @return
     */
    @GetMapping("/home/profile")
    String profile(Principal principal, Model model) {
        UserDetailsImpl userDetails = (UserDetailsImpl) ((Authentication) principal).getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername());
        //User user = userRepository.findByUsername(principal.getName());
        model.addAttribute("profileCommand", new ProfileCommand(user));
        return "home/profile";
    }

    @PostMapping("/home/profile")
    String saveProfile(@Valid final ProfileCommand profileCommand, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "home/profile";
        }

        User user = userRepository.getOne(profileCommand.getId());
        user.setUsername(profileCommand.getUsername());
        user.setFirstName(profileCommand.getFirstName());
        user.setLastName(profileCommand.getLastName());
        user.setDescription(profileCommand.getDescription());
        userRepository.save(user);

        return "redirect:/home";
    }

    @GetMapping("/home/password")
    String password(Principal principal, Model model) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username);
        model.addAttribute("passwordCommand", new PasswordCommand(user));
        return "home/password";
    }

    @PostMapping("/home/password")
    String savePassword(@Valid final PasswordCommand passwordCommand, BindingResult bindingResult) {

        User user = userRepository.getOne(passwordCommand.getId());

        if (!passwordEncoder.matches(passwordCommand.getCurrentPassword(), user.getPassword())) {
            Locale locale = LocaleContextHolder.getLocale();
            String error = messageSource.getMessage("password.not.correct.message", null, locale);
            bindingResult.rejectValue("currentPassword", "user.password", error);
        }
        if (bindingResult.hasErrors()) {
            return "home/password";
        }

        user.setPassword(passwordEncoder.encode(passwordCommand.getPassword()));
        userRepository.save(user);

        return "redirect:/home";
    }

    @GetMapping(value = "/table/lang", produces = "application/json;charset=UTF-8")
    @ResponseBody
    String dataTableLang() {

        Locale locale = LocaleContextHolder.getLocale();

        if (locale.equals(Locale.CHINA)) {
            return Constant.dataTableChineseLanguage;
        } else if (locale.equals(Locale.ENGLISH)) {
            return Constant.dataTableEnglishLanguage;
        }

        return Constant.dataTableChineseLanguage;
    }

    @GetMapping("/403")
    String error403() {
        return "403";
    }
}
