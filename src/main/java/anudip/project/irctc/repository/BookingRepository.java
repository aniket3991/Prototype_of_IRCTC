
package anudip.project.irctc.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import anudip.project.irctc.entity.Booking;
import anudip.project.irctc.entity.Train;
import anudip.project.irctc.entity.User;
import jakarta.transaction.Transactional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer>{
	long countByTrainAndSeatTypeAndTravelDate(Train train, String seatType, LocalDate travelDate);
	
	List<Booking> findAllByUser(User user);
	
	 @Query("select max(b.bookingId) from Booking b")
	 Integer findMaxBookingId();
	 
	 List<Booking> findAllByTrainAndSeatTypeAndTravelDate(Train train, String seatType, LocalDate traveDate);
	 
	 Booking findByPnr(String pnr);
	 
	 @Transactional
	 void deleteByPnr(String pnr);
}