package anudip.project.irctc.service;

import java.time.LocalDate;
import java.util.List;

import anudip.project.irctc.entity.Destination;
import anudip.project.irctc.entity.Source;
import anudip.project.irctc.entity.Train;
import anudip.project.irctc.model.Route;

public interface TrainService {
	
	List<Source> getTrainBySource(String source);
	
	List<Destination> getTrainByDestination(String destination);

	List<Train> getAllTrains(String source, String destination, LocalDate date);
	
	Train getTrainByTrainNo(int trainNo);
	
	String getTrainScheduleList(Train train);
	
	List<String> getTrainScheduleList(List<Train> train);
	
	List<Route> getTrainRoute(Train train);

}
