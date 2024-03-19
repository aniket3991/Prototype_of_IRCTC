package anudip.project.irctc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import anudip.project.irctc.entity.UserVerification;
import anudip.project.irctc.model.SearchInput;
import anudip.project.irctc.service.UserService;

@Controller
public class WebPageController {

	@Autowired
	private UserService userService;

	@GetMapping(value = "/verification", params = "email")
	public String verificationPage(@RequestParam("email") String email, Model model) {
		UserVerification verification = new UserVerification();
		verification.setEmail(email);

		model.addAttribute("verification", verification);
		model.addAttribute("userEmail", email);
		return "verification";
	}
	
	@GetMapping(value = "/verification")
	public String verificationPageBlank(Model model) {
		UserVerification verification = new UserVerification();
		String email = "";
		model.addAttribute("verification", verification);
		model.addAttribute("userEmail", email);
		return "verification";
	}


	@GetMapping("/verifiedUser")
	public String verifiedUser() {
		return "verifiedUser";
	}
	

	@GetMapping("/ContactUs")
	public String searchByDate()
	{
		return "contactUs";
	}

}
