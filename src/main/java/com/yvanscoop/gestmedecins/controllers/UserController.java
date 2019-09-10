package com.yvanscoop.gestmedecins.controllers;

import com.yvanscoop.gestmedecins.entities.Role;
import com.yvanscoop.gestmedecins.entities.User;
import com.yvanscoop.gestmedecins.services.RoleService;
import com.yvanscoop.gestmedecins.services.UserService;
import com.yvanscoop.gestmedecins.utilities.SecurityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class UserController {
    private static int currentPage = 1;
    private static int pageSize = 5;
    private static String motsearch = "";

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    //@Secured(value = {"ROLE_SUPERADMIN"})
    @RequestMapping(value = "/users")
    public String getAllUsers(Model model,
                              @RequestParam("nom") Optional<String> motcle,
                              @RequestParam("page") Optional<Integer> page,
                              @RequestParam("size") Optional<Integer> size) {
        motcle.ifPresent(nom -> motsearch = nom);
        page.ifPresent(p -> currentPage = p);
        size.ifPresent(s -> pageSize = s);
        Page<User> userPage = userService.findPaginated(motsearch, PageRequest.of(currentPage - 1, pageSize));
        int totalPages = userPage.getTotalPages();
        if (currentPage > totalPages) {
            currentPage = 1;
            userPage = userService.findPaginated(motsearch, PageRequest.of(currentPage - 1, pageSize));
        }
        model.addAttribute("users", userPage);
        model.addAttribute("roles", roleService.findPaginated("", PageRequest.of(currentPage - 1, pageSize)).getContent());
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "users";
    }

    //@Secured(value = {"ROLE_SUPERADMIN"})
    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    public String deleteUser(@RequestParam("id") Long id, RedirectAttributes redirectAttribute) {
        User user = userService.getOne(id);
        Collection<String> roles = new ArrayList<>();
        if (!user.getRoles().isEmpty()) {
            for (Role role : user.getRoles()) {
                roles.add(role.getRole());
            }
            if (roles.contains("SUPERADMIN")) {
                redirectAttribute.addFlashAttribute("useradmin", true);
                return "redirect:/users";
            }
        }
        userService.delete(id);
        return "redirect:/users";
    }

    @Secured(value = {"ROLE_SUPERADMIN"})
    @RequestMapping(value = "/desactivateUser", method = RequestMethod.POST)
    public String desactivateUser(@RequestParam("id") Long id, RedirectAttributes redirectAttribute) {
        User user = userService.getOne(id);
        Collection<String> roles = new ArrayList<>();
        if (!user.getRoles().isEmpty()) {
            for (Role role : user.getRoles()) {
                roles.add(role.getRole());
            }
            if (roles.contains("SUPERADMIN")) {
                redirectAttribute.addFlashAttribute("useradminDesactivate", true);
                return "redirect:/users";
            }
        }
        userService.desactivate(user.getUsername());
        return "redirect:/users";
    }

    @Secured(value = {"ROLE_SUPERADMIN"})
    @RequestMapping(value = "/activateUser", method = RequestMethod.POST)
    public String activateUser(@RequestParam("id") Long id) {
        User user = userService.getOne(id);
        Collection<String> roles = new ArrayList<>();
        if (!user.getRoles().isEmpty()) {
            for (Role role : user.getRoles()) {
                roles.add(role.getRole());
            }
        }
        userService.activate(user.getUsername());
        return "redirect:/users";
    }


    @RequestMapping(value = "/formModifUser")
    public String formModifUser(Model model, HttpSession httpSession) {
        SecurityContext securityContext = (SecurityContext) httpSession.getAttribute("SPRING_SECURITY_CONTEXT");
        String user_current = securityContext.getAuthentication().getName();
        User User = userService.getByNom(user_current);
        model.addAttribute("user", User);
        return "modifUser";
    }

    //@Secured(value = {"ROLE_SUPERADMIN"})
    @RequestMapping(value = "/formAddUser")
    public String formAddUser(Model model) {
        model.addAttribute("user", new User());
        return "addUser";
    }

    @RequestMapping(value = "/modifUser", method = RequestMethod.POST)
    public String modifUser(@Valid User userUpdated, BindingResult bindingResult, @RequestParam("newpassword") String newpassword, Model model,
                            HttpSession httpSession) {
        if (!bindingResult.hasErrors() && newpassword.length() >= 8) {
            SecurityContext securityContext = (SecurityContext) httpSession.getAttribute("SPRING_SECURITY_CONTEXT");
            String user_current = securityContext.getAuthentication().getName();
            User userByName = userService.getByNom(userUpdated.getUsername());
            User userRegister = userService.getOne(userUpdated.getId());
            BCryptPasswordEncoder passwordEncoder = SecurityUtility.passwordEncoder();
            if (userByName != null) {
                if (!userUpdated.getUsername().equals(user_current)) {
                    model.addAttribute("usernameExists", true);
                    model.addAttribute("user", userRegister);
                    return "modifUser";
                }
            }
            if (!passwordEncoder.matches(userUpdated.getPassword(), userRegister.getPassword())) {
                model.addAttribute("passwordIncorrect", true);
                model.addAttribute("user", userRegister);
                return "modifUser";
            }
            String encryptpassword = SecurityUtility.passwordEncoder().encode(newpassword);
            userRegister.setUsername(userUpdated.getUsername());
            userRegister.setPassword(encryptpassword);
            userService.save(userRegister);
            return "redirect:/logout";
        } else {
            if (newpassword.isEmpty()) {
                model.addAttribute("newPasswordEmpty", true);
            }

            if (newpassword.length() > 0 && newpassword.length() < 8) {
                model.addAttribute("newPasswordSize", true);
            }

            return "modifUser";
        }

    }

    //@Secured(value = {"ROLE_SUPERADMIN"})
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public String addUser(@Valid User useradd, BindingResult bindingResult, Model model) {

        if (!bindingResult.hasErrors()) {
            User userByName = userService.getByNom(useradd.getUsername());

            if (userByName != null) {
                model.addAttribute("userExists", true);
                model.addAttribute("user", new User());
                return "addUser";
            }

            String encryptpassword = SecurityUtility.passwordEncoder().encode(useradd.getPassword());
            User user = new User();
            user.setUsername(useradd.getUsername());
            user.setPassword(encryptpassword);
            user.setActive(useradd.isActive());
            userService.save(user);

            return "redirect:/users";
        } else {

            model.addAttribute("user", useradd);
            return "addUser";
        }

    }

    //@Secured(value = {"ROLE_SUPERADMIN"})
    @RequestMapping(value = "/addRoles", method = RequestMethod.POST)
    public String saveUserRole(long user_id, long roles_id, Model model, RedirectAttributes redirectAttribute) {
        User user = userService.getOne(user_id);
        Role role = roleService.getOne(roles_id);

        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            userService.save(user);
            redirectAttribute.addFlashAttribute("addrole", true);
        } else {
            redirectAttribute.addFlashAttribute("notaddrole", true);
        }

        return "redirect:/users";
    }

    @Secured(value = {"ROLE_SUPERADMIN"})
    @RequestMapping(value = "/suppUserRole", method = RequestMethod.GET)
    public String suppUserRole(@RequestParam("user_id") Long user_id,
                               @RequestParam("role_id") Long role_id, RedirectAttributes redirectAttribute,
                               HttpSession httpSession) {


        User user = userService.getOne(user_id);
        Role role_define = roleService.getOne(role_id);
        SecurityContext securityContext = (SecurityContext) httpSession.getAttribute("SPRING_SECURITY_CONTEXT");
        String user_current = securityContext.getAuthentication().getName();
        Collection<String> roles = new ArrayList<>();
        if (!user.getRoles().isEmpty()) {
            for (Role role : user.getRoles()) {
                roles.add(role.getRole());
            }

            if (roles.contains("SUPERADMIN") && !user.getUsername().equals(user_current)) {
                redirectAttribute.addFlashAttribute("useradminrole", true);
            } else {
                if (user.getRoles().contains(role_define)) {
                    user.getRoles().remove(role_define);
                    userService.save(user);
                }
            }
        }
        return "redirect:/users";
    }
}
