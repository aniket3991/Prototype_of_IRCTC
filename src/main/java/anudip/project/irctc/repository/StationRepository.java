package anudip.project.irctc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import anudip.project.irctc.entity.Station;

@Repository
public interface StationRepository  extends JpaRepository<Station, Integer> {
    Station findByStationName(String stationName);
}





