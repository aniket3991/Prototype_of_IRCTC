package anudip.project.irctc.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import anudip.project.irctc.entity.Booking;
import anudip.project.irctc.entity.Train;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer>{
	long countByTrainAndSeatTypeAndTravelDate(Train train, String seatType, LocalDate travelDate);
}
