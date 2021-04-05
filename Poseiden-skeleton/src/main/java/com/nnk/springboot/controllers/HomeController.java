package com.nnk.springboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController
{
	/**
	 * Home page
	 *
	 * @param model
	 * @return uri home
	 */
	@RequestMapping("/")
	public String home(Model model)
	{
		return "home";
	}

}
