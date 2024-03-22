package anudip.project.irctc.service.ServiceImp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import anudip.project.irctc.entity.Booking;
import anudip.project.irctc.entity.Destination;
import anudip.project.irctc.entity.Source;
import anudip.project.irctc.entity.Station;
import anudip.project.irctc.entity.Train;
import anudip.project.irctc.entity.TrainAvailableDays;
import anudip.project.irctc.entity.User;
import anudip.project.irctc.model.Route;
import anudip.project.irctc.repository.BookingRepository;
import anudip.project.irctc.repository.DestinationRepository;
import anudip.project.irctc.repository.SourceRepository;
import anudip.project.irctc.repository.StationRepository;
import anudip.project.irctc.repository.TrainAvailableRepository;
import anudip.project.irctc.repository.TrainRepository;
import anudip.project.irctc.repository.UserRepository;
import anudip.project.irctc.service.TrainService;

import jakarta.annotation.Generated;

import jakarta.servlet.http.HttpSession;

@Service
public class TrainServiceImpl implements TrainService {

	@Autowired
	private TrainRepository trainRepository;

	@Autowired
	private TrainAvailableRepository trainAvailableRepository;

	@Autowired
	private SourceRepository sourceRepository;

	@Autowired
	private DestinationRepository destinationRepository;

	@Autowired
	private StationRepository stationRepository;

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public Train getTrainByTrainNo(int trainNo) {
		return trainRepository.findByTrainNo(trainNo).orElse(null);
	}

	@Override
	public String getTrainScheduleList(Train train) {
		Map<Integer, String> dayMapping = new HashMap<>();
		dayMapping.put(1, "Mon ");
		dayMapping.put(2, "Tue ");
		dayMapping.put(3, "Wed ");
		dayMapping.put(4, "Thus ");
		dayMapping.put(5, "Fri ");
		dayMapping.put(6, "Sat ");
		dayMapping.put(7, "Sun ");
		dayMapping.put(8, "Daily");

		StringBuilder schedule = new StringBuilder();
		List<TrainAvailableDays> trainScheduleList = trainAvailableRepository.findAllByTrain(train);

		for (TrainAvailableDays trainSchedule : trainScheduleList) {
			schedule.append(dayMapping.get(trainSchedule.getDay()));
		}
		return schedule.toString();
	}

	@Override
	public List<String> getTrainScheduleList(List<Train> trainList) {

		List<String> scheduleLis = new ArrayList<>();

		for (Train train : trainList)
			scheduleLis.add(getTrainScheduleList(train));

		return scheduleLis;
	}

	@Override
	public List<Train> getAllTrains(String source, String destination, LocalDate date) {

		List<Train> bySourceDestinationList = filterTrainBySourceAndDestination(source, destination);
		List<Train> filterByDay = filterTrainByDay(bySourceDestinationList, date);

		for (Train train : filterByDay) {
			train.setSeat1ACount(train.getSeat1ACount()
					- (int) bookingRepository.countByTrainAndSeatTypeAndTravelDate(train, "AC 1", date));
			train.setSeat2ACount(train.getSeat2ACount()
					- (int) bookingRepository.countByTrainAndSeatTypeAndTravelDate(train, "AC 2", date));
			train.setSeatSlCount(train.getSeatSlCount()
					- (int) bookingRepository.countByTrainAndSeatTypeAndTravelDate(train, "SLP", date));
			train.setSeatGenCount(train.getSeatGenCount()
					- (int) bookingRepository.countByTrainAndSeatTypeAndTravelDate(train, "GEN", date));
		}

		return filterByDay;
	}

	private List<Train> filterTrainBySourceAndDestination(String source, String destination) {
		List<Train> trains = new ArrayList<Train>();

		List<Source> sourceList = getTrainBySource(source);
		List<Destination> destinationList = getTrainByDestination(destination);

		for (Source s : sourceList) {
			for (Destination d : destinationList) {
				if (d.getTrain().getTrainId() == s.getTrain().getTrainId()) {
					if (s.getRequiredMinutes() < d.getRequiredMinutes()) {

						s.getTrain().setSource(source);
						s.getTrain().setDestination(destination);
						s.getTrain()
								.setArrivalTime(changeTime(s.getTrain().getDepartureTime(), s.getRequiredMinutes()));
						s.getTrain()
								.setDepartureTime(changeTime(s.getTrain().getDepartureTime(), d.getRequiredMinutes()));
						s.getTrain().setSeat1APrice(setPrice((int) (d.getPrice() - s.getPrice()), "AC 1"));
						s.getTrain().setSeat2APrice(setPrice((int) (d.getPrice() - s.getPrice()), "AC 2"));
						s.getTrain().setSeatSlPrice(setPrice((int) (d.getPrice() - s.getPrice()), "SLP"));
						s.getTrain().setSeatGenPrice(d.getPrice() - s.getPrice());

						trains.add(s.getTrain());

					}
				}
			}
		}
		return trains;
	}

	public List<Integer> getPriceBySourceDestinationAndTrain(String source, String destination, Train train) {
		Source from = sourceRepository.findByStationAndTrain(stationRepository.findByStationName(source), train);
		Destination to = destinationRepository.findByStationAndTrain(stationRepository.findByStationName(destination),
				train);
		System.out.println(to.getPrice());
		System.out.println(from.getPrice());
		int price = (int) (to.getPrice() - from.getPrice());

		List<Integer> priceList = new ArrayList<>();

		priceList.add(setPrice(price, "AC 1"));
		priceList.add(setPrice(price, "AC 2"));
		priceList.add(setPrice(price, "SLP"));
		priceList.add(setPrice(price, "GEN"));

		return priceList;
	}

	private int setPrice(int price, String seatType) {
		if (seatType.equalsIgnoreCase("AC 1"))
			return price * 6;
		if (seatType.equalsIgnoreCase("AC 2"))
			return price * 4;
		if (seatType.equalsIgnoreCase("SLP"))
			return price * 2;
		return price;
	}

	private List<Train> filterTrainByDay(List<Train> trains, LocalDate date) {
		int day = date.getDayOfWeek().getValue();
		List<Train> trainList = new ArrayList<>();

		for (Train train : trains) {
			List<TrainAvailableDays> trainScheduleList = trainAvailableRepository.findAllByTrain(train);

			if (isTrainAvailableOnDay(trainScheduleList, day))
				trainList.add(train);
		}
		return trainList;
	}

	private boolean isTrainAvailableOnDay(List<TrainAvailableDays> trainAvailableDays, int day) {
		for (TrainAvailableDays days : trainAvailableDays) {
			if (days.getDay() == day || days.getDay() == 8)
				return true;
		}
		return false;
	}

	@Override
	public List<Source> getTrainBySource(String source) {
		Station station = stationRepository.findByStationName(source);
		List<Source> sourceList = sourceRepository.findAllByStation(station);

		return sourceList;
	}

	@Override
	public List<Destination> getTrainByDestination(String destination) {
		Station station = stationRepository.findByStationName(destination);
		List<Destination> destinationList = destinationRepository.findAllByStation(station);

		return destinationList;
	}

	@Override
	public List<Route> getTrainRoute(Train train) {
		List<Source> sourceList = sourceRepository.findAllByTrain(train);
		Collections.sort(sourceList, (o1, o2) -> o1.getRequiredMinutes() - o2.getRequiredMinutes());
		return createRoute(train, sourceList);
	}

	private List<Route> createRoute(Train train, List<Source> sourceList) {
		List<Route> routes = new ArrayList<>();
		int total = sourceList.size();

		routes.add(new Route(1, sourceList.get(0).getStation().getStationCode(),
				sourceList.get(0).getStation().getStationName(), train.getArrivalTime(), train.getDepartureTime()));

		for (int i = 1; i < sourceList.size() - 1; i++) {
			String arrivalTime = changeTime(train.getDepartureTime(), sourceList.get(i).getRequiredMinutes());

			routes.add(new Route(i + 1, sourceList.get(i).getStation().getStationCode(),
					sourceList.get(i).getStation().getStationName(), arrivalTime, changeTime(arrivalTime, 5)));
		}

		Station station = stationRepository.findByStationName(train.getDestination());
		Destination destination = destinationRepository.findByStationAndTrain(station, train);

		routes.add(new Route(total + 1, station.getStationCode(), station.getStationName(),
				changeTime(train.getDepartureTime(), destination.getRequiredMinutes()), "-"));

		return routes;
	}

	private String changeTime(String time, int minutes) {
		int hour = minutes / 60;
		minutes %= 60;

		String[] hourMinute = time.split(":");
		String minute = hourMinute[1];
		hour += (Integer.parseInt(minute) + minutes) / 60;
		hourMinute[1] = String.valueOf((Integer.parseInt(minute) + minutes) % 60);

		hourMinute[0] = String.valueOf((Integer.parseInt(hourMinute[0]) + hour) % 24);
		hourMinute[1] += hourMinute[1].length() == 1 ? "0" : "";
		return hourMinute[0] + ":" + hourMinute[1];
	}

	@Override
	public List<Booking> getAllBookingByUser(User user) {
		return bookingRepository.findAllByUser(user);
	}

//	@Override
//	public boolean bookTicket(Booking booking) {
//		System.out.println("Inside Book Ticket");
//		Station sourceStation = findStationBySource(booking.getSource());
//		int stationId = sourceStation.getStationId();
//		System.out.println(stationId);
//		Station stationDes = findStationByDestination(booking.getDestination());
//		int destinationId = stationDes.getStationId();
//		System.out.println(destinationId);
//		Source source = sourceRepository.findSourceByStationAndTrain(booking.getTrain(), sourceStation);
//
//		System.out.println("got source object");
//		booking.setPnr(generatePnr());
//		booking.setSeatNo("46");
//
//		return false;
//
//	}

	@Override
	public Booking getBookingByPnr(String pnr) {
		return bookingRepository.findByPnr(pnr);
	}

	public boolean bookTicket(Booking booking, String source, String destination, LocalDate date, String train,
			String email) {
		
		float newPrice = 0;
		
		Station sourceStation = findStationBySource(source);
		
		Station stationDes = findStationByDestination(destination);
		
		Train trainObj = trainRepository.getTrainByTrainNo(Integer.parseInt(train));
		Source sourceObj = sourceRepository.findByStationAndTrain(sourceStation, trainObj);
		Destination destinationObj = destinationRepository.findByStationAndTrain(stationDes, trainObj);
		
		User user = userRepository.findByEmail(email);
		
		float price = destinationObj.getPrice() - sourceObj.getPrice();
		if (booking.getSeatType().equals("AC 1")) {
			newPrice = price * 6;
		} else if (booking.getSeatType().equals("AC 2")) {
			newPrice = price * 4;
		} else if (booking.getSeatType().equals("SLP")) {
			newPrice = price * 2;
		} else {
			newPrice = price;
		}
		
		booking.setTrain(trainObj);
		booking.setSource(source);
		booking.setDestination(destination);
		booking.setTravelDate(date);
		booking.setUser(user);
		booking.setPrice(newPrice);
		booking.setPnr(generatePnr());
		
		booking.setSeatNo(generateSeatNo(trainObj, booking.getSeatType(), date));
		booking.setStatus("confirm");
		bookingRepository.save(booking);

		return false;
	}

	private String generatePnr() {
		int pnr = 181286;
		Integer id = bookingRepository.findMaxBookingId();
		return "PNR" + (pnr + id);
	}

	public void cancelTicket(String pnr) {
		bookingRepository.deleteByPnr(pnr);

	}

	public String generateSeatNo(Train train, String seatType, LocalDate date) {
		List<Booking> bookingList = bookingRepository.findAllByTrainAndSeatTypeAndTravelDate(train, seatType, date);
		int size = 0;
		String seat = "CNF/";
		if (seatType.equalsIgnoreCase("AC 1")) {
			size = train.getSeat1ACount();
			seat += "AC1/";
		}

		else if (seatType.equalsIgnoreCase("AC 2")) {
			size = train.getSeat2ACount();
			seat += "AC2/";
		}

		else if (seatType.equalsIgnoreCase("SLP")) {
			size = train.getSeatSlCount();
			seat += "SLP/";
		}

		else {
			size = train.getSeatGenCount();
			seat += "GEN/";
		}

		Map<Integer, String> seatMap = new HashMap<Integer, String>();
		for (Booking booking : bookingList) {
			String[] sp = booking.getSeatNo().split("/");
			seatMap.put(Integer.parseInt(sp[2]), sp[2]);
		}

		for (int i = 1; i <= size; i++) {
			if (seatMap.get(i) == null) {
				seat += i;
				break;
			}
		}
		return seat;
	}

	public Station findStationBySource(String source) {
		Station station = stationRepository.findByStationName(source);

		return station;
	}

	public Station findStationByDestination(String destination) {
		Station station = stationRepository.findByStationName(destination);

		return station;
	}
}
