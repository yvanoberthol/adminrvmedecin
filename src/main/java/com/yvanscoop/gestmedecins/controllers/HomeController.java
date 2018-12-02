package com.yvanscoop.gestmedecins.controllers;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {
	
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public String login() {
		
		return "login";
	}
	
	@RequestMapping(value="/chargement",method=RequestMethod.GET)
	public String chargement() {
		
		return "chargement";
	}
	
	@RequestMapping(value="/403",method=RequestMethod.GET)
	public String accessDenied() {
		return "403";
	}
	
	@RequestMapping(value= {"/menuPrincipal","/"},method=RequestMethod.GET)
	public String menuPrincipal(HttpSession session) {
		session.setAttribute("date", new Date());
		return "menuAdministration";
	}

}
