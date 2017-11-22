package me.lejo.app.controller;

import me.lejo.app.util.JsonUtil;
import me.lejo.app.vo.DataTableParams;
import me.lejo.app.vo.DataTableResult;
import me.lejo.app.vo.RoleCommand;
import me.lejo.app.vo.UserCommand;
import me.lejo.core.domain.Role;
import me.lejo.core.domain.User;
import me.lejo.core.repository.RoleRepository;
import me.lejo.core.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    // index view
    // Role and Authority are the same, hasRole and hasAuthority are the same as well.
    // hasAuthority('ROLE_ADMIN') has same result with hasRole('ROLE_ADMIN')
    // @PreAuthorize("isAuthenticated()")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')") // role based, isAuthenticated is not necessary here
    @PreAuthorize("hasPermission('', 'manageUser')")
    // permission based
    @GetMapping("/index")
    String index(Model model) {
        List<RoleCommand> commands = new ArrayList<>();
        for (Role role : roleRepository.findAll()) {
            commands.add(new RoleCommand(role));
        }
        model.addAttribute("roles", commands);
        return "user/index";
    }

    @PreAuthorize("hasPermission('', 'manageUser')")
    // permission based
    @GetMapping(value = "list", produces = "application/json;charset=UTF-8")
    @ResponseBody
    String list(HttpServletRequest request) {

        DataTableParams params = new DataTableParams(request);

        // populate jpa specification
        // sorting
        Sort sort = new Sort(Sort.Direction.DESC, "id"); // default is id desc
        if (StringUtils.isNotEmpty(params.getOrder()) && StringUtils.isNotEmpty(params.getOrderDir())) {
            sort = new Sort(Sort.Direction.fromString(params.getOrderDir()), params.getOrder());
        }

        // pagination, page has already been calculated by params
        Pageable pageable = new PageRequest(params.getPage(), params.getSize(), sort);

        // filtering if existing in the params
        final String search = "%" + params.getSearch() + "%";
        Specification<User> specification = (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.like(root.get("username"), search)); // username
            predicates.add(cb.like(root.get("firstName"), search)); // first name
            predicates.add(cb.like(root.get("lastName"), search)); // last name

            return cb.or(predicates.toArray(new Predicate[predicates.size()]));
        };

        Page<User> page = userRepository.findAll(specification, pageable);


        DataTableResult<UserCommand> result = new DataTableResult<>(params);
        List<UserCommand> userCommands = new ArrayList<>();
        for (User user : page.getContent()) {
            userCommands.add(new UserCommand(user));
        }

        result.setData(userCommands);
        result.setRecordsTotal(page.getTotalElements());
        result.setRecordsFiltered(userRepository.count(specification));

        LOGGER.debug("Gson result: " + JsonUtil.toJson(result));

        return JsonUtil.toJson(result);
    }
}
