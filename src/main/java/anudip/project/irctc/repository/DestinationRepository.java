package anudip.project.irctc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import anudip.project.irctc.entity.Destination;
import anudip.project.irctc.entity.Source;
import anudip.project.irctc.entity.Station;
import anudip.project.irctc.entity.Train;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Integer> {
  List<Destination> findAllByStation(Station station);
  Destination findByStationAndTrain(Station station, Train train);
}
