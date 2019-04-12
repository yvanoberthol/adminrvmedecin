package com.yvanscoop.gestmedecins.controllers;


import com.yvanscoop.gestmedecins.entities.Role;
import com.yvanscoop.gestmedecins.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class RoleController {

    private static int currentPage = 1;
    private static int pageSize = 5;
    private static String motsearch = "";

    @Autowired
    private RoleService RoleService;

    @RequestMapping(value = "/roles")
    public String getAllRoles(Model model,
                              @RequestParam("nom") Optional<String> motcle,
                              @RequestParam("page") Optional<Integer> page,
                              @RequestParam("size") Optional<Integer> size) {
        motcle.ifPresent(nom -> motsearch = nom);
        page.ifPresent(p -> currentPage = p);
        size.ifPresent(s -> pageSize = s);
        Page<Role> RolePage = RoleService.findPaginated(motsearch, PageRequest.of(currentPage - 1, pageSize));

        int totalPages = RolePage.getTotalPages();
        if (currentPage > totalPages) {
            currentPage = 1;
            RolePage = RoleService.findPaginated(motsearch, PageRequest.of(currentPage - 1, pageSize));
        }
        model.addAttribute("Roles", RolePage);
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "Roles";
    }

    @RequestMapping(value = "/deleteRole", method = RequestMethod.POST)
    public String deleteRole(@RequestParam("id") Long id) {
        RoleService.delete(id);
        return "redirect:/Roles";
    }

    @RequestMapping(value = "/formModifRole")
    public String formModifRole(@RequestParam("id") Long id, Model model) {
        Role Role = RoleService.getOne(id);
        model.addAttribute("Role", Role);
        return "modifRole";
    }

    @RequestMapping(value = "/detailRole")
    public String detailRole(@RequestParam("id") Long id, Model model) {
        Role Role = RoleService.getOne(id);
        model.addAttribute("Role", Role);
        return "detailRole";
    }

    @RequestMapping(value = "/formAddRole")
    public String formAddRole(Model model) {
        model.addAttribute("Role", new Role());
        return "addRole";
    }

    @RequestMapping(value = "/modifRole", method = RequestMethod.POST)
    public String modifRole(Role RoleUpdated) {
        Role Role = RoleService.getOne(RoleUpdated.getId());
        Role.setRole(RoleUpdated.getRole());
        RoleService.save(Role);
        return "redirect:/Roles";
    }

    @RequestMapping(value = "/addRole", method = RequestMethod.POST)
    public String addRole(Role Roleadd, Model model) {
        Role RoleName = RoleService.getByNom(Roleadd.getRole());
        if (RoleName != null) {
            model.addAttribute("RoleExists", true);
            model.addAttribute("Role", new Role());
            return "addRole";
        }
        RoleService.save(Roleadd);
        return "redirect:/Roles";
    }

}
