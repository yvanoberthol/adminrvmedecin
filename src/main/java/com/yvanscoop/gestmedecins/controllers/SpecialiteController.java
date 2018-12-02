package com.yvanscoop.gestmedecins.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.yvanscoop.gestmedecins.entities.Medecin;
import com.yvanscoop.gestmedecins.entities.MedecinSpecialite;
import com.yvanscoop.gestmedecins.entities.Specialite;
import com.yvanscoop.gestmedecins.services.MedecinSpecialiteService;
import com.yvanscoop.gestmedecins.services.SpecialiteService;

@Controller
public class SpecialiteController {

	private static int currentPage = 1;
    private static int pageSize = 5;
    
    private static int currentPageDetail = 1;
    private static int pageSizeDetail = 5;
    private static String motsearch = "";
	
	@Autowired
	private SpecialiteService specialiteService;
	
	@Autowired
	private MedecinSpecialiteService msService;
	
	@RequestMapping(value="/specialites")
	public String getAllSpecialites(Model model,
				@RequestParam("nom") Optional<String> motcle, 
				@RequestParam("page") Optional<Integer> page, 
		        @RequestParam("size") Optional<Integer> size) {
		motcle.ifPresent(nom -> motsearch = nom);
		page.ifPresent(p -> currentPage = p);
        size.ifPresent(s -> pageSize = s);
        Page<Specialite> specialitePage = specialiteService.findPaginated(motsearch,PageRequest.of(currentPage - 1, pageSize));
        
        int totalPages = specialitePage.getTotalPages();
        if(currentPage > totalPages) {
        	currentPage = 1;
        	specialitePage = specialiteService.findPaginated(motsearch,PageRequest.of(currentPage - 1, pageSize));
        }
        model.addAttribute("specialites", specialitePage);
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
		return "specialites";
	}
	
	@RequestMapping(value="/deleteSpecialite",method=RequestMethod.POST)
	public String deleteSpecialite(@RequestParam("id") Long id) {
		Specialite specialite = specialiteService.getOne(id);
		specialiteService.delete(id);
		msService.deleteFromSpecialite(specialite);
		return "redirect:/specialites";
	}
	
	@RequestMapping(value="/formModifSpecialite")
	public String formModifSpecialite(@RequestParam("id") Long id,Model model) {
		Specialite specialite = specialiteService.getOne(id);
		model.addAttribute("specialite", specialite);
		return "modifSpecialite";
	}
	
	@RequestMapping(value="/detailSpecialite")
	public String detailSpecialite(@RequestParam("id") Long id,Model model,
									@RequestParam("page") Optional<Integer> page, 
							        @RequestParam("size") Optional<Integer> size,
							        Pageable pageable
			) {
		page.ifPresent(p -> currentPageDetail = p);
        size.ifPresent(s -> pageSizeDetail = s);
        pageable = PageRequest.of(currentPageDetail - 1, pageSizeDetail);
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
		Specialite specialite = specialiteService.getOne(id);
		List<MedecinSpecialite> lms = msService.findBySpecialite(specialite);
		List<Medecin> medecins = new ArrayList<>();
		for(MedecinSpecialite ms: lms) {
			medecins.add(ms.getMedecin());
		}
		List<Medecin> list;
		if (medecins.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSizeDetail, medecins.size());
            list = medecins.subList(startItem, toIndex);
        }
		Page<Medecin> medecinPage = new PageImpl<Medecin>(list, pageable, medecins.size());
		int totalPages = medecinPage.getTotalPages();
        if(currentPageDetail > totalPages) {
        	currentPageDetail = 1;
        	medecinPage = new PageImpl<Medecin>(list, pageable, medecins.size());
        }
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
            System.out.println(pageNumbers);
        }
        System.out.println(medecinPage.getContent());
		model.addAttribute("specialite", specialite);
		model.addAttribute("medecins", medecinPage);
		return "detailSpecialite";
	}
	
	@RequestMapping(value="/formAddSpecialite")
	public String formAddSpecialite(Model model) {
		model.addAttribute("specialite",new Specialite());
		return "addSpecialite";
	}
	
	@RequestMapping(value="/modifSpecialite",method=RequestMethod.POST)
	public String modifSpecialite(@Valid Specialite specialiteUpdated, BindingResult bindingResult, Model model) {
		
		Specialite specialite = specialiteService.getOne(specialiteUpdated.getId());
		if (!bindingResult.hasErrors()) {
			Specialite specialiteName = specialiteService.getByNom(specialiteUpdated.getNom());
			if(specialiteName !=null) {
				model.addAttribute("specialiteExists", true);
				model.addAttribute("specialite",specialiteUpdated);
				return "modifSpecialite";
			}
			specialite.setNom(specialiteUpdated.getNom());
			specialite.setDesription(specialiteUpdated.getDesription());
			specialiteService.save(specialite);
			return "redirect:/specialites";
		}else {
			return "modifSpecialite";
		}
		
	}
	
	@RequestMapping(value="/addSpecialite",method=RequestMethod.POST)
	public String addSpecialite(@Valid Specialite specialiteadd, BindingResult bindingResult, Model model) {
		 if (!bindingResult.hasErrors()) {
			 Specialite specialiteName = specialiteService.getByNom(specialiteadd.getNom());
				if(specialiteName !=null) {
					model.addAttribute("specialiteExists", true);
					model.addAttribute("specialite",specialiteadd);
					return "addSpecialite";
				}
				specialiteService.save(specialiteadd);
				return "redirect:/specialites";
		 }else {
				return "addSpecialite";
		 }

	}
	
    @RequestMapping(value = "/medecinSpecialiteStat", method = RequestMethod.GET)
    public String index(Model model) {
        List<Specialite> specialites = specialiteService.getAll("");
        List<Integer> counts = new ArrayList<>();
        for (Specialite specialite : specialites){
            int count = msService.countBySpecialite(specialite);
            counts.add(count);
        }
        model.addAttribute("counts", counts);
        model.addAttribute("listeSpecialites", specialites);
        return "statMedecinService";
    }
}
