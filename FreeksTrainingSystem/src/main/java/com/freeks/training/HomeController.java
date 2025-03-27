package com.freeks.training;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {
	@GetMapping(value="/")
	public String home() {
		return "home";
	}
	
	// firstWebSystem画面へ遷移
	@GetMapping(value = "/firstWebSystem")
	public String menu(Model model) {
		model.addAttribute("msg", "初めてのWebシステム");
		return "firstWebSystem";
	}
}