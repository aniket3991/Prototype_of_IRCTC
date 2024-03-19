package anudip.project.irctc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorController {
	
	@GetMapping("adminRegistration")
	public String alreadyRegisteredError() {
		return "Kindly follow mail for updates";
	}
	
}
