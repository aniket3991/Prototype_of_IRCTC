package anudip.project.irctc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import anudip.project.irctc.entity.Source;
import anudip.project.irctc.entity.Station;
import anudip.project.irctc.entity.Train;

@Repository
public interface SourceRepository extends JpaRepository<Source, Integer> {
	List<Source> findAllByStation(Station station);
	List<Source> findAllByTrain(Train train);
	//Source findSourceByStationAndTrain(Train train,Station station);
	
	Source findByStationAndTrain(Station station, Train train);
}