package anudip.project.irctc.service.ServiceImp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import anudip.project.irctc.entity.Destination;
import anudip.project.irctc.entity.Source;
import anudip.project.irctc.entity.Station;
import anudip.project.irctc.entity.Train;
import anudip.project.irctc.entity.TrainAvailableDays;
import anudip.project.irctc.model.Route;
import anudip.project.irctc.repository.BookingRepository;
import anudip.project.irctc.repository.DestinationRepository;
import anudip.project.irctc.repository.SourceRepository;
import anudip.project.irctc.repository.StationRepository;
import anudip.project.irctc.repository.TrainAvailableRepository;
import anudip.project.irctc.repository.TrainRepository;
import anudip.project.irctc.service.TrainService;

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
		
		for(Train train : trainList)
			scheduleLis.add(getTrainScheduleList(train));
		
		return scheduleLis;
	}
	
	@Override
	public List<Train> getAllTrains(String source, String destination, LocalDate date){
		
		List<Train> bySourceDestinationList = filterTrainBySourceAndDestination(source, destination);
		List<Train> filterByDay = filterTrainByDay(bySourceDestinationList, date);
		
		for(Train train : filterByDay) {
			train.setSeat1ACount(train.getSeat1ACount() - 
					(int)bookingRepository.countByTrainAndSeatTypeAndTravelDate(train,"AC 1", date));
			train.setSeat2ACount(train.getSeat2ACount() - 
					(int)bookingRepository.countByTrainAndSeatTypeAndTravelDate(train,"AC 2", date));
			train.setSeatSlCount(train.getSeatSlCount() - 
					(int)bookingRepository.countByTrainAndSeatTypeAndTravelDate(train,"SLP", date));
			train.setSeatGenCount(train.getSeatGenCount() - 
					(int)bookingRepository.countByTrainAndSeatTypeAndTravelDate(train,"GEN", date));
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
						s.getTrain().setArrivalTime(changeTime(s.getTrain().getDepartureTime(), s.getRequiredMinutes()));
						s.getTrain().setDepartureTime(changeTime(s.getTrain().getDepartureTime(), d.getRequiredMinutes()));
						
						trains.add(s.getTrain());

					}
				}
			}
		}
		return trains;
	}
	
	private List<Train> filterTrainByDay(List<Train> trains, LocalDate date){
		int day = date.getDayOfWeek().getValue();
		List<Train> trainList = new ArrayList<>();
		
		for(Train train : trains) {
			List<TrainAvailableDays> trainScheduleList = trainAvailableRepository.findAllByTrain(train);
			
			if(isTrainAvailableOnDay(trainScheduleList, day))
				trainList.add(train);
		}
		return trainList;
	}
	
	private boolean isTrainAvailableOnDay(List<TrainAvailableDays> trainAvailableDays, int day) {
		for(TrainAvailableDays days : trainAvailableDays) {
			if(days.getDay() == day || days.getDay() == 8)
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
		hourMinute[1] += hourMinute[1].length()==1 ? "0" : "";
		return hourMinute[0] + ":" + hourMinute[1];
	}
}
