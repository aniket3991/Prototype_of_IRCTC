package anudip.project.irctc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import anudip.project.irctc.entity.Train;
import anudip.project.irctc.entity.TrainAvailableDays;

@Repository
public interface TrainAvailableRepository extends JpaRepository<TrainAvailableDays, Integer> {
   List<TrainAvailableDays> findAllByTrain(Train train);
}
