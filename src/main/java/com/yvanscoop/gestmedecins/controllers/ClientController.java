package com.yvanscoop.gestmedecins.controllers;


import com.yvanscoop.gestmedecins.entities.security.Client;
import com.yvanscoop.gestmedecins.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class ClientController {

    private static int currentPage = 1;
    private static int pageSize = 5;
    private static String motsearch = "";
    
    @Autowired
    private ClientService clientService;

    @RequestMapping(value = "/patients")
    public String getAllClients(Model model,
                                 @RequestParam("nom") Optional<String> motcle,
                                 @RequestParam("page") Optional<Integer> page,
                                 @RequestParam("size") Optional<Integer> size) {
        motcle.ifPresent(nom -> motsearch = nom);
        page.ifPresent(p -> currentPage = p);
        size.ifPresent(s -> pageSize = s);
        Page<Client> clientPage = clientService.findPaginated(motsearch, PageRequest.of(currentPage - 1, pageSize));

        int totalPages = clientPage.getTotalPages();
        if (currentPage > totalPages) {
            currentPage = 1;
            clientPage = clientService.findPaginated(motsearch, PageRequest.of(currentPage - 1, pageSize));
        }
        model.addAttribute("patients", clientPage);
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "clients";
    }
}
