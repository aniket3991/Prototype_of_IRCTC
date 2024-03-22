package anudip.project.irctc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import anudip.project.irctc.entity.User;
import anudip.project.irctc.entity.UserVerification;
import anudip.project.irctc.model.Login;
import anudip.project.irctc.model.SearchInput;
import anudip.project.irctc.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("user")
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * Method used to register new user
	 * 
	 * @param user   - user object with user entered details
	 * @param result - Used for server side validation
	 * @param model  - Model used to bind and transfer data to UI
	 * @return - return to html page
	 */
	@PostMapping("/registration")
	public String createUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {

		if (result.hasErrors()) {
			return "registration";
		}
		User existedUser = userService.getUserByEmail(user.getEmail());
		if (existedUser != null)
			user.setUserId(existedUser.getUserId());
		if (existedUser == null || existedUser.getStatus() == 0) {
			userService.saveUserAndSentOtp(user);
			if (user.getRole().equalsIgnoreCase("user")) {
				return "redirect:/verification?email=" + user.getEmail();
			}
			return "redirect:/adminRegistration";
		}
		model.addAttribute("existedUser", true);
		return "registration";
	}
	
	/**
	 * Method used to hit a particular html page
	 * @param model - Model used to bind and transfer data to UI
	 * @return - return to html page
	 */
	@GetMapping("/registration")
	public String registrationPage(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "registration";
	}

	@GetMapping("/verify/{email}")
	public String verifyUser(@PathVariable("email") String email,
			@ModelAttribute("verification") UserVerification verification, Model model) {
		boolean notVerified = true;
		verification.setEmail(email);

		if (userService.verifyUser(verification)) {
			return "redirect:/user/login";
		}
		model.addAttribute("notVerified", notVerified);
		return "redirect:/verification?email=" + email;
	}

	@GetMapping("/login")
	public String login(Model model, HttpSession httpSession) {

		if (httpSession.getAttribute("email") != null)
			return "redirect:/user/home";

		Login login = new Login();
		model.addAttribute("login", login);
		return "login";
	}

	@PostMapping("/login")
	public String login(@Valid @ModelAttribute("login") Login login, BindingResult result, Model model,
			HttpSession httpSession) {
		if (result.hasErrors())
			return "login";

		if (userService.userAuthentication(login)) {
			httpSession.setAttribute("email", login.getEmail());
			httpSession.setMaxInactiveInterval(60 * 30);
			model.addAttribute("user", userService.getUserByEmail(login.getEmail()));
			return "redirect:/user/home";
		}
		model.addAttribute("incorrect", true);
		return "login";
	}

	@GetMapping(value = "/home")
	public String homePage(Model model, HttpSession httpSession) {

		if (httpSession.getAttribute("email") == null)
			return "redirect:/user/login";

		SearchInput input = new SearchInput();
		model.addAttribute("search", input);

		return "home";
	}

	@GetMapping("/logout")
	public String logout(HttpSession httpSession) {
		httpSession.invalidate();
		return "redirect:/user/login";
	}
}