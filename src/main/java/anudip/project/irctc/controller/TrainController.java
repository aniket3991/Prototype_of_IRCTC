package anudip.project.irctc.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import anudip.project.irctc.entity.Booking;
import anudip.project.irctc.entity.Train;
import anudip.project.irctc.entity.User;
import anudip.project.irctc.model.Route;
import anudip.project.irctc.model.SearchInput;
import anudip.project.irctc.service.TrainService;
import anudip.project.irctc.service.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("train")
public class TrainController {

	@Autowired
	private TrainService trainService;

	@Autowired
	private UserService userService;

	/**
	 * Method help to find Details of a particular train including its Route from
	 * origin to destination
	 * 
	 * @param trainNo     - train Number
	 * @param model       - Model used to bind and transfer data to UI
	 * @param httpSession - To Manage User Session
	 * @return - return to html page
	 */
	@GetMapping(value = "/details", params = "id")
	public String getTrainDetails(@RequestParam("id") String trainNo, Model model, HttpSession httpSession) {

		if (httpSession.getAttribute("email") == null)
			return "redirect:/user/login";

		Train train = trainService.getTrainByTrainNo(Integer.parseInt(trainNo));
		List<Route> route = new ArrayList<>();
		String trainShedule = "";
		boolean incorrect = true;

		if (train != null) {
			trainShedule = trainService.getTrainScheduleList(train);
			route = trainService.getTrainRoute(train);
			incorrect = false;
		}
		model.addAttribute("train", train);
		model.addAttribute("schedule", trainShedule);
		model.addAttribute("route", route);
		model.addAttribute("incorrect", incorrect);
		return "traindetails";
	}

	/**
	 * Method used to book tickets for user
	 * 
	 * @param source      - source station of journey
	 * @param destination - destination station of journey
	 * @param trainNo     - train no.
	 * @param date        - date of journey
	 * @param model       - Model used to bind and transfer data to UI
	 * @param httpSession - To Manage User Session
	 * @return - return to html page
	 */
	@GetMapping(value = "/Booking", params = { "src", "dst", "train", "date" })
	public String bookTicket(@RequestParam("src") String source, @RequestParam("dst") String destination,
			@RequestParam("train") String trainNo, @RequestParam("date") LocalDate date, Model model,
			HttpSession httpSession) {

		if (httpSession.getAttribute("email") == null)
			return "redirect:/user/login";

		User user = userService.getUserByEmail((String) httpSession.getAttribute("email"));

		Booking bookingInfo = new Booking();
		bookingInfo.setSource(source);
		bookingInfo.setDestination(destination);
		bookingInfo.setTravelDate(date);
		bookingInfo.setUser(user);
		Train train = trainService.getTrainByTrainNo(Integer.parseInt(trainNo));
		bookingInfo.setTrain(train);

		model.addAttribute("userTicket", bookingInfo);
		model.addAttribute("train", train);

		return "booking";

	}

	/**
	 * Used to hit a particular html page
	 * 
	 * @param httpSession - To Manage User Session
	 * @return - return to html page
	 */
	@GetMapping("/searchBydate")
	public String searchByDate(HttpSession httpSession) {

		if (httpSession.getAttribute("email") == null)
			return "redirect:/user/login";

		return "SearchBydate";
	}

	/**
	 * Method used to find details of all trains on a particular date
	 * 
	 * @param email       - user Email
	 * @param search      - Object with search details
	 * @param model       - Model used to bind and transfer data to UI
	 * @param httpSession - To Manage User Session
	 * @return - return to html page
	 */
	@PostMapping(value = "/searchBydate/{email}")
	public String searchTrain(@PathVariable String email, @ModelAttribute("search") SearchInput search, Model model,
			HttpSession httpSession) {

		if (httpSession.getAttribute("email") == null)
			return "redirect:/user/login";

		List<Train> trainList = trainService.getAllTrains(search.getSource(), search.getDestination(),
				search.getDate());
		List<String> scheduleList = trainService.getTrainScheduleList(trainList);

		model.addAttribute("ListOfTrain", trainList);
		model.addAttribute("scheduleList", scheduleList);
		model.addAttribute("searchDate", search.getDate());
		model.addAttribute("email", email);
		return "SearchBydate";

	}

	@GetMapping("/tickets")
	public String ticketManagement(Model model, HttpSession httpSession) {

		if (httpSession.getAttribute("email") == null)
			return "redirect:/user/login";

		User user = userService.getUserByEmail((String) httpSession.getAttribute("email"));
		List<Booking> bookingListByUser = trainService.getAllBookingByUser(user);
		Booking booking = new Booking();
		model.addAttribute("bookingList", bookingListByUser);
		model.addAttribute("selectedTicket", booking);

		return "tickets";
	}

	/**
	 * Method Used to show User booking information
	 * 
	 * @param booking     - booking object with data
	 * @param httpSession - To Manage User Session
	 * @return - return to html page
	 */

	@PostMapping(value = "/userBookingInfo", params = { "src", "dst", "date", "train" })
	public String bookingInfo(@ModelAttribute("userTicket") Booking booking, @RequestParam("src") String source,
			@RequestParam("dst") String destination, @RequestParam("date") LocalDate date,
			@RequestParam("train") String train, HttpSession httpSession) {

		if (httpSession.getAttribute("email") == null)
			return "redirect:/user/login";

		boolean status = trainService.bookTicket(booking, source, destination, date, train,
				(String) httpSession.getAttribute("email"));
		return "getTicket";
	}

	/**
	 * Method used to show tickets details and ask for confirmation from user
	 * 
	 * @param pnr         - pnr of ticket
	 * @param model       - Model used to bind and transfer data to UI
	 * @param httpSession - To Manage User Session
	 * @return - return to html page
	 */
	@GetMapping(value = "/cancel", params = "pnr")
	public String cancelTicket(@RequestParam("pnr") String pnr, HttpSession httpSession, Model model) {
		if (httpSession.getAttribute("email") == null)
			return "redirect:/user/login";

		model.addAttribute("booking", trainService.getBookingByPnr(pnr));
		return "confirmation";
	}

	/**
	 * Method used to get user confirmation and cancel the ticket
	 * 
	 * @param pnr         - pnr of ticket
	 * @param httpSession - To Manage User Session
	 * @return - return to html page
	 */
	@GetMapping(value = "/cancellation", params = "pnr")
	public String cancellationConfirm(@RequestParam("pnr") String pnr, HttpSession httpSession) {
		if (httpSession.getAttribute("email") == null)
			return "redirect:/user/login";

		trainService.cancelTicket(pnr);

		return "redirect:/train/tickets";
	}

}
